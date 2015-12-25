package evaluator;
import java.util.ArrayList;


public class Number extends Operand{
	private double val;
	
	public Number(double val){
		this.val = val;
	}

	public String toString(){return ""+val;}

	public int getNumVals() {
		return 0;
	}

	public double eval(ArrayList<Double> vals) {
		return val;
	}

	public int getOrderOps() {
		return 0;
	}
}
