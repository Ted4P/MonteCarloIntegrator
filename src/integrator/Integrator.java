package integrator;
import java.util.ArrayList;
import java.util.Scanner;

import evaluator.Evaluator;
public class Integrator {
	public static void main(String[] args) throws Exception{
		Evaluator eval = new Evaluator();
		Scanner scan = new Scanner(System.in);
		System.out.print("Welcome to the Integrator!\nEnter your equation\nf=");
		String func = scan.nextLine();
		
		eval.parse(func);
		ArrayList<String> vars = eval.getKeys();
		ArrayList<Var> bounds = new ArrayList<Var>();
		for(String var: vars){
			System.out.println("Enter the lower bound for variable " + var);
			double low = scan.nextDouble();
			System.out.println("Enter the upper bound for variable " + var);
			double high = scan.nextDouble();
			bounds.add(new Var(var, low, high));
		}
		
		System.out.println("Enter the number of samples to take: ");
		int samples = scan.nextInt();
		double avgVal=0;
		for(int i = 0; i < samples; i++){
			avgVal+=eval.evaluate(vars, getVals(bounds));
		}
		avgVal/=samples;
		System.out.println("The average function value was " + avgVal);
		double area = getArea(bounds);
		System.out.println("The area under the curve was " + (avgVal*area));
	}

	private static double getArea(ArrayList<Var> bounds) {
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
	}
}
