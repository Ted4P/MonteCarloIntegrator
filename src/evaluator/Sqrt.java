package evaluator;

import java.util.ArrayList;


public class Sqrt extends Operand {

	public int getNumVals() {
		return 1;
	}
	
	public int getOrderOps(){return 2;}

	public double eval(ArrayList<Double> vals) {
		return Math.sqrt(vals.get(0));
	}

	public String toString(){return "sqrt";}

}
