package evaluator;

public abstract class Paren implements Func{
	public boolean isAParen() {return true;}
	public boolean isANumber() {return false;}
	public boolean isAFunction() {return false;}
	public boolean isAVar(){return false;}
	
	public int getOrderOps(){return 1;}
	public abstract boolean isOpening(); 
}
