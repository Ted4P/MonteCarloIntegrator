package evaluator;
import java.util.ArrayList;


public class Number extends Operand{
	private double val;
	
	public Number(double val){
		this.val = val;
	}
	
	public boolean isAParen() {return false;}
	public boolean isANumber() {return true;}
	public boolean isAFunction() {return true;}

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
