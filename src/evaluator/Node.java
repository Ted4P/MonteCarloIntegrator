package evaluator;
import java.util.ArrayList;

public class Node {
	private Operand mainFunc;
	private ArrayList<Node> children;
	
	public Node(ArrayList<Func> function){
	while(stripLeadTail(function));
	if(function.size()==1 && function.get(0).isANumber()){
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
		if(!(function.get(0).isAParen()&& function.get(function.size() - 1).isAParen())) return false; 	//If the func does not start+end with ( and ), exit
		for(int i = 1; i < function.size() - 1; i++){	//If the paren depth is ever ==0 before the end, exit
			if(function.get(i).isAParen()){
				if(((Paren)function.get(i)).isOpening()) currParenLevel++;
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
			if(temp.isAParen()){
				if(((Paren) temp).isOpening()) parenNum++;
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
	
	public double eval(ArrayList<KeyPair> keys) throws Exception{
		if(mainFunc instanceof Var){
			String thisKey = ((Var)mainFunc).getKey();
			if(keys==null) throw new Exception();
			for(KeyPair key: keys){
				if(key.getKey().equals(thisKey)){
					ArrayList<Double> val = new ArrayList<Double>();
					val.add(key.getVal());
					 return mainFunc.eval(val);
				}
			}
			throw new Exception();
		}
		if(mainFunc instanceof Number) return mainFunc.eval(null);
		
		ArrayList<Double> subEval = new ArrayList<Double>();
		for(Node child: children){
			subEval.add(child.eval(keys));
		}
		return mainFunc.eval(subEval);
	}


	public ArrayList<KeyPair> getKeys() {
		if(children == null || children.size() == 0){
			ArrayList<KeyPair> keys = new ArrayList<KeyPair>();
			if(mainFunc.isAVar()){
				keys.add(new KeyPair(((Var) mainFunc).getKey()));
			}
			return keys;
		}
		if(children.size()==1){
			return children.get(0).getKeys();
		}
		ArrayList<KeyPair> child1 = children.get(0).getKeys();
		ArrayList<KeyPair> child2 = children.get(1).getKeys();
		int i = 0;
		while(i < child1.size()){	//Delete duplicate keys
			int j = 0;
			while(j < child2.size()){
				if(child1.get(i).getKey().equals(child2.get(j).getKey()))
					child2.remove(j);
				else
					j++;
			}
			i++;
		}
		for(KeyPair key: child2){	//Add the remaining second list on to the first
			child1.add(key);
		}
		return child1;
	}
}