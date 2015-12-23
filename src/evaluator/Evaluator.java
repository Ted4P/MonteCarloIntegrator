package evaluator;
import java.util.Map;
import java.util.TreeMap;


public class Evaluator {
	Node mainNode;
	public Map<String, MutableDouble> getKeys(){
		Map<String, MutableDouble> map = new TreeMap<String, MutableDouble>();
		mainNode.addKeys(map);
		return map;
	}
	public void parse(String function){
		 mainNode = new Node(Lexer.lex(function));
		 mainNode.simplify();
	}
	
	public double evaluate(Map<String, MutableDouble> varSet) throws Exception{
		return mainNode.eval(varSet);
	}
	public double evaluate() throws Exception{
		return mainNode.eval();
	}
	
	public String toString(){
		return mainNode.toString();
	}
}