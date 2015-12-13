package evaluator;
import java.util.ArrayList;
import java.util.Map;

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
	
	public double eval(Map<String, Double> keys) throws Exception{
		if(mainFunc instanceof Var){
			String key = ((Var)mainFunc).getKey();
			if(!keys.containsKey(key)) throw new Exception();
			
			ArrayList<Double> val = new ArrayList<Double>();
			val.add(keys.get(key));
			return mainFunc.eval(val);
		}
		if(mainFunc instanceof Number) return mainFunc.eval(null);
		
		ArrayList<Double> subEval = new ArrayList<Double>();
		for(Node child: children){
			subEval.add(child.eval(keys));
		}
		return mainFunc.eval(subEval);
	}


	public void addKeys(Map<String, Double> map) {
		if(children == null || children.size() == 0){
			if(mainFunc instanceof Var){
				map.put(((Var)mainFunc).getKey(), null);
			}
			return;
		}
		for(Node child: children){
			child.addKeys(map);
		}
		return;
	}
}