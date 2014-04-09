import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
//import java.awt.BasicStroke;

public class ObjectEngine implements ActionListener {
	protected Interface _interface;

	public Interface getInterface() { return _interface; }
	
	public void initInterface(Interface i) { 
		_interface = i; 
	}
	
	public void write() {
		System.out.println("Vous êtes dans ObjectEngine");
	}
	
    public void mouseClicked(int whichButton, int x, int y) {
	System.out.println("Mouse Clicked in ObjectEngine : (" + x + ", " + y + ")");
    }
	
	public void keyPressed(char c) {
		//System.out.println("keyPressed in ObjectEngine : "+ c);
	}
	
	public void keyReleased(char c) {
		//System.out.println("keyReleased in ObjectEngine : "+ c);
	}
	
	public void keyTyped(char c) {
		//System.out.println("keyTyped in ObjectEngine : "+ c);
	}
	
	public void terminate() {
		// supprimer les menus
	}
	
	public BufferedImage draw(JViewport vp) {
		Dimension d = vp.getSize();
		if (d.width <= 0 || d.height <= 0) return null;
		
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setColor(new Color(0,125,0));	    
		g.fillRect(0, 0, d.width, d.height);
		return image;
	}
	
	public void redraw() {
		if(_interface != null)
			_interface.redraw();
	}
	
	public void actionPerformed(ActionEvent arg0) {
	}
}
