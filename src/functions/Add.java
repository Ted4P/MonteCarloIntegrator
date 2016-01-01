package functions;
import java.util.ArrayList;

import evaluator.*;


public class Add extends Operand {

	public int getNumVals() {
		return 2;
	}
	
	public int getOrderOps(){return 5;}

	public double eval(ArrayList<Double> vals) {
		return vals.get(0)+vals.get(1);
	}

	public String toString(){return "+";}

	@Override
	public ArrayList<Func> derive(ArrayList<Node> children, Character key) {
		ArrayList<Func> temp = new ArrayList<Func>();
		
		temp.add(new OpenParen());
		temp.addAll(children.get(0).derive(key));
		temp.add(new CloseParen());
		temp.add(new Add());
		temp.add(new OpenParen());
		temp.addAll(children.get(1).derive(key));
		temp.add(new CloseParen());
		
		return temp;
	}

}
