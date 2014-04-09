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

public class AGParameters {
	private TrafficJamAGPlayer	_player;
	private JFrame 				_frame;

	final JTextField _nbGenerationsText;
	final JTextField _nbIndividusText;
	final JTextField _tauxSelectionText;
	final JTextField _tauxMutationText;
	final JTextField _tauxCroisementText;

	public AGParameters(TrafficJamAGPlayer player) {
		_player = player;
		_nbGenerationsText = new JTextField(10);
		_nbIndividusText =  new JTextField(10);
		_tauxSelectionText =  new JTextField(10);
		_tauxMutationText =  new JTextField(10);
		_tauxCroisementText =  new JTextField(10);
		
		setTexts();
		
		JPanel p = new JPanel();
		
		p.setLayout(new GridLayout(0,2));
		
		p.add(new JLabel("# Générations", JLabel.LEFT));
		p.add(_nbGenerationsText);
		
		p.add(new JLabel("# Individus", JLabel.LEFT));
		p.add(_nbIndividusText);

		p.add(new JLabel("% Sélection", JLabel.LEFT));
		p.add(_tauxSelectionText);

		p.add(new JLabel("% Mutation", JLabel.LEFT));
		p.add(_tauxMutationText);

		p.add(new JLabel("% Croisement", JLabel.LEFT));
		p.add(_tauxCroisementText);

		JButton ok     = new JButton("  OK  ");
		JButton cancel = new JButton("Cancel");
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					int nbG = (new Integer(_nbGenerationsText.getText())).intValue();
					_player.setNbGenerations(nbG);

					int nbI = (new Integer(_nbIndividusText.getText())).intValue();
					_player.setNbIndividus(nbI);

					// nécessairement après setNbIndividus !!
					float nbS = (new Float(_tauxSelectionText.getText())).floatValue();
					_player.setTauxSelection(nbS);

					float txM = (new Float(_tauxMutationText.getText())).floatValue();
					_player.setTauxMutation(txM);

					float txC = (new Float(_tauxCroisementText.getText())).floatValue();
					_player.setTauxCroisement(txC);
				} catch (Exception _) {}
				
				setTexts();

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
		
		_frame = new JFrame("AGParameters");
		
		JRootPane root = _frame.getRootPane();
		root.setDefaultButton(ok);
		
		_frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				System.out.println("AGParameters closed"); 
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
	
	public void setTexts()
	{
		_nbGenerationsText.setText("" + _player.nbGenerations());
		_nbIndividusText.setText("" + _player.nbIndividus());
		_tauxSelectionText.setText("" + _player.tauxSelection());
		_tauxMutationText.setText("" + _player.tauxMutation());
		_tauxCroisementText.setText("" + _player.tauxCroisement());
	}

    public JFrame getFrame() { return _frame; }	
}
