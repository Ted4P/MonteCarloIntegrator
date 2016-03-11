package evaluator;

public class MutableDouble {
	private double val;
	public MutableDouble(double center) {
		setVal(center);
	}
	public MutableDouble() {
		val=0;
	}
	public double getVal(){return val;}
	public void setVal(double newVal){ val = newVal;}
}
