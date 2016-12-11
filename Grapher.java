import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Matthew on 2016-11-27.
 */
public class Grapher extends JFrame{
    private static double leftX,rightX,upY,downY,dx,dh;
    public static int height = 800,width = 1200;
    public static String function ="";

    public static JTextField functionField,xMin,xMax,yMin,yMax;
    public static JButton button;
    public static boolean changed = false;

    public Grapher(){
        setSize(width,height);
        setResizable(false);
        JPanel panel=new JPanel();
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new keyListener());

        functionField = new JTextField(10);
        xMin = new JTextField("-100");
        xMax = new JTextField("100");
        yMin = new JTextField("-100");
        yMax = new JTextField("100");
        button =new JButton("DRAW");
        button.addActionListener(new buttonListener());
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
        Grapher graph =new Grapher();
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
//        super.paint(g);
        super.paintComponents(g);
        double x = leftX,y,y2;
        g.drawLine(0,vtoY(0),width,vtoY(0));
        g.drawLine(vtoX(0),0,vtoX(0),height);
        for(int w = 0;w<width;w++){
            y = Function.calculate(function,x,0,0);
            y2 = Function.calculate(function,(x+dx),0,0);
            if(y>=downY&&y<=upY&&y2>=downY&&y2<=upY) {
//                System.out.println(x+" "+y+" "+(x+dx)+" "+y2);
                g.drawLine(w, vtoY(y), w + 1, vtoY(y2));
            }
            x+=dx;
        }
    }

    private int vtoX(double x){
        double countX = leftX;
        for(int w = 0;w<width;w++) {
            if (x > countX - dx && x < countX + dx) {
                return w;
            }
            countX += dx;
        }
        return (int)x;
    }

    private int vtoY(double y){
        double countY = downY;
        for(int h = height;h>=0;h--) {
            if (y > countY - dh && y < countY + dh) {
                return h;
            }
            countY += dh;
        }
        return (int)y;
    }

    private static void sync(){
        function = functionField.getText();
        try {
            leftX = Double.parseDouble(xMin.getText());
            rightX = Double.parseDouble(xMax.getText());
            upY = Double.parseDouble(yMax.getText());
            downY = Double.parseDouble(yMin.getText());
        }catch(Exception e){
        }finally {
            dx = (rightX-leftX)/width;// dx/pixel
            dh = (upY-downY)/height;// dy/pixel
        }
    }

    private class buttonListener implements ActionListener {
        public void actionPerformed(ActionEvent event)  {
            //            sync();
            changed = true;
        }
    }

    private class keyListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(true);
            //Get the key pressed
            int key = e.getKeyCode();
            int changeX = vtoX(dx*10);
            int changeY = vtoY(dh*10);
            if(key==KeyEvent.VK_RIGHT){
                leftX+=changeX;
                rightX+=changeX;
                changed = true;
                System.out.println("right");
                xMin.setText(Double.toString(leftX));
                xMax.setText(Double.toString(rightX));
            }
            if(key==KeyEvent.VK_LEFT){
                leftX-=changeX;
                rightX-=changeX;
                changed = true;
                System.out.println("left");
                xMin.setText(Double.toString(leftX));
                xMax.setText(Double.toString(rightX));
            }
            if(key==KeyEvent.VK_UP){
                upY+=changeY;
                downY+=changeY;
                changed = true;
                System.out.println("up");
                yMax.setText(Double.toString(upY));
                yMin.setText(Double.toString(downY));
            }
            if(key==KeyEvent.VK_DOWN){
                upY-=changeY;
                downY-=changeY;
                changed = true;
                System.out.println("down");
                yMax.setText(Double.toString(upY));
                yMin.setText(Double.toString(downY));
            }
            sync();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println(false);
        }
    }
}
