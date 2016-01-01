package evaluator;
import java.util.ArrayList;


public abstract class Operand implements Func{
	public abstract int getNumVals();
	public abstract double eval(ArrayList<Double> vals);
	public abstract Node derive(ArrayList<Node> children, Character key);
}
