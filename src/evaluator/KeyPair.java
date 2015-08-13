package evaluator;

public class KeyPair {
	private String key;
	private double val;
	public KeyPair(String key){
		this.key = key;
	}
	public void setVal(double val){
		this.val = val;
	}
	
	public String getKey(){return key;}
	public double getVal(){return val;}
}
