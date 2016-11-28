import java.util.Scanner;

public class Vector {
	public static void main(String[]args){
		System.out.println("Inner Product: ");
		System.out.println(innerProduct(vectorInput(0,0,0),vectorInput(0,0,0)));
	}
	
	public static double innerProduct(double[][]v1,double[][]v2){
		double product = 0;
		if(v1[0].length>1&&v1.length==1){
			v1 = Matrix.transpose(v1);
		}
		if(v2[0].length>1&&v2.length==1){
			v2 = Matrix.transpose(v2);
		}
		for(int i = 0;i<v1.length;i++){
			product+=v1[i][0]*v2[i][0];
		}
		return product;
	}
	
	public static double[][] vectorInput(double x,double y,double z){
		Scanner input = new Scanner(System.in);
		try{
			System.out.print("Enter Size of the Vector: ");
			int size = Integer.parseInt(input.nextLine());
			double[][]vector = new double[1][size];
			System.out.print("Enter Vector (#,#,#): ");
			String v = input.nextLine();
			for(int i = 0;i<size;i++)
				vector[0][i] = Function.calculate(v.split(",")[i],x,y,z);
			return vector;
		} catch (Exception e) {
			System.out.println("Invalid Entry");
			return vectorInput(x,y,z);
		}
	}
}
