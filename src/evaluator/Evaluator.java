package evaluator;
import java.util.HashMap;
import java.util.Map;

import functions.*;


public class Evaluator {
	Node mainNode;
	public Map<Character, MutableDouble> getKeys(){
		Map<Character, MutableDouble> map = new HashMap<Character, MutableDouble>();
		mainNode.addKeys(map);
		return map;
	}
	public void parse(String function){
		 mainNode = new Node(Lexer.lex(function));
		 mainNode.simplify();
	}
	
	public double evaluate(Map<Character, MutableDouble> varSet) throws Exception{
		return mainNode.eval(varSet);
	}
	public double evaluate() throws Exception{
		return mainNode.eval();
	}
	
	public String toString(){
		return mainNode.toString();
	}
	
	public void derive(Character key){
		mainNode = mainNode.derive(key);
		mainNode.simplify();
	}
	
	public void taylorSeries(int degree, double center, Character key) throws Exception{
		double[] derivatives = new double[degree];
		
		Map<Character, MutableDouble> set = new HashMap<Character, MutableDouble>();		//To evaluate derivatives
		set.put(key, new MutableDouble(center));
		
		for(int i = 1; i < degree; i++){
			mainNode = mainNode.derive(key);
			mainNode.simplify();
			derivatives[i] = mainNode.eval(set);
		}
		Node TS = new Node(new Number(mainNode.eval(set)));
		
		for(int i = 1; i < degree; i++){
			Node child = new Node(new Exp(), new Node(new Sub(), new Node(new Var(key)), new Node(new Number(center))), new Node(new Number(i)));
			child = new Node(new Mul(), child, new Node(new Number(derivatives[i])));
			child = new Node(new Div(), child, new Node(new Number(factorial(i))));
			TS = new Node(new Add(), TS, child);
		}
		mainNode = TS;
		mainNode.simplify();
	}
	
	private double factorial(int i) {
		if(i==0||i==1) return 1;
		double val = 1;
		while(i>1){val*=i--;}
		return val;
	}
	public static void main(String[] args) throws Exception{
		Evaluator eval = new Evaluator();
		eval.parse("e^(X)");
		eval.taylorSeries(8,0,'X');
		System.out.println(eval.toString());
		
		/*eval.parse("sin(X)*-1");
		System.out.println(eval);
		Map<Character, MutableDouble> set = new HashMap<Character, MutableDouble>();		//To evaluate derivatives
		set.put('X', new MutableDouble());
		System.out.println(eval.evaluate(set));*/
	}
}