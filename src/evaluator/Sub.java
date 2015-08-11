package evaluator;
import java.util.ArrayList;

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
