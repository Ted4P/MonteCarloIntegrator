package integrator;

import org.scilab.forge.jlatexmath.*;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

import evaluator.Evaluator;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;



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
	private JLabel integral;
	private String areaStr;
	
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
		
		samples.setText("500000");
		initText();
		
		add(entireGUI);
		pack();
		this.setContentPane(entireGUI);
		setVisible(true);
		
	}
	
	
	private void calculate () throws Exception{
		eq = equation.getText();
		lb = Double.parseDouble(lbound.getText());
		ub = Double.parseDouble(ubound.getText());
		sam = Integer.parseInt(samples.getText());
		
		eval.parse(eq);
		
		Map<String, Double> varSet = eval.getKeys();
		Set<String> vars = varSet.keySet();
		
		if(vars.size()!=1){ System.out.println("BAD NUMBER OF VARIABLES!"); return;}
		String key = vars.iterator().next();
		
		double[][] vals = new double[sam][2];
		for(int i = 0; i < vals.length; i++){ 
			vals[i][0] = randInBound();
			varSet.put(key, vals[i][0]); 
			vals[i][1] = eval.evaluate(varSet);
		}
		sortArray(vals);
		
		for(double[] val: vals){
			System.out.println(val[0] + ", " + val[1]);
		}
		double sum = 0;
		for(int i = 0; i < vals.length-1; i++){			//Trapezoid sum
			double height = (vals[i][1]+vals[i+1][1])/2;
			double width = vals[i+1][0]-vals[i][0];
			sum+=height*width;
		}
		
		DecimalFormat df = new DecimalFormat("####0.00000");
		areaStr = df.format(sum);
		
		avgval.setText((1/(ub-lb))*sum+"");
		area.setText(sum+"");

		latexRender();
		
		pack();
		
	}
	
	private void sortArray(double[][] vals) {
		int size = vals.length;
        if (size < 2)
            return;
        int mid = size / 2;
        int leftSize = mid;
        int rightSize = size - mid;
        double[][] left = new double[leftSize][];
        double[][] right = new double[rightSize][];
        for (int i = 0; i < mid; i++) {
            left[i] = vals[i];

        }
        for (int i = mid; i < size; i++) {
            right[i - mid] = vals[i];
        }
        sortArray(left);
        sortArray(right);
        merge(left, right, vals);
    }

    public static void merge(double[][] left, double[][] right, double[][] vals) {
        int leftSize = left.length;
        int rightSize = right.length;
        int i = 0, j = 0, k = 0;
        while (i < leftSize && j < rightSize) {
            if (left[i][0] <= right[j][0]) {
                vals[k] = left[i];
                i++;
                k++;
            } else {
                vals[k] = right[j];
                k++;
                j++;
            }
        }
        while (i < leftSize) {
            vals[k] = left[i];
            k++;
            i++;
        }
        while (j < leftSize) {
            vals[k] = right[j];
            k++;
            j++;
        }
    }


	private double randInBound(){
		return lb + ((ub-lb)*Math.random());
	}
	
	
	private void latexRender(){
		
		String form = "\\int_{" + lb + "}^{" + ub + "}" + eq + "\\, dx \\approx" + areaStr;
		
		TeXFormula formula = new TeXFormula (form);
		
		TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
				
		BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(new JLabel(), image.getGraphics(), 0, 0);
		
		
		integral.setIcon(icon);
		
	}
	
	
	private void initText(){
		//top title
		final JLabel TITLE = new JLabel("Monte Carlo Integrator", SwingConstants.CENTER);
		c.ipady=15;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=3;
		c.weightx=1.0;
		c.weighty=0.1;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(TITLE,c);
		
		//latex equation
		integral = new JLabel("",SwingConstants.CENTER);
		c.ipady=0;
		c.gridwidth=2;
		c.gridx=0;
		c.gridy=1;
		c.weightx=0.6;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(integral,c);
		
		//input data labels
		final JLabel EQUATION = new JLabel("Equation: ",SwingConstants.RIGHT);
		c.ipadx=15;
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
		
		final JLabel SAMPLES = new JLabel("# of Samples: ",SwingConstants.RIGHT);
		c.ipady=40;
		c.gridx=0;
		c.gridy=5;
		c.weightx=0.3;
		c.weighty=0.2;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(SAMPLES,c);	
		
		//input text fields
		c.ipadx=0;
		c.ipady=0;
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
				try {
					calculate();
				} catch (Exception e1) {
					System.out.println("Parse failed!");
				}
			}
		});
		c.ipadx=50;
		c.gridx=2;
		c.gridy=1;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(perform,c);
		
		final JLabel AVGVAL = new JLabel("Average Value",SwingConstants.CENTER);
		c.gridx=2;
		c.gridy=2;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(AVGVAL,c);
		
		avgval = new JLabel("",SwingConstants.CENTER);
		c.gridx=2;
		c.gridy=3;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(avgval,c);
		
		
		final JLabel AREA = new JLabel("Area Under Curve",SwingConstants.CENTER);
		c.gridx=2;
		c.gridy=4;
		c.weightx=0.4;
		c.weighty=0.3;
		c.fill=GridBagConstraints.HORIZONTAL;
		entireGUI.add(AREA,c);
		
		area = new JLabel("",SwingConstants.CENTER);
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
}




