package integrator;

//Logic
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

//Swing
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//Graphing
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
//LateX
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import evaluator.Evaluator;
import evaluator.MutableDouble;

public class Integrator extends JFrame 
{
	private Evaluator eval;
	
	private JPanel entireGUI, enterPanel, optionsPanel, integralPanel, graphPanel;
	private Box buttonBox, valsBox, verticalBox, resultBox, consoleBox;
	private JTextField integrand, lbound, ubound, samples;
	private JRadioButton right, left, mid, trap;
	private JTextArea console;
	
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
		
		buildMainGUI();
		
		consoleBox = Box.createVerticalBox();
		console = new JTextArea("", 14, 20);
		console.setEditable(false);
		consoleBox.add(new JLabel("Console:"));
		consoleBox.add(console);
		
		entireGUI.add(verticalBox);
		entireGUI.add(graphPanel);
		entireGUI.add(consoleBox);
		
		add(entireGUI);
		pack();
		integral.setVisible(false);
		entireGUI.setVisible(true);
		this.setContentPane(entireGUI);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void buildMainGUI() {
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
						console.setText("");
						integrand.setText("");
						lbound.setText("");
						ubound.setText("");
						samples.setText("500000");
						integral.setIcon(null);
						avgval.setText(" ");
						area.setText(" ");
						graphPanel.removeAll();
						entireGUI.setVisible(true);
						pack();
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
		
		graphPanel = new JPanel();
		
		
		verticalBox = Box.createVerticalBox();
		verticalBox.add(enterPanel);
		verticalBox.add(optionsPanel);
		verticalBox.add(resultBox);
		verticalBox.add(integralPanel);
	}
	
	private void calculate() throws Exception
	{
		eq = integrand.getText();
		lb = Double.parseDouble(lbound.getText());
		ub = Double.parseDouble(ubound.getText());
		sam = Integer.parseInt(samples.getText());
		console.setText("");
		
		addMessage("Parsing...");
		eval.parse(eq);
		
		Map<String, MutableDouble> varSet = eval.getKeys();
		Set<String> vars = varSet.keySet();
		
		if(vars.size()!=1)
		{
			JOptionPane.showMessageDialog(entireGUI, "Wrong number of variables", "Error", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		String key = vars.iterator().next();
		
		addMessage("Generating random X values...");
		double[][] vals = new double[sam][2];
		for(int i = 0; i < vals.length; i++){ 
			vals[i][0] = randInBound();
		}
		
		addMessage("Sorting...");
		MergeSorter sorter = new MergeSorter();
		sorter.sort(vals);
	
		double sum = 0;
		addMessage("Calculating...");
		if(mid.isSelected())
		{
			//Creates a new array new values in the middle of the previously selected ones.
			double[][] midVals = new double[sam - 1][2];
			for(int i = 0; i < midVals.length; i++)    //Midpoint sum
			{ 
				double width = vals[i+1][0]-vals[i][0];
				midVals[i][0] = width / 2 + vals[i][0];
				varSet.get(key).setVal(midVals[i][0]); 
				midVals[i][1] = eval.evaluate(varSet);
				double height = midVals[i][1];
				sum+=height*width;
			}
		}
		else
		{
			for(int i = 0; i < vals.length; i++)
			{
				varSet.get(key).setVal(vals[i][0]);  
				vals[i][1] = eval.evaluate(varSet);
			}
			if(trap.isSelected())
			{
				for(int i = 0; i < vals.length-1; i++){		//Trapezoid sum
					double height = (vals[i][1]+vals[i+1][1])/2;
					double width = vals[i+1][0]-vals[i][0];
					sum+=height*width;
				}
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
		}
		
		DecimalFormat df = new DecimalFormat("####0.00000");
		areaStr = df.format(sum);
		
		avgval.setText((1/(ub-lb))*sum+"");
		area.setText(sum+"");

		addMessage("Graphing...");
		JPanel chartpanel = graphEquation(vars, vals);
		
		graphPanel.add(chartpanel);
		graphPanel.setVisible(true);
		entireGUI.add(graphPanel);
		pack();
		
		latexRender();
	
	}

	private JPanel graphEquation(Set<String> vars, double[][] vals) {
		graphPanel.removeAll();//reset
		XYSeries graphPoints = new XYSeries("");
		for (int i = 0; i<vals.length; i += 100) { //plots every 100th point, just for time vs. result efficiency
			graphPoints.add(vals[i][0], vals[i][1]);
		}
		XYSeriesCollection xy = new XYSeriesCollection();
		xy.addSeries(graphPoints);
		
		String varName = vars.iterator().next();		//Properly name the variable
		JFreeChart jfreechart = ChartFactory.createXYLineChart("Graph", varName, "f(" + varName + ")", xy, PlotOrientation.VERTICAL, false, false, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.getDomainAxis().setLowerMargin(0.0D);
		xyplot.getDomainAxis().setUpperMargin(0.0D);
		JPanel chartpanel = new ChartPanel(jfreechart);
		return chartpanel;
	}
	
	private void addMessage(String msg){
		console.setText(console.getText() + "\n" + msg);
		repaint();
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