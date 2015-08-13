package evaluator;
import java.util.ArrayList;

public class Cosine extends Operand {

	public int getNumVals() {
		return 1;
	}
	
	public int getOrderOps(){return 2;}

	public double eval(ArrayList<Double> vals) {
		return Math.cos(vals.get(0));
	}

	public String toString(){return "cos";}

}