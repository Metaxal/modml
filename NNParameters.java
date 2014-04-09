import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class NNParameters {
	private FunctionLearner _engine;
	private JFrame _frame;
	
	final JTextField _nbNeuronesText;
	final JTextField _nbIterationsText;
	final JTextField _epsilonText;
	final JTextField _alphaText;
	final JTextField _speedText;
	
	public JFrame getFrame() { return _frame; }	
	
	public NNParameters(FunctionLearner engine) {
		_engine = engine;
		_nbNeuronesText = new JTextField(10);
		_nbIterationsText = new JTextField(10);
		_epsilonText = new JTextField(10);
		_alphaText = new JTextField(10);
		_speedText = new JTextField(10);
		
		setTexts();
		
		JPanel p = new JPanel();
		
		p.setLayout(new GridLayout(0,2));
		
		p.add(new JLabel("# Neurones", JLabel.LEFT));
		p.add(_nbNeuronesText);
		
		p.add(new JLabel("Epsilon", JLabel.LEFT));
		p.add(_epsilonText);
		
		p.add(new JLabel("Alpha", JLabel.LEFT));
		p.add(_alphaText);
		
		p.add(new JLabel("# Iterations", JLabel.LEFT));
		p.add(_nbIterationsText);
		
		p.add(new JLabel("Rafraichissement(ms)", JLabel.LEFT));
		p.add(_speedText);
		
		JButton ok     = new JButton("  OK  ");
		JButton cancel = new JButton("Cancel");
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					double alpha = (new Double(_alphaText.getText())).doubleValue();
					_engine.setAlpha(alpha);
					
					double epsilon = (new Double(_epsilonText.getText())).doubleValue();
					_engine.setEpsilon(epsilon);
					
					int speed = (new Integer(_speedText.getText())).intValue();
					_engine.setRefreshSpeed(speed);

					int iter = (new Integer(_nbIterationsText.getText())).intValue();
					_engine.setNbIterations(iter);

					// A mettre à la fin car il risque de recréer le RN (donc autant changer les autres parameètres avant.
					int nbNeurs = (new Integer(_nbNeuronesText.getText())).intValue();
					_engine.setNbNeurones(nbNeurs);
					
					_engine.nouveauReseau(); // il ne faudrait le faire que lorsque le nombre de neurones a changé ! 
				} catch (Exception _) {}
				
				setTexts();
				
				_engine.getInterface().redraw();
				_frame.setVisible(false);			
			}});		
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				_frame.setVisible(false);
			}});	    	    
		
		JPanel buttonPanel = new JPanel();	    
		buttonPanel.add(ok);
		buttonPanel.add(cancel);	
		
		JPanel buttonSpacePanel = new JPanel();
		buttonSpacePanel.setLayout(new BorderLayout());	    
		buttonSpacePanel.add(new JLabel(" ", JLabel.RIGHT), BorderLayout.NORTH);
		buttonSpacePanel.add(buttonPanel, BorderLayout.CENTER);	
		
		_frame = new JFrame("TrafficJam parameters");
		
		JRootPane root = _frame.getRootPane();
		root.setDefaultButton(ok);
		
		_frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				System.out.println("TrafficJam parameters closed"); 
			}});
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(10);
		_frame.getContentPane().setLayout(borderLayout);
		
		_frame.getContentPane().add(new JLabel(" ", JLabel.RIGHT), BorderLayout.NORTH);
		_frame.getContentPane().add(p, BorderLayout.CENTER);
		_frame.getContentPane().add(buttonSpacePanel, BorderLayout.SOUTH);
		_frame.getContentPane().add(new JLabel(" ", JLabel.RIGHT), BorderLayout.EAST);
		_frame.getContentPane().add(new JLabel(" ", JLabel.RIGHT), BorderLayout.WEST);	    
		
		_frame.pack();
		_frame.setResizable(false);
		Utility.center(_frame);
	}
	
	public void setTexts() {
		_epsilonText.setText("" + _engine.epsilon());
		_alphaText.setText("" + _engine.alpha());
		_nbNeuronesText.setText("" + _engine.nbNeurones());
		_speedText.setText("" + _engine.refreshSpeed());
		_nbIterationsText.setText("" + _engine.nbIterations());
	}
}

