package functions;
import java.util.ArrayList;

import evaluator.Node;
import evaluator.Operand;
import evaluator.Number;

public class Div extends Operand {

	public int getNumVals() {
		return 2;
	}
	
	public int getOrderOps(){return 3;}

	public double eval(ArrayList<Double> vals) {
		return vals.get(0)/vals.get(1);
	}

	public String toString(){return "/";}

	public Node derive(ArrayList<Node> children, Character key)
	{
		return new Node(new Div(), new Node(new Sub(), new Node(new Mul(), children.get(0).derive(key), children.get(1)), new Node(new Mul(), children.get(0),
				children.get(1).derive(key))), new Node(new Exp(), children.get(1), new Node(new Number(2))));
		//Quotient rule: (f'(x)*g(x)-f(x)g'(x)) / ((g(x))^2)
	}

	public void simplify(Node node) {
		try {
			double val = node.getChildren().get(0).eval();
			if(Math.abs(val) < DOUBLE_TOL) node.replaceWithNum(0);
		} catch (Exception e) {
		}
		
	}
}