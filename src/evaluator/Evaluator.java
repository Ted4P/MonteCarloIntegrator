package evaluator;
import java.util.Map;
import java.util.TreeMap;


public class Evaluator {
	Node mainNode;
	public Map<String, Double> getKeys(){
		Map<String, Double> map = new TreeMap<String, Double>();
		mainNode.addKeys(map);
		return map;
	}
	public void parse(String function){
		 mainNode = new Node(Lexer.lex(function));
	}
	
	public double evaluate(Map<String, Double> keys) throws Exception{
		return mainNode.eval(keys);
	}
	public double evaluate() throws Exception{
		return mainNode.eval();
	}
}
