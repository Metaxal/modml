import java.awt.*;
import javax.swing.*;
/*import java.awt.event.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.geom.*;
import javax.swing.border.*;
import java.awt.image.BufferedImage;
import java.lang.*;
import java.io.*;*/

public class ProgressBar extends JPanel {
    private Interface _interface;
    private JProgressBar _pbar;

    public ProgressBar(Interface i, int min, int max) {	    
	super(true);	    
	_interface = i;
	setLayout(new GridLayout(1,1,0,0));	    
	_pbar = new JProgressBar();	    
	setMinimum(min);
	setMaximum(max);	    
	add(_pbar);
    }

    public void setValue(int newValue) {
	_pbar.setValue(newValue);
    }
	
    public void setMaximum(int max) {
	_pbar.setMaximum(max);
    }
	    
    public void setMinimum(int min) {
	_pbar.setMinimum(min);
    }
}
