import java.util.Scanner;
public class Function {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		double x = 0,y = 0,z = 0;
		boolean run = true;
		while(run){
			System.out.print("Enter x,y,z-values if exist (x,y,z): ");
			String variables = input.nextLine();
			try{
				x = calculate(variables.split(",")[0],x,y,z);
			}catch(Exception e){ x = 0;}
			try{
				y = calculate(variables.split(",")[1],x,y,z);
			}catch(Exception e){ y = 0;}
			try{
				z = calculate(variables.split(",")[2],x,y,z);
			}catch(Exception e){ z = 0;}
			System.out.print("f = ");
			String f = input.nextLine();
			System.out.println("f(x,y,z) = "+calculate(f,x,y,z));
			try{
//			System.out.println("Full: "+calculation(f,x,y,z));
			}catch(Exception e){}
			System.out.print("Y/N - Use the Program Again: ");
			String choice = input.nextLine();
			run = choice.equalsIgnoreCase("Y");
		}
		input.close();
	}
	
	public static double calculate(String f,double x,double y,double z){
		try {
			f = removeSpace(f);
//			System.out.println(f);
			f = addX(f, 0);
//			System.out.println(f);
			f = fill(f, 0);
//			System.out.println(f);
			return calculation(f, x, y, z);
		}catch(Exception e){
//			e.printStackTrace();
//			System.out.println("Invalid Function");
			return 0;
		}
	}

	private  static String removeSpace(String f){
        if(f.length()<=1) {
            return f;
        }else if(f.charAt(0)==' '){
            return removeSpace(f.substring(1));
        }else{
            return f.substring(0,1)+removeSpace(f.substring(1));
        }
    }
	
	private static double calculation(String f,double x,double y,double z){
		f = substitute(f,x,y,z);
//		System.out.println(f);
		double preValue = 0,value = 0;
		int index = 0,preIndex = -1;
		if(f.length()>1){
			for(int i = f.length()-1;i>=0;i--){
				if(isOperator(f,i)){
					index = i;
					i = -1;
//					System.out.println(f);
					value = Double.parseDouble(f.substring(index+1));
				}
			}

			if(index==0) return Double.parseDouble(f);
			for(int j = index-1;j>=0;j--){
				if(isOperator(f,j)){
					if(f.charAt(j)=='-'&&isOperator(f,Math.max(j-1, 0))){
						preIndex = Math.max(j-1, 0);
						j = -1;
					}else{
						preIndex = j;
						j = -1;
					}
				}
			}
			if(f.substring(preIndex+1,index).length()>0){
//				if(f.indexOf("E")!=-1){
//					System.out.println(f+" "+x);
//					f = f.substring(preIndex + 1,index);
//					String exp = f.substring(f.indexOf("E")+1);
//					preValue = Double.parseDouble(f.substring(0,f.indexOf("E")))*Math.pow(10,Double.parseDouble(exp));
//				}else{
					preValue = Double.parseDouble(f.substring(preIndex + 1, index));
//				}
			}else{
				preValue = Double.parseDouble(f.substring(index));
				value = 0;
//				System.out.println(true);
			}
			
			if(preIndex==-1){
//				System.out.println(f+" "+index+" "+f.substring(0,index));
//				if(f.indexOf("E")!=-1){
//					return Double.parseDouble(f.substring(0,f.indexOf("E")))*Math.pow(10,Double.parseDouble(f.substring(f.indexOf("E")+1)));
//				}else{
//				System.out.println(f+" "+f.substring(preIndex+1,index)+" & "+value+" & " +index);
					return operation(f.substring(0,index),Double.toString(value),f.charAt(index));
//				}
			}else{
				return operation(Double.toString(operation(Double.toString(calculation(f.substring(0,preIndex),x,y,z)),Double.toString(preValue),f.charAt(preIndex))),Double.toString(value),f.charAt(index));
			}
		}else if(f.length()==1)return Double.parseDouble(f);
		else return 0;
	}

	private static String substitute(String f,double x,double y,double z){
//		System.out.println(f);
		String newF = "";
		int countB = 0;
		for(int i = 0;i<f.length();i++){
			if(f.charAt(i)=='x') newF+=x;
			else if(f.charAt(i)=='y') newF+=y;
			else if(f.charAt(i)=='z') newF+=z;
			else if(f.charAt(i)=='e') newF+=Math.E;
			else if(f.charAt(i)=='p') newF+=Math.PI;
			else if(f.charAt(i)=='('){
//				System.out.println(true);
				for(int j = i;j<f.length();j++){
					if(f.charAt(j)=='('){
						countB++;
					}else if(f.charAt(j)==')'){
						countB--;
					}
					if(countB==0){
						countB = j;
						j = f.length();
					}
				}
				double result = calculation(f.substring(i+1,countB),x,y,z);
//				System.out.println("Result: "+result+" "+(i+1)+" "+countB);
				newF+=result;
				i = countB;
				countB = 0;
			}else newF+=f.charAt(i);
		}
//		System.out.println(newF);
		newF = removeDuplicate(newF);
//		System.out.println(newF);
		return newF;
	}

	private static String removeDuplicate(String f){
		if(f.length()>1&&f.charAt(0)=='-'){
			if(f.charAt(1)=='-') {
				return "+" + removeDuplicate(f.substring(2));
			}else if(f.charAt(1)=='+'){
				return "-" + removeDuplicate(f.substring(2));
			}else return f.substring(0, 1)+removeDuplicate(f.substring(1));
		}if(f.length()>1&&f.charAt(0)=='+'){
			if(f.charAt(1)=='-') {
				return "-" + removeDuplicate(f.substring(2));
			}else if(f.charAt(1)=='+'){
				return "+" + removeDuplicate(f.substring(2));
			}else return f.substring(0, 1)+removeDuplicate(f.substring(1));
		}else if(f.length()>1)return f.substring(0, 1)+removeDuplicate(f.substring(1));
		else return f;
	}
	
	private static String addX(String f,int i){
		if(i<f.length()){
			String newF = "";
			if(!(f.charAt(i)=='*'||f.charAt(i)=='/'||f.charAt(i)=='^')){
				if(f.charAt(i)=='-'&&(f.charAt(Math.max(0, i-1))=='('||i==0)&&!isVariable(f,i+1)&&!isOperator(f,i+1)){
					for(int j = i+1;j<f.length();j++){
//						System.out.println(f);
						if(isOperator(f,j)||isVariable(f,j)){
							newF = f.substring(0, i)+"("+f.substring(i,j)+")"+f.substring(j);
							return addX(newF,i+2);
						}
					}
				}
				boolean variableNext = (f.charAt(Math.min(i+1, f.length()-1))=='(')||isVariable(f,Math.min(i+1, f.length()-1));				
				if((!isOperator(f,i))&&variableNext&&i!=f.length()-1){
//					System.out.println(i+" "+true);
					newF = f.substring(0,i+1)+"*"+f.substring(i+1);
					return addX(newF,i+1);
				}else if(i!=f.length()-1&&f.charAt(i)==')'&&f.charAt(Math.min(i+1, f.length()-1))=='('){
					newF = f.substring(0,i+1)+"*"+f.substring(i+1);
					return addX(newF,i+1);
				}else if(i!=f.length()-1&&(f.charAt(i)==')'||isVariable(f,i))&&(!isOperator(f,Math.min(i+1, f.length()-1)))){
//					System.out.println(i+" "+false);
					newF = f.substring(0,i+1)+"*"+f.substring(i+1);
					return addX(newF,i+1);
				}else return addX(f,i+1);
			}else return addX(f,i+1);
		}else return f;
	}

	private static String fill(String f,int i){
//		System.out.println(i);
		if(i<f.length()){
			String newF = "";
			int countP = 0,countN = 0;
			int countB = 0;
			if(f.charAt(i)=='*'||f.charAt(i)=='/'||f.charAt(i)=='^'){
				for(int p = i-1;p>=0;p--){
					if(isOperator(f,p)){
						if(f.charAt(p)==')'){
							countB++;
							countP++;
						}else if(f.charAt(p)=='('&&countB>0){
							countB--;
							countP++;
						}else{
							if(countB==0&&f.charAt(p)!='^') p = -1;
							else countP++;
						}
					}else countP++;
				}
//				System.out.println("countPrevious: "+countP);
				newF+= f.substring(0,i-countP)+"("+f.substring(i-countP,i);
//				System.out.println(i-countP-1+" "+i+" =>"+newF);
				countB = 0;
				
				for(int n = i+1;n<f.length();n++){
					if(isOperator(f,n)){
						if(f.charAt(n)=='('){
							countB++;
							countN++;
						}else if(f.charAt(n)==')'&&countB>0){
							countB--;
							countN++;
						}else{
							if(countB==0&&f.charAt(n)!='^') n = f.length();
							else countN++;
						}
					}else countN++;
				}
//				System.out.println("countNext: "+countN);
				newF+= f.substring(i,i+countN+1)+")"+f.substring(i+countN+1);
//				System.out.println(i+" "+(i+countN+1)+" =>"+newF);
				countB = 0;
				return fill(newF,i+2);
			}else return fill(f,i+1);
		}else return f;
	}
	
	private static double operation(String v1,String v2,char operator){
//		System.out.println(v1+" "+v2);
		double value1,value2;
		if(v1.indexOf("E")!=-1){
			value1 = Double.parseDouble(v1.substring(0,v1.indexOf("E")))*Math.pow(10,Double.parseDouble(v1.substring(v1.indexOf("E")+1)));
			value2 = Double.parseDouble(v2);
		}else if(v2.indexOf("E")!=-1){
			value1 = Double.parseDouble(v1);
			value2 = Double.parseDouble(v2.substring(0,v2.indexOf("E")))*Math.pow(10,Double.parseDouble(v2.substring(v2.indexOf("E")+1)));
		}else{
			value1 = Double.parseDouble(v1);
			value2 = Double.parseDouble(v2);
		}
//		System.out.println("=> "+value1+" "+operator+" "+value2);
		if(operator=='+') return value1+value2;
		else if(operator=='-') return value1-value2;
		else if(operator=='*') return value1 * value2;//(double)Math.round(value1 * value2 * 100000000) / 100000000;
		else if(operator=='/') return value1 / value2;//(double)Math.round(value1 / value2 * 100000000) / 100000000;
		else if(operator=='^') return Math.pow(value1, value2);//(double)Math.round(Math.pow(value1, value2) * 100000000) / 100000000;
		else return 0;
	}

	private static boolean isOperator(String function, int index){
		char[]operators = new char[]{'+','-','*','/','^','(',')'};
		for(char o : operators){
			if(function.charAt(index)==o){
				if(index>=1&&function.charAt(index-1)=='E'){
					return false;
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isVariable(String function, int index){
		char[]variables = new char[]{'x','y','z','e','p'};
		for(char o : variables){
			if(function.charAt(index)==o){
				return true;
			}
		}
		return false;
	}
}
