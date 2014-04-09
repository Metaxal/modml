import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

public class AboutBox {
	private JFrame _frame;
	
	public JFrame getFrame() { return _frame; }	
	
	public AboutBox() {
		super();
		
		JLabel texteLabel = new JLabel("<html>Module Machine Learning<br>Pascal Garcia &amp; Laurent Orseau<br><br>&copy;&nbsp;Insa de Rennes 2005-2006</html>");
		
		JPanel p = new JPanel();
		
		p.setLayout(new FlowLayout());
		
		p.add(texteLabel);

		JButton ok     = new JButton("  OK  ");
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				_frame.setVisible(false);
			}});	    	    
		
		JPanel buttonPanel = new JPanel();	    
		buttonPanel.add(ok);

		JPanel buttonSpacePanel = new JPanel();
		buttonSpacePanel.setLayout(new BorderLayout());	    
		buttonSpacePanel.add(new JLabel(" ", JLabel.RIGHT), BorderLayout.NORTH);
		buttonSpacePanel.add(buttonPanel, BorderLayout.CENTER);	
		
		_frame = new JFrame("About");
		
		JRootPane root = _frame.getRootPane();
		root.setDefaultButton(ok);
		
		_frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				System.out.println("TrafficJam parameters closed"); 
			}});
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(10);
		_frame.getContentPane().setLayout(borderLayout);
		
		_frame.getContentPane().add(new JLabel("     ", JLabel.RIGHT), BorderLayout.NORTH);
		_frame.getContentPane().add(p, BorderLayout.CENTER);
		_frame.getContentPane().add(buttonSpacePanel, BorderLayout.SOUTH);
		_frame.getContentPane().add(new JLabel("     ", JLabel.RIGHT), BorderLayout.EAST);
		_frame.getContentPane().add(new JLabel("     ", JLabel.RIGHT), BorderLayout.WEST);	    
		
		_frame.pack();
		_frame.setResizable(false);
		Utility.center(_frame);
		
	}
	
}
