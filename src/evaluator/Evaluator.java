package evaluator;
import java.util.ArrayList;
import java.util.Scanner;


public class Evaluator {
	Node mainNode;
	public static void main(String[] args) throws Exception{
		Scanner scan = new Scanner(System.in);
		Evaluator eval =  new Evaluator();
		double ans;
		
		System.out.println("Welcome to the variable-function evaluator!");
		System.out.print("Please enter you function: f=");
		eval.parse(scan.nextLine());
		ArrayList<String> keys = eval.getKeys();
		if(keys!=null && keys.size()!=0){
			ArrayList<Double> vals = new ArrayList<Double>();
			for(String key: keys){
				System.out.println("Enter the value for variable " + key);
				vals.add(scan.nextDouble());
			}
			ans = eval.evaluate(keys, vals);
		}
		else ans = eval.evaluate();
		System.out.println("The function evaluates to: " + ans);
	}
	
	public ArrayList<String> getKeys(){
		return mainNode.getKeys();
	}
	public void parse(String function){
		 mainNode = new Node(Lexer.lex(function));
	}
	
	public double evaluate(ArrayList<String> keys, ArrayList<Double> vals) throws Exception{
		return mainNode.eval(keys, vals);
	}
	public double evaluate() throws Exception{
		return mainNode.eval();
	}
}
