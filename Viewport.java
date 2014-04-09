import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
/*import java.beans.*;
 import java.awt.geom.*;
 import javax.swing.border.*;
 import java.lang.*;
 import java.io.*;*/

public class Viewport extends JViewport {
	private Interface _interface;
	private BufferedImage _image;
	
	public Viewport(Interface i) {
		_interface = i;
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				draw();
			}}) ;	    
		addMouseListener(new MouseInputAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				System.out.println("(" + x + "," + y +")");
				ObjectEngine mli = _interface.getObjectEngine();
				if(mli != null) {
				    switch (e.getButton()) {
				    case MouseEvent.BUTTON1:
					System.out.println("Button 1");
					mli.mouseClicked(1, x, y);
					break;
				    case MouseEvent.BUTTON2:
					System.out.println("Button 2");
					mli.mouseClicked(2, x, y);
					break;
				    case MouseEvent.BUTTON3:
					System.out.println("Button 3");
					mli.mouseClicked(3, x, y);
					break;
				    default:
					System.out.println("Button ?");
					break;
				    }
				}				
			}});
		draw();
	}
	
	public void draw() {
		ObjectEngine mli = _interface.getObjectEngine();
		if(mli == null)
			return;
		BufferedImage image = mli.draw(this);
		if(image != null)
			_image = image;
		if(_image == null)
			return;
		getGraphics().drawImage(_image, 0, 0, this);
	}
	
	public void paint(Graphics g) {
		g.drawImage(_image, 0, 0, this);
	}	
	
}
