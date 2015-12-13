package evaluator;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import functions.Add;
import functions.Cosine;
import functions.Div;
import functions.Exp;
import functions.Mul;
import functions.Sine;
import functions.Sqrt;
import functions.Sub;
import functions.Tangent;
import functions.UnaryMinus;


public class Lexer {
	public static int pointer;
	public static ArrayList<Func> lex(String function) {
		//if(checkErrors(function)) throw new Exception(); TODO: Syntactic error checking
		function = removeSpaces(function);
		ArrayList<Func> lexed = new ArrayList<Func>();
		pointer = 0;
		while(pointer < function.length()){
			if(isNumber(function, pointer)){ 	
				lexed.add(getNumber(function));
			}
			else if(isParen(function, pointer)) lexed.add(getParen(function));
			else {
				Func posFunc = getFunc(function, lexed);
				if(posFunc!=null) lexed.add(posFunc);
				else{		//Add a var with the given name
					lexed.add(new Var("" + function.charAt(pointer)));
					pointer++;
				}
			}
		}
		handleImplcitMult(lexed);
		return lexed;
	}
	
	private static void handleImplcitMult(ArrayList<Func> lexed){
		int i = 0;
		while(i < lexed.size() -1){
			if(lexed.get(i).isANumber()){
				Func next = lexed.get(i+1);
				if(next.isANumber()) lexed.add(i+1, new Mul());
				else if(next.isAParen() && ((Paren)next).isOpening()) lexed.add(i+1, new Mul());
			}
			if(lexed.get(i) instanceof Paren && lexed.get(i+1) instanceof Paren){
				if(lexed.get(i) instanceof CloseParen && lexed.get(i+1) instanceof OpenParen) lexed.add(i+1, new Mul());	//If )(, add a *
			}
			i++;
		}
		Func last = lexed.get(lexed.size()-1);
		if(lexed.size() > 1 && (last instanceof Number || last instanceof Var)  && lexed.get(lexed.size()-2) instanceof Paren) lexed.add(lexed.size()-1, new Mul());	//Check if final characters look like this: )4 or )Y
	}
	
	private static String removeSpaces(String function) {	
		int i = 0;
		while(i<function.length()){
			if(function.charAt(i)==' ') function = function.substring(0, i) + function.substring(i+1);
			else i++;
		}
		return function;
	}

	private static Func getFunc(String function, ArrayList<Func> lexed) {		
		if(pointer < function.length() && function.charAt(pointer)=='-' && (lexed.size()==0 || !(lexed.get(lexed.size()-1)).isANumber())){ 
			pointer++; return new UnaryMinus();
		}
		
		ArrayList<Operand> functions = new ArrayList<Operand>();
		functions.add(new Add());
		functions.add(new Mul());
		functions.add(new Sub());
		functions.add(new Div());
		functions.add(new Exp());
		functions.add(new Sqrt());
		functions.add(new Sine());
		functions.add(new Cosine());
		functions.add(new Tangent());
		
		for(Operand op: functions){
			String name = op.toString();
			if(pointer + name.length() < function.length() && function.substring(pointer, pointer+ name.length()).equals(name)){
				pointer += name.length();
				return op;
			}
		}
		return null;
	}

	private static Func getParen(String function){
		Paren paren;
		if(function.charAt(pointer)=='(' || function.charAt(pointer)=='['){
			paren = new OpenParen();
		}
		else{ 
			paren = new CloseParen();
		}	
		pointer++;
		return paren;
	}
	
	private static Func getNumber(String function){
		String temp = "";
		
		while(pointer < function.length() && isNumber(function, pointer)){
			temp += function.charAt(pointer);
			pointer++;
		}
			
		double val = (new Double(temp)).doubleValue();
		return new Number(val);
	}
	
	
	private static boolean isNumber(String function, int target){
		char val = function.charAt(target);
		return (val == '.' ||val == '0' ||val == '1' ||val == '2' ||val == '3' ||val == '4' ||val == '5' ||val == '6' ||val == '7' ||val == '8' || val == '9');
	}
	
	private static boolean isParen(String function, int target) {
		char val = function.charAt(target);
		return val == '(' || val ==')' || val == '[' || val ==']';	
		
	}

}
