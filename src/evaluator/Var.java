package evaluator;
import java.util.ArrayList;
import java.util.Map;


public class Var extends Operand{
	private String key;
	
	public Var(String key){
		this.key = key;
	}
	
	public boolean isAParen() {return false;}
	public boolean isANumber() {return true;}
	public boolean isAFunction() {return true;}
	public boolean isAVar(){return true;}

	public String toString(){return key;}

	public int getNumVals() {
		return 1;
	}
	
	public String getKey(){return key;}

	public double eval(ArrayList<Double> vals) {
		return vals.get(0);
	}
	
	public double eval(Map<String, MutableDouble> vals){
		return vals.get(key).getVal();
	}

	public int getOrderOps() {
		return 0;
	}
}
