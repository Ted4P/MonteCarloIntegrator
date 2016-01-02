package functions;
import java.util.ArrayList;

import evaluator.Node;
import evaluator.Operand;

public class Mul extends Operand {

	public int getNumVals() {
		return 2;
	}
	
	public int getOrderOps(){return 3;}

	public double eval(ArrayList<Double> vals) {
		return vals.get(0)*vals.get(1);
	}

	public String toString(){return "*";}

	public Node derive(ArrayList<Node> children, Character key) 
	{
		return new Node(new Add(), new Node(new Mul(), children.get(0).derive(key), children.get(1)), new Node(new Mul(), children.get(0), children.get(1)).derive(key));
	}
}
