package functions;
import java.util.ArrayList;

import evaluator.Operand;

public class Sub extends Operand {

	public int getNumVals() {
		return 2;
	}
	
	public int getOrderOps(){return 5;}

	public double eval(ArrayList<Double> vals) {
		return vals.get(0)-vals.get(1);
	}

	public String toString(){return "-";}

}
