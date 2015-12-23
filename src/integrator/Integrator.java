package integrator;

import org.scilab.forge.jlatexmath.*;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.ButtonGroup;

import evaluator.Evaluator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Integrator extends JFrame 
{
	private Evaluator eval;
	
	private JPanel entireGUI, enterPanel, optionsPanel, integralPanel;
	private Box buttonBox, valsBox, verticalBox, resultBox;
	private JTextField integrand, lbound, ubound, samples;
	private JRadioButton right, left, mid, trap;
	
	private JButton integrate, restart, help;
	
	//results
	private JLabel avgval, area, integral;
	private String areaStr;
	
	//variables to store text field data
	private String eq; //equation
	private double lb, ub; //lower and upper bounds
	private int sam; //samples	
	
	public Integrator() 
	{
		super("Monte Carlo Integrator");
		
		eval = new Evaluator();
		entireGUI = new JPanel();
		
		integrand = new JTextField(10);
		JPanel panel1 = new JPanel();
		panel1.add(new JLabel("      Integrand"));
		panel1.add(integrand);

		lbound = new JTextField(10);
		JPanel panel2 = new JPanel();
		panel2.add(new JLabel("Lower Bound"));
		panel2.add(lbound);
		
		ubound = new JTextField(10);
		JPanel panel3 = new JPanel();
		panel3.add(new JLabel("Upper Bound"));
		panel3.add(ubound);
		
		samples = new JTextField("500000", 10);
		JPanel panel4 = new JPanel();
		panel4.add(new JLabel("# of Samples"));
		panel4.add(samples);
		
		valsBox = Box.createVerticalBox();
		valsBox.add(panel1);
		valsBox.add(panel2);
		valsBox.add(panel3);
		valsBox.add(panel4);
		
		right = new JRadioButton("Right");
		left = new JRadioButton("Left");
		mid = new JRadioButton("Midpoint");
		trap = new JRadioButton("Trapezoid", true);
		ButtonGroup group = new ButtonGroup();
		group.add(left);
		group.add(right);
		group.add(mid);
		group.add(trap);
		buttonBox = Box.createVerticalBox();
		buttonBox.add(left);
		buttonBox.add(right);
		buttonBox.add(mid);
		buttonBox.add(trap);
		
		enterPanel = new JPanel();
		enterPanel.add(valsBox);
		enterPanel.add(buttonBox);
		
		help = new JButton("?");
		help.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						JOptionPane.showMessageDialog(entireGUI, "Help message goes here.", "Help", JOptionPane.QUESTION_MESSAGE);
					}
				});
		integrate = new JButton("Integrate");
		integrate.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						try 
						{
							calculate();
						} 
						catch (Exception e1)
						{
							JOptionPane.showMessageDialog(entireGUI, "Parse failed.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
		restart = new JButton("Clear");
		restart.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						integrand.setText("");
						lbound.setText("");
						ubound.setText("");
						samples.setText("500000");
						integral.setIcon(null);
						avgval.setText(" ");
						area.setText(" ");
						entireGUI.setVisible(true);
					}
				});
		
		optionsPanel = new JPanel();
		optionsPanel.add(help);
		optionsPanel.add(integrate);
		optionsPanel.add(restart);
		
		//Initializes the variables so the window will be a set size.
		eq = "";
		lb = 0;
		ub = 0;
		areaStr = "";
		
		integral = new JLabel();
		latexRender();
		integralPanel = new JPanel();
		integralPanel.add(integral);
		
		avgval = new JLabel(" ");
		area = new JLabel(" ");
		resultBox = Box.createVerticalBox();
		resultBox.setAlignmentX(RIGHT_ALIGNMENT);
		resultBox.add(new JLabel("Average Value:"));
		resultBox.add(avgval);
		resultBox.add(new JLabel("Area Under Graph:"));
		resultBox.add(area);
		
		verticalBox = Box.createVerticalBox();
		verticalBox.add(enterPanel);
		verticalBox.add(optionsPanel);
		verticalBox.add(resultBox);
		verticalBox.add(integralPanel);
		
		entireGUI.add(verticalBox);
		add(entireGUI);
		pack();
		integral.setVisible(false);
		entireGUI.setVisible(true);
		this.setContentPane(entireGUI);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void calculate() throws Exception
	{
		eq = integrand.getText();
		lb = Double.parseDouble(lbound.getText());
		ub = Double.parseDouble(ubound.getText());
		sam = Integer.parseInt(samples.getText());
		
		eval.parse(eq);
		
		Map<String, Double> varSet = eval.getKeys();
		Set<String> vars = varSet.keySet();
		
		if(vars.size()!=1)
		{
			JOptionPane.showMessageDialog(entireGUI, "Wrong number of variables", "Error", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		String key = vars.iterator().next();
		
		double[][] vals = new double[sam][2];
		for(int i = 0; i < vals.length; i++){ 
			vals[i][0] = randInBound();
			varSet.put(key, vals[i][0]); 
			vals[i][1] = eval.evaluate(varSet);
		}
		
		MergeSort sorter = new MergeSort();
		sorter.sort(vals);
		
		double sum = 0;
		if(trap.isSelected())
		{
			for(int i = 0; i < vals.length-1; i++){		//Trapezoid sum
				double height = (vals[i][1]+vals[i+1][1])/2;
				double width = vals[i+1][0]-vals[i][0];
				sum+=height*width;
			}
		}
		else if(mid.isSelected())
		{
			
		}
		else if(left.isSelected())
		{
			for(int i = 0; i < vals.length-1; i++){		//Left sum
				double height = vals[i][1];
				double width = vals[i+1][0]-vals[i][0];
				sum+=height*width;
			}
		}
		else if(right.isSelected())
		{
			for(int i = 1; i < vals.length; i++){		//Right sum
				double height = vals[i][1];
				double width = vals[i][0]-vals[i-1][0];
				sum+=height*width;
			}
		}
		
		DecimalFormat df = new DecimalFormat("####0.00000");
		areaStr = df.format(sum);
		
		avgval.setText((1/(ub-lb))*sum+"");
		area.setText(sum+"");

		latexRender();
	}
	
	private class MergeSort {
	     
	    private double[][] array;
	    private double[][] tempMergArr;
	    private int length;
	     
	    public void sort(double inputArr[][]) {
	        this.array = inputArr;
	        this.length = inputArr.length;
	        this.tempMergArr = new double[length][2];
	        doMergeSort(0, length - 1);
	    }
	 
	    private void doMergeSort(int lowerIndex, int higherIndex) {
	         
	        if (lowerIndex < higherIndex) {
	            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
	            // Below step sorts the left side of the array
	            doMergeSort(lowerIndex, middle);
	            // Below step sorts the right side of the array
	            doMergeSort(middle + 1, higherIndex);
	            // Now merge both sides
	            mergeParts(lowerIndex, middle, higherIndex);
	        }
	    }
	 
	    private void mergeParts(int lowerIndex, int middle, int higherIndex) {
	 
	        for (int i = lowerIndex; i <= higherIndex; i++) {
	            tempMergArr[i] = array[i];
	        }
	        int i = lowerIndex;
	        int j = middle + 1;
	        int k = lowerIndex;
	        while (i <= middle && j <= higherIndex) {
	            if (tempMergArr[i][0] <= tempMergArr[j][0]) {
	                array[k] = tempMergArr[i];
	                i++;
	            } else {
	                array[k] = tempMergArr[j];
	                j++;
	            }
	            k++;
	        }
	        while (i <= middle) {
	            array[k] = tempMergArr[i];
	            k++;
	            i++;
	        }
	 
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
		integral.setVisible(true);
		entireGUI.setVisible(true);
	}

	public static void main(String[] args){
		new Integrator();
	}
}