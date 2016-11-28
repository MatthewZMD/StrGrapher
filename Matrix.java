import java.util.Scanner;

public class Matrix {
	public static double x = 0,y = 0,z = 0;
	public static String[][] originalMatrix;
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		double[][] matrix1 = null;
		boolean run = true;
		char choice;
		while(run){
			System.out.print("Enter x,y,z-values if exist (x,y,z): ");
			String variables = input.nextLine();
			try{
				x = Function.calculate(variables.split(",")[0],x,y,z);
			}catch(Exception e){ x = 0;}
			try{
				y = Function.calculate(variables.split(",")[1],x,y,z);
			}catch(Exception e){ y = 0;}
			try{
				z = Function.calculate(variables.split(",")[2],x,y,z);
			}catch(Exception e){ z = 0;}
			if(matrix1==null) matrix1 = matrixInput();
			else{
				System.out.print("Y/N - New Matrix: ");
				choice = input.nextLine().charAt(0);
				if(choice=='Y')
					matrix1 = matrixInput();
				else{
					for(int i = 0;i<originalMatrix.length;i++){
						for(int j = 0;j<originalMatrix[i].length;j++){
							matrix1[i][j] = Function.calculate(originalMatrix[i][j],x,y,z);
						}
					}
				}
			}
			display(matrix1);
			System.out.println("A - Addition");
			System.out.println("D - Determinant");
			System.out.println("I - Inverse");
			System.out.println("M - Multiplication");
			System.out.println("N - Norm");
			System.out.println("S - Substraction");
			System.out.println("T - Transpose");
			System.out.print("Enter: ");
			choice = input.nextLine().charAt(0);

			if(choice=='A'||choice=='S'){
				System.out.println("N - New Matrix");
				System.out.println("T - Transpose");
				System.out.println("U - Unit Matrix");
				System.out.print("Enter: ");
				choice = input.nextLine().charAt(0);
				System.out.println("Matrix 2: ");
				double[][]matrix2 = otherMatrix(choice,matrix1);
				if(choice=='A')
					matrix1 = additionSubstraction(matrix1,matrix2,'+');
				else
					matrix1 = additionSubstraction(matrix1,matrix2,'-');
				display(matrix1);
			}else if(choice=='M'){
				System.out.println("N - New Matrix");
				System.out.println("S - Scalar");
				System.out.println("T - Transpose");
				System.out.println("U - Unit Matrix");
				System.out.println("V - Vector");
				System.out.print("Enter: ");
				choice = input.nextLine().charAt(0);
				matrix1 = otherMatrix(choice,matrix1);
				
				display(matrix1);
			}else if(choice=='T'){
				System.out.println("Transpose: ");
				matrix1 = transpose(matrix1);
				display(matrix1);
			}else if(choice=='D'){
				if(matrix1.length!=matrix1[0].length)
					System.out.println("Must be Square Matrix");
				else
					System.out.println("Determinant: "+determinant(matrix1));
			}else if(choice=='N'){
				System.out.println("The Norm of the Matrix: "+norm(matrix1));
			}else if(choice=='I'){
				matrix1 = inverseMatrix(matrix1);
				System.out.println("Matrix Inverse: ");
				display(matrix1);
			}
			System.out.print("Y/N - Use the Program Again: ");
			choice = input.nextLine().charAt(0);
			run = choice=='Y'||choice=='y';
		}
		input.close();
	}
	
	public static double[][] otherMatrix(char choice,double[][]matrix){
		Scanner input = new Scanner(System.in);
		double[][]matrix2;
		if(choice=='N'){
			matrix2 = matrixInput();
		}else if(choice=='S'){
			System.out.print("Enter a Scalar: ");
			double scalar = Function.calculate(input.nextLine(), x, y, z);
			return scalarMatrixMultiplication(matrix,scalar);
		}else if(choice=='T'){
			matrix2 = transpose(matrix);
		}else if(choice=='U'){
			matrix2 = unitMatrix(matrix);
		}else if(choice=='V'){
			double[][]vector = Vector.vectorInput(x,y,z);
			display(vector);
			return matrixVectorMultiplication(matrix,vector);
		}else{
			System.out.println("Invalid Input");
			System.out.print("Enter: ");
			return otherMatrix(input.nextLine().charAt(0),matrix);
		}
		display(matrix2);

		return matrixMultiplication(matrix,matrix2);
		
	}
	
	public static double[][]inverseMatrix(double[][]m){
		double[][]matrix = new double[m.length][m[0].length];
		if(m.length>2&&m[0].length>2){
			int countCol = 0,countRow = 0;
			for(int row = 0;row<m.length;row++){
				for(int col = 0;col<m[0].length;col++){
					int factor = (int)Math.pow(-1, row+col+2);
					double[][]newM = new double[m.length-1][m[0].length-1];
					for(int i = 0;i<m.length;i++){
						for(int j = 0;j<m[i].length;j++){
							if(row!=i){
								if(col!=j){
									newM[countRow][countCol] = m[i][j];
									if(countCol==m[0].length-2){
										countCol = 0;
										if(countRow==m.length-2){
											countRow = 0;
										}else countRow++;
									}else countCol++;
								}
							}
						}
					}
					matrix[row][col] = factor*determinant(newM);
				}
			}
			matrix = scalarMatrixMultiplication(transpose(matrix),1/determinant(m));
		}else{
			matrix[0][0] = m[1][1];
			matrix[0][1] = -m[0][1];
			matrix[1][0] = -m[1][0];
			matrix[1][1] = m[0][0];
//			display(matrix);
			matrix = scalarMatrixMultiplication(matrix,1/determinant(m));
		}
		boolean invalid = false;
		int countZero = 0;
		for(int i = 0;i<matrix.length;i++){
			for(int j = 0;j<matrix[i].length;j++){
				if(!invalid){
					if(matrix[i][j]==0){
						countZero++;
					}else invalid = Double.isInfinite(matrix[i][j])||Double.isNaN(matrix[i][j]);
				}
			}
		}
		if(invalid||countZero==matrix.length*matrix[0].length){
			display(matrix);
			System.out.println("Matrix Can't be Inversed");
		}
		else{
			System.out.println("Inverse Valid, Checking...");
			display(matrixMultiplication(m,matrix));
		}
		return matrix;
	}
	
	
	public static double determinant(double[][]m){
		double determinant = 0;
		int factor;
		int countCol = 0;
		if(m.length==2) return (m[0][0]*m[1][1])-(m[0][1]*m[1][0]);
		else{
			for(int col = 0;col<m.length;col++){
				if((col+1)%2==0) factor = -1;
				else factor = 1;
				double[][]newM = new double[m.length-1][m[0].length-1];
				for(int i = 0;i<m.length-1;i++){
					for(int j = 0;j<m.length;j++){
						if(col!=j){
							newM[i][countCol] = m[i+1][j];
							countCol++;
						}
					}
					countCol = 0;
				}
//				System.out.println(factor);
//				display(newM);
				determinant += factor*m[0][col]*determinant(newM);
			}
		}
		return determinant;
	}
	
	public static double[][] unitMatrix(double[][]matrix){
		double[][] m = new double[matrix.length][matrix[0].length];
		for(int i = 0;i<m.length;i++){
			for(int j = 0;j<m[0].length;j++){
				if(i==j){
					m[i][j] = 1;
				}else{
					m[i][j] = 0;
				}
			}
		}
		return m;
	}
	
	public static double norm(double[][]m){
		double result = 0;
		for(int i = 0;i<m.length;i++){
			for(int j = 0;j<m[i].length;j++){
				result+=(m[i][j]*m[i][j]);
			}
		}
		result = Math.sqrt(result);
		return result;
	}
	
	public static double[][] transpose(double[][]oriMatrix){
		double[][]m = new double[oriMatrix[0].length][oriMatrix.length];
		for(int j = 0;j<oriMatrix[0].length;j++){
			for(int i = 0;i<oriMatrix.length;i++){
				m[j][i]=oriMatrix[i][j];
			}
		}
		return m;
	}

	public static double[][] matrixMultiplication(double[][] m1, double[][] m2) {
		System.out.println("Matrix Multiplication: ");
		double[][] product = new double[m1.length][m2[0].length];
		product = initialize(product);
		if (m1[0].length != m2.length) {
			System.out.println("Incomplete Matrix");
			return product;
		}
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2[0].length; j++) {
				for (int o = 0; o < m1[i].length; o++) {
					product[i][j] += m1[i][o] * m2[o][j];
				}
			}
		}
		return product;
	}
	
	public static double[][] matrixVectorMultiplication(double[][]matrix,double[][]vector){
		System.out.println("Matrix-Vector Multiplication: ");
		if(vector[0].length>1&&vector.length==1){
			vector = transpose(vector);
		}
		
		double[][]result = new double[matrix.length][1];
		result = initialize(result);
		if(matrix[0].length!=vector.length){
			System.out.println("Incomplete Matrix/Vector");
			return result;
		}
		for(int i = 0;i<matrix.length;i++){
			for(int j = 0;j<matrix[i].length;j++){
				result[i][0]+=matrix[i][j]*vector[j][0];
			}
		}
		
		return result;
	}
	
	
	public static double[][]scalarMatrixMultiplication(double[][]matrix,double scalar){
		double[][]result = new double[matrix.length][matrix[0].length];
		for(int i = 0;i<result.length;i++){
			for(int j = 0;j<result[0].length;j++){
				result[i][j] = scalar*matrix[i][j];
			}
		}
		return result;
	}
	
	
	public static double[][] additionSubstraction(double[][]m1, double [][] m2,char operator){
		if(operator=='+')
			System.out.println("Matrix Addition: ");
		else if(operator=='-')
			System.out.println("Matrix Substraction: ");
		double[][]result = new double[m1.length][m1[0].length];
		if(m1.length!=m2.length||m1[0].length!=m2[0].length){
			System.out.println("Incomplete Matrix");
			return result;
		}
		for(int i = 0;i<m1.length;i++){
			for(int j = 0;j<m1[0].length;j++){
				if(operator=='+') result[i][j] = m1[i][j]+m2[i][j];
				else if(operator=='-') result[i][j] = m1[i][j]-m2[i][j];
			}
		}
		return result;
	}

	public static double[][] initialize(double[][] product) {
		for (int i = 0; i < product.length; i++) {
			for (int j = 0; j < product[i].length; j++) {
				product[i][j] = 0;
			}
		}
		return product;
	}
	

	public static void display(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("[ ");
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println("]");
		}
		System.out.println();
	}
	
	public static double[][] matrixInput() {
		Scanner input = new Scanner(System.in);
//		System.out.println("Matrix: ");
		System.out.print("Enter Size of the Matrix (ixj): ");
		String size = input.nextLine();
		try {
			int row = Integer.parseInt(size.split("x")[0]);
			int col = Integer.parseInt(size.split("x")[1]);
			double[][] matrix = new double[row][col];
			originalMatrix = new String[row][col];
			System.out.println("Enter the Matrix (#,#,#): ");
			String matrixContent;
			for (int i = 0; i < row; i++) {
				System.out.print("Row #" + (i + 1) + ": ");
				matrixContent = input.nextLine();
				for (int o = 0; o < matrixContent.split(",").length; o++) {
					originalMatrix[i][o] = matrixContent.split(",")[o];
					matrix[i][o] = Function.calculate(matrixContent.split(",")[o],x,y,z);
				}
			}
			return matrix;
		} catch (Exception e) {
			System.out.println("Invalid Entry");
			return matrixInput();
		}
	}
}
