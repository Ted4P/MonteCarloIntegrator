package evaluator;
import java.util.ArrayList;

public class Sine extends Operand {

	public int getNumVals() {
		return 1;
	}
	
	public int getOrderOps(){return 2;}

	public double eval(ArrayList<Double> vals) {
		return Math.sin(vals.get(0));
	}

	public String toString(){return "sin";}

}
