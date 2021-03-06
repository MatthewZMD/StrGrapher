import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Matthew on 2016-11-27.
 */
public class Grapher extends JFrame{
    private static double leftX,rightX,upY,downY,dx,dh;
    public static int height = 800,width = 1200;
    public static String function ="";
    public static Grapher graph;
    public static JTextField functionField,xMin,xMax,yMin,yMax;
    public static JButton button;
    public static boolean changed = false;

    public Grapher(){
        setSize(1200,800);
//        setResizable(false);
        JPanel panel=new JPanel();
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseWheelListener(new mouseScroller());
        addMouseMotionListener(new mouseMotionListener());
        functionField = new JTextField(10);
        functionField.addKeyListener(new keyListener());
        functionField.requestFocus();
        xMin = new JTextField("-100.0");
        xMin.addKeyListener(new keyListener());
        xMax = new JTextField("100.0");
        xMax.addKeyListener(new keyListener());
        yMin = new JTextField("-100.0");
        yMin.addKeyListener(new keyListener());
        yMax = new JTextField("100.0");
        yMax.addKeyListener(new keyListener());
        button =new JButton("DRAW");
        button.addActionListener(new buttonListener());
        button.addKeyListener(new keyListener());
        panel.add(new JLabel("f(x) = "));
        panel.add(functionField);
        panel.add(new JLabel("   "));
        panel.add(xMin);
        panel.add(new JLabel(" ≤ x ≤ "));
        panel.add(xMax);
        panel.add(new JLabel("   "));
        panel.add(yMin);
        panel.add(new JLabel(" ≤ y ≤ "));
        panel.add(yMax);
        panel.add(new JLabel("   "));
        panel.add(button);
    }

    public static void main(String []args){
        graph = new Grapher();
        graph.setVisible(true);
        do{
            sync();
            if(changed) {
                graph.repaint();
                changed = false;
            }
        }while (!changed);
    }

    public void paint(Graphics g) {
        super.paintComponents(g);
        double x = leftX,y,y2;
        g.setColor(new Color(160,160,160));
        g.drawLine(0,(int)vtoY(0),width,(int)vtoY(0));
        g.drawLine((int)vtoX(0),0,(int)vtoX(0),height);
        g.setColor(new Color(0,0,153));
        if(!function.equals("")){
            for(int w = 0;w<width;w++){
                y = Function.calculate(function,x,0,0);
                y2 = Function.calculate(function,(x+dx),0,0);
                if(y>=downY&&y<=upY&&y2>=downY&&y2<=upY) {
//                System.out.println(x+" "+y+" "+(x+dx)+" "+y2);
                    g.drawLine(w, (int)vtoY(y), w + 1, (int)vtoY(y2));
                }
                x+=dx;
            }
        }
    }

    private double vtoX(double x){
        double countX = leftX;
        for(int w = 0;w<width;w++) {
            if (x >= countX - dx && x <= countX + dx) {
                return w;
            }
            countX += dx;
        }
        return (int)x;
    }

    private double vtoY(double y){
        double countY = downY;
        for(int h = height;h>=0;h--) {
            if (y >= countY - dh && y <= countY + dh) {
                return h;
            }
            countY += dh;
        }
        return (int)y;
    }

    protected static void sync(){
        function = functionField.getText();
        width = graph.getWidth();
        height = graph.getHeight();
        try {
            leftX = Double.parseDouble(xMin.getText());
            rightX = Double.parseDouble(xMax.getText());
            upY = Double.parseDouble(yMax.getText());
            downY = Double.parseDouble(yMin.getText());
        }catch(Exception e){
        }finally {
            dx = (rightX-leftX)/width;// dx/pixel
            dh = (upY-downY)/height;// dy/pixel
//            changeX=dx*100;
//            changeY=dh*100;
        }
    }

    private class buttonListener implements ActionListener {
        public void actionPerformed(ActionEvent event)  {
            functionField.setText(Function.prepare(function));
            changed = true;
            sync();
        }
    }

