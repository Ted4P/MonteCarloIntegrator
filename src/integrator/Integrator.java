package integrator;
import java.util.ArrayList;
import java.util.Scanner;

import evaluator.Evaluator;
import evaluator.KeyPair;

public class Integrator {
/*	public static void main(String[] args) throws Exception{
		Evaluator eval = new Evaluator();
		Scanner scan = new Scanner(System.in);
		System.out.print("Welcome to the Integrator!\nEnter your equation\nf=");
		String func = scan.nextLine();
		
		eval.parse(func);
		ArrayList<KeyPair> vars = eval.getKeys();
		ArrayList<Var> bounds = new ArrayList<KeyPair>();
		for(KeyPair var: varsNoBounds){
			vars.add(new Var(var));
			System.out.println("Enter the lower bound for variable " + var);
			vars.get(vars.size()-1).setLow(scan.nextDouble());
			System.out.println("Enter the upper bound for variable " + var);
			vars.get(vars.size()-1).setHigh(scan.nextDouble());
		}
		
		System.out.println("Enter the number of samples to take: ");
		int samples = scan.nextInt();
		double avgVal=0;
		for(int i = 0; i < samples; i++){
			for(Var var: vars){
				var.getKey().setVal(var.randInBound());
			}
			avgVal+=eval.evaluate(vars);
		}
		avgVal/=samples;
		System.out.println("The average function value was " + avgVal);
		double area = getArea(bounds);
		System.out.println("The area under the curve was " + (avgVal*area));
	}

	private static double getArea(ArrayList<KeyPair> bounds) {
		double area = 1;
		for(Var bound: bounds){
			area*=(bound.getHigh()-bound.getLow());
		}
		return area;
	}

	private static ArrayList<Double> getVals(ArrayList<Var> bounds) {
		ArrayList<Double> vals = new ArrayList<Double>();
		for(Var bound: bounds){
			vals.add(bound.randInBound());
		}
		return vals;
	}*/ //This console version is terrible and outdated, ignore it
}
