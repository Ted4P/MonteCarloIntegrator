package evaluator;

public interface Func {
	public boolean isAParen();
	public boolean isANumber();
	public boolean isAFunction();
	public boolean isAVar();
	public int getOrderOps();
}
