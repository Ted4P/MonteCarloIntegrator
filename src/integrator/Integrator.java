package integrator;
import java.util.ArrayList;
import java.util.Scanner;

import evaluator.Evaluator;
import evaluator.KeyPair;
import evaluator.Var;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Integrator extends javax.swing.JFrame {
	
	private Evaluator eval;
	
	//encloses everything
	private JPanel entireGUI;
	
	JTextField equation = new JTextField(10);
	JTextField lbound = new JTextField(10);
	JTextField ubound = new JTextField(10);
	JTextField samples = new JTextField(10);
	
	//results
	private JLabel avgval;
	private JLabel area;
	
	//variables to store text field data
	private String eq; //equation
	private double lb; //lower bound
	private double ub; //upper bound
	private int sam; //samples
	
	GridBagConstraints c;
	
	
	
	
	public Integrator() {
		entireGUI = new JPanel();
		entireGUI.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		eval = new Evaluator();
		
		
		initText();
		
		
		
		add(entireGUI);
		pack();
		this.setContentPane(entireGUI);
		setVisible(true);
		
	}
	
	
	private void calculate (){
		eq = equation.getText();
		lb = Double.parseDouble(lbound.getText());
		ub = Double.parseDouble(ubound.getText());
		sam = Integer.parseInt(samples.getText());
		
		eval.parse(eq);
		
		
		
		
		
	}
	
	
	
	private void initText(){
		//top title
		final JLabel TITLE = new JLabel("Monte Carlo Integrator", SwingConstants.CENTER);
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=3;
		c.weightx=1.0;
		c.weighty=0.1;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(TITLE,c);
		
		
		/** REMOVED FOR NOW TO SEE IF JLATEXMATH WILL WORK
		//integral symbol
		JLabel intsymbol = new JLabel();
		intsymbol.setIcon(new ImageIcon(getClass().getResource("image/int.png")));
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=6;
		c.gridheight=3;
		c.fill=GridBagConstraints.NONE;
		entireGUI.add(intsymbol,c);
		*/ 
		
		//latex equation
		JLabel integral = new JLabel("WHERE THE LATEX WILL GO");
		c.gridwidth=2;
		c.gridx=0;
		c.gridy=1;
		c.weightx=0.6;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(integral,c);
		
		
		//input data labels
		final JLabel EQUATION = new JLabel("Equation: ",SwingConstants.RIGHT);
		c.gridwidth=1;
		c.gridx=0;
		c.gridy=2;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(EQUATION,c);
		
		final JLabel LBOUND = new JLabel("Lower Bound: ",SwingConstants.RIGHT);
		c.gridx=0;
		c.gridy=3;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(LBOUND,c);
		
		final JLabel UBOUND = new JLabel("Upper Bound: ",SwingConstants.RIGHT);
		c.gridx=0;
		c.gridy=4;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(UBOUND,c);
		
		final JLabel SAMPLES = new JLabel("Samples: ",SwingConstants.RIGHT);
		c.gridx=0;
		c.gridy=5;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(SAMPLES,c);
		
		
		//input text fields
		
		c.gridx=1;
		c.gridy=2;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(equation,c);
		
		c.gridx=1;
		c.gridy=3;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(lbound,c);
		
		c.gridx=1;
		c.gridy=4;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(ubound,c);
		
		c.gridx=1;
		c.gridy=5;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(samples,c);
		
		
		
		//calculation side
		final JButton perform = new JButton("INTEGRATE!");
		perform.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				calculate();
			}
		});
		c.gridx=2;
		c.gridy=1;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(perform,c);
		
		final JLabel AVGVAL = new JLabel("Average Value");
		c.gridx=2;
		c.gridy=2;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(AVGVAL,c);
		
		avgval = new JLabel("");
		c.gridx=2;
		c.gridy=3;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(avgval,c);
		
		
		final JLabel AREA = new JLabel("Area Under Curve");
		c.gridx=2;
		c.gridy=4;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(AREA,c);
		
		area = new JLabel("");
		c.gridx=2;
		c.gridy=5;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(area,c);
	}
	


	public static void main(String[] args){
		new Integrator();
		
	}
	
	
	
/*	public static void main(String[] args) throws Exception{
		Evaluator eval = new Evaluator();
		Scanner scan = new Scanner(System.in);
		System.out.print("Welcome to the Integrator!\nEnter your equation\nf=");
		String func = scan.nextLine();
		
		eval.parse(func);
		ArrayList<KeyPair> vars = eval.getKeys();
		ArrayList<Var> bounds = new ArrayList<KeyPair>();
		for(KeyPair var: varsNoBounds){
			vars.add(new Var(var));
			System.out.println("Enter the lower bound for variable " + var);
			vars.get(vars.size()-1).setLow(scan.nextDouble());
			System.out.println("Enter the upper bound for variable " + var);
			vars.get(vars.size()-1).setHigh(scan.nextDouble());
		}
		
		System.out.println("Enter the number of samples to take: ");
		int samples = scan.nextInt();
		double avgVal=0;
		for(int i = 0; i < samples; i++){
			for(Var var: vars){
				var.getKey().setVal(var.randInBound());
			}
			avgVal+=eval.evaluate(vars);
		}
		avgVal/=samples;
		System.out.println("The average function value was " + avgVal);
		double area = getArea(bounds);
		System.out.println("The area under the curve was " + (avgVal*area));
	}

	private static double getArea(ArrayList<KeyPair> bounds) {
		double area = 1;
		for(Var bound: bounds){
			area*=(bound.getHigh()-bound.getLow());
		}
		return area;
	}

	private static ArrayList<Double> getVals(ArrayList<Var> bounds) {
		ArrayList<Double> vals = new ArrayList<Double>();
		for(Var bound: bounds){
			vals.add(bound.randInBound());
		}
		return vals;
	}*/ //This console version is terrible and outdated, ignore it
}