    private class keyListener implements KeyListener{
        double changeX,changeY;
        int factor = 10;
        @Override
        public void keyPressed(KeyEvent e) {
            //Get the key pressed
            int key = e.getKeyCode();
            if(e.getSource()==button){
                if(key==KeyEvent.VK_RIGHT){
                    changeX = dx*factor;
                    leftX+=changeX;
                    xMin.setText(Double.toString(leftX));
                    rightX+=changeX;
                    xMax.setText(Double.toString(rightX));
                    changed = true;
                    sync();
                }else if(key==KeyEvent.VK_LEFT){
                    changeX = dx*factor;
                    leftX-=changeX;
                    xMin.setText(Double.toString(leftX));
                    rightX-=changeX;
                    xMax.setText(Double.toString(rightX));
                    changed = true;
                    sync();
                }
                if(key==KeyEvent.VK_UP){
                    changeY = dh*factor;
                    upY+=changeY;
                    yMax.setText(Double.toString(upY));
                    downY+=changeY;
                    yMin.setText(Double.toString(downY));
                    changed = true;
                    sync();
                }else if(key==KeyEvent.VK_DOWN){
                    changeY = dh*factor;
                    upY-=changeY;
                    yMax.setText(Double.toString(upY));
                    downY-=changeY;
                    yMin.setText(Double.toString(downY));
                    changed = true;
                    sync();
                }
            }else{
                if(key==KeyEvent.VK_ENTER){
                    functionField.setText(Function.prepare(function));
                    changed = true;
                    sync();
//                    x^3/3^ep
                    button.requestFocus();
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

    }
        @Override
        public void keyReleased(KeyEvent e) {
//            System.out.println(false);
        }
    }

    private class mouseScroller implements MouseWheelListener{
        double changeX,changeY;
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            changeX = dx*5;
            changeY = dh*5;
//            System.out.println(dx+" "+dh);
            if(e.getWheelRotation()>0){
//                System.out.println("zoom out");
                leftX-=changeX;
                xMin.setText(Double.toString(leftX));
                rightX+=changeX;
                xMax.setText(Double.toString(rightX));
                upY-=changeY;
                yMax.setText(Double.toString(upY));
                downY+=changeY;
                yMin.setText(Double.toString(downY));
                changed = true;
                sync();
            }else if(e.getWheelRotation()<0){
//                System.out.println("zoom in");
                leftX+=changeX;
                xMin.setText(Double.toString(leftX));
                rightX-=changeX;
                xMax.setText(Double.toString(rightX));
                upY+=changeY;
                yMax.setText(Double.toString(upY));
                downY-=changeY;
                yMin.setText(Double.toString(downY));
                changed = true;
                sync();
            }
        }
    }

    private class mouseMotionListener implements MouseMotionListener{
        double changeX,changeY;
        int preX,preY,x=0,y=0;
        int factor,limit = 20;
        int dxMouse,dyMouse;
        @Override
        public void mouseDragged(MouseEvent e) {
//            System.out.println(x+"-"+y+"/"+preX+"-"+preY);
            preX = x;
            preY = y;
            x = e.getX();
            y = e.getY();
            dxMouse = x-preX;
            dyMouse = y-preY;
//            System.out.println(dxMouse+" "+dyMouse);
            if(dxMouse<0){
                factor = Math.min(Math.abs(dxMouse)/2,limit);
                changeX = dx*factor;
                leftX+=changeX;
                xMin.setText(Double.toString(leftX));
                rightX+=changeX;
                xMax.setText(Double.toString(rightX));
                changed = true;
                sync();
            }else if(dxMouse>0){
                factor = Math.min(Math.abs(dxMouse)/2,limit);
                changeX = dx*factor;
                leftX-=changeX;
                xMin.setText(Double.toString(leftX));
                rightX-=changeX;
                xMax.setText(Double.toString(rightX));
                changed = true;
                sync();
            }
            if(dyMouse>0){
                factor = Math.min(Math.abs(dyMouse)/2,limit);
                changeY = dh*factor;
                upY+=changeY;
                yMax.setText(Double.toString(upY));
                downY+=changeY;
                yMin.setText(Double.toString(downY));
                changed = true;
                sync();
            }else if(dyMouse<0){
                factor = Math.min(Math.abs(dyMouse)/2,limit);
                changeY = dh*factor;
                upY-=changeY;
                yMax.setText(Double.toString(upY));
                downY-=changeY;
                yMin.setText(Double.toString(downY));
                changed = true;
                sync();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}
