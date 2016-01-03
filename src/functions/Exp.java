package functions;
import java.util.ArrayList;

import evaluator.Node;
import evaluator.Operand;
import evaluator.Number;

public class Exp extends Operand {

	public int getNumVals() {
		return 2;
	}
	
	public int getOrderOps(){return 2;}

	public double eval(ArrayList<Double> vals) {
		return Math.pow(vals.get(0), vals.get(1));
	}

	public String toString(){return "^";}

	@Override
	public Node derive(ArrayList<Node> children, Character key) {
		Node base = children.get(0);
		Node exp = children.get(1);
		if(base.hasVar(key)&&exp.hasVar(key)){ //Fancy X^X BS
			return null;
		}
		if(base.hasVar(key)){			//Power rule
			Node ndx = new Node(new Mul(), exp, base.derive(key));	//ndX
			Node nm	= new Node(new Sub(), exp, new Node(new Number(1),null,null));			//n-1
			Node xn	= new Node(new Exp(), base, nm);			//X^nm
			
			return new Node(new Mul(), ndx, xn);
		}
		try {
			Node ndx = new Node(new Mul(), exp.derive(key), new Node(new Number(Math.log(base.eval())), null,null));
			Node nx = new Node(new Exp(), base, exp);
			return new Node(new Mul(), ndx, nx);
		} catch (Exception e) {
			//Will never occur
			return null;
		}
	}

	@Override
	public void simplify(Node node) {
		// TODO Auto-generated method stub
		
	}

}