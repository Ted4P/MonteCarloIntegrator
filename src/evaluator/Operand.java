package evaluator;
import java.util.ArrayList;
import java.util.Map;


public abstract class Operand implements Func{
	public boolean isAParen() {return false;}
	public boolean isANumber() {return false;}
	public boolean isAFunction() {return true;}
	public boolean isAVar(){return false;}
	
	public abstract int getNumVals();
	public abstract double eval(ArrayList<Double> vals);

}
