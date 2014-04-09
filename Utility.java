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

public class Utility {
    public static void center(JFrame frame) {	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int x = (screenSize.width - frame.getSize().width) / 2;
	int y = (screenSize.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
    }
}
