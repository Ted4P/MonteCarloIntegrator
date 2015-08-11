package evaluator;
import java.util.ArrayList;

public class Div extends Operand {

	public int getNumVals() {
		return 2;
	}
	
	public int getOrderOps(){return 3;}

	public double eval(ArrayList<Double> vals) {
		return vals.get(0)/vals.get(1);
	}

	public String toString(){return "/";}

}