package evaluator;
import java.util.ArrayList;

public class UnaryMinus extends Operand {

	public int getNumVals() {
		return 1;
	}
	
	public int getOrderOps(){return 5;}

	public double eval(ArrayList<Double> vals) {
		return -1 * vals.get(0);
	}

	public String toString(){return "-";}

}