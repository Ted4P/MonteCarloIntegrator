package CAS;

import java.util.Scanner;

import evaluator.Evaluator;

public class CAS {
	private static String in = "", last = "";
	private static Evaluator eval = new Evaluator();
	
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.print("Welcome to the MX Computer Algebra System. Written by Ted Pyne, Julia McClellan, and Hyun Choi.\nFor help text, type 'help', or 'man' followed by the specific page.\nTo exit, type 'exit'\n\n");
		while(!in.equals("exit")){
			System.out.print(": ");
			in = scan.nextLine();
			String out = processIn();
			
			
			System.out.println(out);
			last = out;
		}
	}

	private static String processIn() {
		String ret;
		in.replaceAll("LAST", last);
		
		if(in.startsWith("DERIVE")) ret = deriveFunc(in.substring(6));
		else if(in.startsWith("EVAL")) ret = evalFunc(in.substring(4));
		else ret = "ERROR";
		return ret;
	}

	private static String evalFunc(String substring) {
		eval.parse(func);
	}

	private static String deriveFunc(String func) {							//DERIVE x^2,x
		int comma = func.indexOf(",");
		if(comma==-1) return "ERROR: BAD FORMAT!";
		
		Character key = func.charAt(comma+1);
		func = func.substring(0, comma);
		eval.parse(func);
		eval.derive(key);
		return eval.toString();
	}
}
