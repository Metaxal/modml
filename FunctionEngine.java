import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JViewport;

/**
 * @author Laurent Orseau
 *
 */
public class FunctionEngine extends ObjectEngine implements ClassOpener {
	public class Point {
		private double _x;
		private double _y;
		
		public double x() { return _x; }
		public double y() { return _y; }
		
		public Point(double x, double y) {
			_x = x;
			_y = y;
		}
	}
	
	protected List _points; // tous dans (-1,-1)->(1,1)
	
	private int _midx;
	private int _midy;
	private int _maxx;
	private int _maxy;
	
	private FunctionLearner 	_learner;
	private JMenu 				_functionMenu;
	private JMenuItem 			_sinItem;
	private JMenuItem 			_linItem;
	private JMenuItem 			_gaussItem;
	private JMenuItem 			_resetItem;
	private ClassLoadMenu 		_classLoadMenu;
	
	public List getPoints() { return _points; }
	
	public FunctionEngine() {
		super();
		
		_learner = null;
	}		
	
	public void initInterface(Interface i) {
		super.initInterface(i);
		_points = Collections.synchronizedList(new LinkedList());
		
		_functionMenu = new JMenu("Function");
		
		_classLoadMenu = new ClassLoadMenu("Learner", this, FunctionLearner.class, _interface);
		_functionMenu.add(_classLoadMenu.getMenu());
		
		_functionMenu.addSeparator();
		
		_resetItem = new JMenuItem("Reset Points");
		_resetItem.addActionListener(this);
		_functionMenu.add(_resetItem);
		
		_sinItem = new JMenuItem("Sinusoide");
		_sinItem.addActionListener(this);
		_functionMenu.add(_sinItem);
		
		_linItem = new JMenuItem("Linear");
		_linItem.addActionListener(this);
		_functionMenu.add(_linItem);
		
		_gaussItem = new JMenuItem("Gaussian");
		_gaussItem.addActionListener(this);
		_functionMenu.add(_gaussItem);

		_interface.getMenu().addMenu(_functionMenu);
	}
	
	public final void terminate() {
		if(_learner != null)
			_learner.terminate();
		_interface.getMenu().removeMenu(_functionMenu);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == _resetItem) {
			resetPoints();
			return;
		}
		if(event.getSource() == _sinItem) {
			sinFunction();
			return;
		}
		if(event.getSource() == _linItem) {
			linFunction();
			return;
		}
		if(event.getSource() == _gaussItem) {
			gaussFunction();
			return;
		}
		super.actionPerformed(event); // au d�but de la fonction plut�t non ?
	}
	
	public void classOpened(Object object, Object caller) {
		if(_learner != null)
			_learner.terminate();
		_learner = (FunctionLearner)object; 
		_learner.init(this); // ne pas oublier !
	}
	
	public void write() {
		System.out.println("Vous �tes dans FunctionEngine");
	}
	
	public BufferedImage draw(JViewport vp) {
		Dimension d = vp.getSize();
		if (d.width <= 0 || d.height <= 0) return null;
		
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setColor(new Color(0,125,0));	    
		g.fillRect(0, 0, d.width, d.height);
		
		g.setColor(new Color(0,0,0));
		g.setStroke(new BasicStroke(3));
		
		int longueurx = d.width-20;
		int longueury = d.height-20;
		_midx = d.width/2;
		_midy = d.height/2;
		_maxx = longueurx/2;
		_maxy = longueury/2;
		
		g.drawLine( _midx-_maxx, _midy, _midx+_maxx, _midy);
		g.drawLine( _midx, _midy-_maxy, _midx, _midy+_maxy);
		
		for(int i = 0; i < _points.size(); i++) {
			Point p = (Point)_points.get(i);
			int px = new Double(_midx + _maxx*p.x()).intValue();
			int py = new Double(_midy + _maxy*p.y()).intValue();
			g.drawLine(px-3, py, px+3, py);
			g.drawLine(px, py-3, px, py+3); 
		}
		
		g.setColor(new Color(0,0,160));
		g.setStroke(new BasicStroke(1));
		if(_learner != null) {
			int px2 = new Double(_midx + _maxx*-1.).intValue();
			double vy2 = _learner.y(-1.);
			if(vy2 > 1.) vy2 = 1.; else if(vy2 < -1.) vy2 = -1.;
			int py2 = new Double(_midy + _maxy*vy2).intValue();
			for(double vx=-1.; vx < 1.; vx += 2/(_maxx+0.)) { // /_maxx ??? -> tous les 2 pixels
				double vy = _learner.y(vx);
				//System.out.println("vx = " + vx + " vy = " + vy);
				if(vy > 1.) vy = 1.; else if(vy < -1.) vy = -1.;
				int px = new Double(_midx + _maxx*vx).intValue();
				int py = new Double(_midy + _maxy*vy).intValue();
				g.drawLine(px2, py2, px, py);
				
				px2 = px;
				py2 = py;
			}
			
		}
		
		
		return image;
	}
	
    public void mouseClicked(int whichButton, int x, int y) {
		_points.add(new Point((x-_midx)/(_maxx+0.d), (y-_midy)/(_maxy+0.d)));
		System.out.println("Mouse Clicked in FunctionEngine : (" + x + ", " + y + ")");
		_interface.redraw();
	}

	
	public void resetPoints() {
		_points.clear();
		_interface.redraw();
	}
	
	public void sinFunction() {
		_points.clear();
		for(double i=-1.; i < 1.; i+=0.1) {
			_points.add(new Point(i, Math.sin(i*3.15)));
		}
		_interface.redraw();
	}

	public void linFunction() {
		_points.clear();
		for(double i=-1.; i < 1.; i+=0.1) {
			_points.add(new Point(i, i/2.));
		}
		_interface.redraw();
	}

	public void gaussFunction() {
		_points.clear();
		for(double i=-1.; i < 1.; i+=0.1) {
			_points.add(new Point(i, -Math.exp(-i*i*10.)));
		}
		_interface.redraw();
	}
}
