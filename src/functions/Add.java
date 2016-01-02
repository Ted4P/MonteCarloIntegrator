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
	public Node derive(ArrayList<Node> children, Character key) {
		return new Node(new Add(), children.get(0).derive(key), children.get(1).derive(key));
	}

}
