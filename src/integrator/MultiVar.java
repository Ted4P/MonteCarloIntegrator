package integrator;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import evaluator.Evaluator;
import evaluator.MutableDouble;

public class MultiVar {
	public static void main(String[] args) throws Exception{
		Scanner scan = new Scanner(System.in);
		System.out.print("INTEGRAND: ");
		String equation = scan.next();
		
		Evaluator eval = new Evaluator();
		eval.parse(equation);
		
		Map<String, MutableDouble> vars = eval.getKeys();
		Set<String> varNames = vars.keySet();
		int numVars = vars.size();
		if(numVars==0) return;
		
		double[][] varBounds = new double[numVars][2];		//Lower and upper bound for each var
		int i = 0;
		for(String varName: varNames){
			System.out.println("Lower bound for " + varName);
			varBounds[i][0] = scan.nextDouble();
			System.out.println("Upper bound for " + varName);
			varBounds[i][1] = scan.nextDouble();
			i++;
		}
		
		//Now do the parsing
		System.out.print("NUM SAMPLES: ");
		int samples = scan.nextInt();
		double sum = 0;
		for(int j = 0; j < samples; j++){
			int k = 0;
			for(String var: varNames){
				vars.get(var).setVal(randInBound(varBounds[k]));
				k++;
			}
			sum+=eval.evaluate(vars);
		}
		sum/=samples;
		for(double[] bound: varBounds){
			sum*=bound[1]-bound[0];
		}
		
		System.out.println("SUM FOUND TO BE: " + sum);
	}
	
	public static double randInBound(double[] arr){
		return arr[0] + (Math.random()*(arr[1]-arr[0]));
	}
}
