package evaluator;
import java.util.ArrayList;
import java.util.Map;

public class Node {
	private Operand mainFunc;
	private ArrayList<Node> children;
	
	public Node(ArrayList<Func> function){
	while(stripLeadTail(function));
	if(function.size()==1){
		mainFunc = (Operand) function.get(0);
	}
	else{
			children = new ArrayList<Node>();
			int index = findHighestOp(function);
			mainFunc = (Operand)function.get(index);
			if(mainFunc.getNumVals()==1) {
				children.add(new Node(subList(index + 1, function.size(), function)));
			}
			else if (mainFunc.getNumVals()==2) {
				children.add(new Node(subList(0, index, function)));
				children.add(new Node(subList(index + 1, function.size(), function)));
			}
	}
	}

	private static ArrayList<Func> subList(int start, int end, ArrayList<Func> function) {
		
		ArrayList<Func> subList = new ArrayList<Func>();
		for(int i = start; i < end; i++){
			subList.add(function.get(i));
		}
		return subList;
	}

	private boolean stripLeadTail(ArrayList<Func> function) {
		int currParenLevel = 1;
		if(!(function.get(0) instanceof Paren && function.get(function.size() - 1) instanceof Paren)) return false; 	//If the func does not start+end with ( and ), exit
		for(int i = 1; i < function.size() - 1; i++){	//If the paren depth is ever ==0 before the end, exit
			if(function.get(i) instanceof Paren){
				if(function.get(i) instanceof OpenParen) currParenLevel++;
				else currParenLevel--;
			}
			if(currParenLevel==0) return false;
		}
		function.remove(function.size()-1);
		function.remove(0);
		return true;
	}

	private int findHighestOp(ArrayList<Func> function) {
		int highestOp = -1;
		int index = -1;
		int parenNum=0;
		for(int i = 0; i < function.size(); i++){
			Func temp = function.get(i);
			if(temp instanceof Paren){
				if(temp instanceof OpenParen) parenNum++;
				else parenNum--;
			}
			if(parenNum == 0 && temp.getOrderOps()>highestOp){
				highestOp = temp.getOrderOps();
				index = i;
			}
		}
		return index;
	}
	
	public double eval() throws Exception {
		return eval(null);
	}
	
	public double eval(Map<String, MutableDouble> varSet) throws Exception{
		if(mainFunc instanceof Var){
			return ((Var)mainFunc).eval(varSet);
		}
		if(mainFunc instanceof Number) return mainFunc.eval(null);
		
		ArrayList<Double> subEval = new ArrayList<Double>();
		for(Node child: children){
			subEval.add(child.eval(varSet));
		}
		return mainFunc.eval(subEval);
	}


	public void addKeys(Map<String, MutableDouble> map) {
		if(children == null || children.size() == 0){
			if(mainFunc instanceof Var){
				map.put(((Var)mainFunc).getKey(), new MutableDouble());
			}
			return;
		}
		for(Node child: children){
			child.addKeys(map);
		}
		return;
	}

	public void simplify() {			//Attempt to solve as much of the equation tree as possible without variables
		if(mainFunc instanceof Var || mainFunc instanceof Number) return;
		
		for(Node child: children) child.simplify();
		ArrayList<Double> vals = new ArrayList<Double>();
		for(Node child: children){
			try{
				vals.add(child.eval());
			}
			catch (Exception ex){
				return;
			}
		}
		mainFunc = new Number(mainFunc.eval(vals));			//Replace the function with a constant, and remove children
		children.clear();
	}
	
	public String toString(){
		if(children==null || children.size()==0) return mainFunc.toString();
		if(children.size()==1) return mainFunc.toString() + "(" + children.get(0) +")";
		return "(" + children.get(0) +")"+ mainFunc.toString() + "(" + children.get(1) +")";
	}
}