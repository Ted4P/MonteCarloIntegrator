package integrator;

public class Var {
	private String name;
	private double low, high;
	public Var(String name, double low, double high){
		this.name = name;
		this.low = low;
		this.high = high;
	}
	
	public String getName(){return name;}
	
	public double randInBound(){
		return low + ((high - low) * Math.random());
	}
	
	public double getLow(){return low;}
	public double getHigh(){return high;}
}
