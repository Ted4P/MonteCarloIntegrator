package integrator;

import java.util.Map;
import java.util.Scanner;

import evaluator.Evaluator;
import evaluator.MutableDouble;

public class MultiVar {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.print("INTEGRAND: ");
		String equation = scan.next();
		
		Evaluator eval = new Evaluator();
		eval.parse(equation);
		
		Map<String, MutableDouble> vars = eval.getKeys();
		int numVars = vars.size();
		if(numVars==0) return;
		
		int[][] varBounds = new int[numVars][2];		//Lower and upper bound for each var
		for(int i = 0; i < numVars; i++){
			System.out.println("Lower bound for " + )
		}
	}
}
