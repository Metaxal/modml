import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JViewport;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.*;
/*import java.awt.event.*;
  import javax.swing.event.*;
  import java.beans.*;*/
import java.awt.geom.*;

public class RobotEngine extends ObjectEngine implements ClassOpener {
    private JMenu	       	_robotMenu;
    private JMenu          	_buildSubMenu;
    private JMenuItem          	_newItem;      
    private JMenuItem          	_obstaclesItem;
    private JMenuItem          	_robotPositionItem;
    private JMenu          	_controlSubMenu;
    private JMenuItem          	_startItem;      
    private JMenuItem          	_stopItem;
    private JMenuItem          	_manualItem;
    private JMenuItem          	_automaticItem;
    //private ToolBar            	_toolBar;    

    private LinkedList          _polygonList;
    private Polygon             _polygon;
    private double              _robotX;
    private double              _robotY;
    private double              _robotAngle;
    private double              _robotSpeed;
    private double              _robotInertia;
    private double              _robotHeight;
    private double              _robotWidth;
    private double              _angle;
    private double              _speed;
    private double              _magnitude;
    private int                 _accuracy;
    private double              _range;
    private int _firstX;
    private int _firstY;

    private FuzzyLogicBasic     _learner;

    private ClassLoadMenu 	_classLoadMenu;
	
    public RobotEngine() {
	super();
	_polygonList = new LinkedList();
	_polygon = new Polygon();
	_robotX = -1;
	_robotY = -1;
	_robotAngle = (double)(Math.PI / 2);
	_robotSpeed = 0;
	_robotInertia = 0.5;
	_robotHeight = 20;
	_robotWidth = 10;
	_angle = 0;
	_speed = 10;
	_magnitude = (double)Math.PI / 4;
	_accuracy = 10;
	_range = 100;
	_firstX = -1;
	_firstY = -1;
	StrongTriangularFuzzyPartition[] sftps = new StrongTriangularFuzzyPartition[3];
	sftps[0] = new StrongTriangularFuzzyPartition("front", new double[]{0, _range / 2, _range});
	sftps[1] = new StrongTriangularFuzzyPartition("left", new double[]{0, _range / 2, _range});
	sftps[2] = new StrongTriangularFuzzyPartition("right", new double[]{0, _range / 2, _range});
	_learner = new FuzzyLogicBasic(sftps, 1);
    }

    public void initInterface(Interface i) {
	super.initInterface(i);
		
	// ajout du menu propre à RobotEngine
	// En classe chargée dynamiquement, il est nécessaire de créer les menus à la volée et non en tête de classe !
	_robotMenu = new JMenu("Robot");
		
	_buildSubMenu = new JMenu("Build");
	_buildSubMenu.addActionListener(this);
	_robotMenu.add(_buildSubMenu);
						
	_newItem = new JMenuItem("New");
	_newItem.addActionListener(this);
	_buildSubMenu.add(_newItem);

	_buildSubMenu.addSeparator();

	ButtonGroup group = new ButtonGroup();			

	_obstaclesItem = new JRadioButtonMenuItem("Obstacles");
	_obstaclesItem.addActionListener(this);
	_buildSubMenu.add(_obstaclesItem);
	group.add(_obstaclesItem);
		
	_robotPositionItem = new JRadioButtonMenuItem("Robot position");
	_robotPositionItem.addActionListener(this);
	_buildSubMenu.add(_robotPositionItem);
	group.add(_robotPositionItem);

	_obstaclesItem.setSelected(true);

	_controlSubMenu = new JMenu("Control");
	_controlSubMenu.addActionListener(this);
	_robotMenu.add(_controlSubMenu);
						
	_startItem = new JMenuItem("Start");
	_startItem.addActionListener(this);
	_controlSubMenu.add(_startItem);

	_stopItem = new JMenuItem("Stop");
	_stopItem.addActionListener(this);
	_controlSubMenu.add(_stopItem);

	_controlSubMenu.addSeparator();

	group = new ButtonGroup();			

	_manualItem = new JRadioButtonMenuItem("Manual");
	_manualItem.addActionListener(this);
	_controlSubMenu.add(_manualItem);
	group.add(_manualItem);
		
	_automaticItem = new JRadioButtonMenuItem("Automatic");
	_automaticItem.addActionListener(this);
	_controlSubMenu.add(_automaticItem);
	group.add(_automaticItem);

	_manualItem.setSelected(true);
		
	_interface.getMenu().add(_robotMenu);
	_interface.getMenu().validate();
	//	_interface.getFrame().getContentPane().add(_toolBar, BorderLayout.NORTH);
	//_interface.getFrame().getContentPane().validate();
    }
		
    public void actionPerformed(ActionEvent event) {
	System.out.println("Menu Robot item [" + event.getActionCommand() + "] was pressed.");

	if (event.getSource() == _newItem) {	    
	    newMap();
	}
	if (event.getSource() == _obstaclesItem) {
	}	    
	if (event.getSource() == _robotPositionItem) {
	}	    
	if (event.getSource() == _startItem) {
	    move();
	}	    

    }

    public void newMap() {
	_polygonList = new LinkedList();
	_robotX = _robotY = -1;
	_interface.redraw();	
    }
    
	
    public void classOpened(Object object, Object caller) {
    }
	
    private Polygon createRobotPolygon() {
	Polygon res = new Polygon();
	double x = _robotX - Math.cos(_robotAngle) * _robotHeight;
	double y = _robotY + Math.sin(_robotAngle) * _robotHeight;
	double dx = Math.sin(_robotAngle) * _robotWidth;
	double dy = Math.cos(_robotAngle) * _robotWidth;

	res.addPoint((int)Math.round(_robotX), (int)Math.round(_robotY));
	res.addPoint((int)Math.round(x + dx), 
		     (int)Math.round(y + dy));
	res.addPoint((int)Math.round(x), 
		     (int)Math.round(y));
	res.addPoint((int)Math.round(x - dx), 
	(int)Math.round(y - dy));
	return res;
    }

    private LinkedList createRobotBoundary() {
	LinkedList res = new LinkedList();
	double x = _robotX - Math.cos(_robotAngle) * _robotHeight;
	double y = _robotY + Math.sin(_robotAngle) * _robotHeight;
	double dx = Math.sin(_robotAngle) * _robotWidth;
	double dy = Math.cos(_robotAngle) * _robotWidth;

	res.add(new Line2D.Double(_robotX, _robotY, x + dx, y + dy));
	res.add(new Line2D.Double(x + dx, y + dy, x - dx, y - dy));
	res.add(new Line2D.Double(x - dx, y - dy, _robotX, _robotY));
	return res;
    }
    
    private LinkedList createFrontRadarBeams() {
	LinkedList res = new LinkedList();
	double angleStep = _magnitude / _accuracy;
	for (int i = 0; i < _accuracy; i++) {
	    double dx = _range * Math.cos(_robotAngle - _magnitude / 2 + i * angleStep);
	    double dy = _range * Math.sin(_robotAngle - _magnitude / 2 + i * angleStep);
	    res.add(new Line2D.Double(_robotX, _robotY, _robotX + dx, _robotY - dy));
	}
	return res; 
    }

    private LinkedList createLeftRadarBeams() {
	LinkedList res = new LinkedList();
	double angleStep = _magnitude / _accuracy;
	for (int i = 0; i < _accuracy; i++) {
	    double dx = _range * Math.cos(_robotAngle - Math.PI / 3 - _magnitude / 2 + i * angleStep);
	    double dy = _range * Math.sin(_robotAngle - Math.PI / 3 - _magnitude / 2 + i * angleStep);
	    res.add(new Line2D.Double(_robotX, _robotY, _robotX + dx, _robotY - dy));
	}
	return res; 
    }

    private LinkedList createRightRadarBeams() {
	LinkedList res = new LinkedList();
	double angleStep = _magnitude / _accuracy;
	for (int i = 0; i < _accuracy; i++) {
	    double dx = _range * Math.cos(_robotAngle + Math.PI / 3 - _magnitude / 2 + i * angleStep);
	    double dy = _range * Math.sin(_robotAngle + Math.PI / 3 - _magnitude / 2 + i * angleStep);
	    res.add(new Line2D.Double(_robotX, _robotY, _robotX + dx, _robotY - dy));
	}
	return res; 
    }

    private boolean isInSegment(Line2D line, double x, double y) {
	return 
	    Math.min(line.getX1(), line.getX2()) <= x &&
	    Math.max(line.getX1(), line.getX2()) >= x &&
	    Math.min(line.getY1(), line.getY2()) <= y &&
	    Math.max(line.getY1(), line.getY2()) >= y;
    }

    private Point2D intersection(Line2D line1, Line2D line2) {
	double a1 = (line1.getX1() == line1.getX2() ? Double.POSITIVE_INFINITY 
		    : (line1.getY2() - line1.getY1()) / (line1.getX2() - line1.getX1()));
	double a2 = (line2.getX1() == line2.getX2() ? Double.POSITIVE_INFINITY 
		    : (line2.getY2() - line2.getY1()) / (line2.getX2() - line2.getX1()));

	if (a1 == Double.POSITIVE_INFINITY) {
	    if (a2 == Double.POSITIVE_INFINITY) {
		if (line1.getX1() != line2.getX1()) return new Point2D.Double(-1,-1);
		else {
		    if (distanceToRobot(line2.getP1()) <= distanceToRobot(line2.getP2())) {
			if (Math.min(line1.getY1(), line1.getY2()) <= line2.getP1().getY() &&
			    Math.max(line1.getY1(), line1.getY2()) >= line2.getP1().getY()) {
			    return line2.getP1();
			}
			else {
			    return new Point2D.Double(-1,-1);
			}
		    }
		    else {
			if (Math.min(line1.getY1(), line1.getY2()) <= line2.getP2().getY() &&
			    Math.max(line1.getY1(), line1.getY2()) >= line2.getP2().getY()) {
			    return line2.getP2();
			}
			else {
			    return new Point2D.Double(-1,-1);
			}
		    }
		}
	    }
	    else {
		double b2 = line2.getY1() - a2 * line2.getX1();
		double y = a2 * line1.getX1() + b2;
		if (isInSegment(line1, line1.getX1(), y) && isInSegment(line2, line1.getX1(), y)) {
		    return new Point2D.Double(line1.getX1(), y);
		}
		else {
		    return new Point2D.Double(-1,-1);
		}
	    }
	}
	else {
	    if (a2 == Double.POSITIVE_INFINITY) {
		double b1 = line1.getY1() - a1 * line1.getX1();
		double y = a1 * line2.getX1() + b1;
		if (isInSegment(line1, line2.getX1(), y) && isInSegment(line2, line2.getX1(), y)) {
		    return new Point2D.Double(line2.getX1(), y);
		}
		else {
		    return new Point2D.Double(-1,-1);
		}
	    }
	    else {
		double b1 = line1.getY1() - a1 * line1.getX1();
		double b2 = line2.getY1() - a2 * line2.getX1();

		if (a1 == a2) {
		    if (b1 != b2) return new Point2D.Double(-1,-1);
		    else {
			if (distanceToRobot(line2.getP1()) <= distanceToRobot(line2.getP2())) {
			    if (Math.min(line1.getY1(), line1.getY2()) <= line2.getP1().getY() &&
				Math.max(line1.getY1(), line1.getY2()) >= line2.getP1().getY()) {
				return line2.getP1();
			    }
			    else {
				return new Point2D.Double(-1,-1);
			    }
			}
			else {
			    if (Math.min(line1.getY1(), line1.getY2()) <= line2.getP2().getY() &&
				Math.max(line1.getY1(), line1.getY2()) >= line2.getP2().getY()) {
				return line2.getP2();
			    }
			    else {
				return new Point2D.Double(-1,-1);
			    }
			}
		    }
		}
		else {
		    double x = (b2 - b1) / (a1 - a2);
		    double y = x * a1 + b1;
		    if (isInSegment(line1, x, y) && isInSegment(line2, x, y)) {
			return new Point2D.Double(x,y);
		    }
		    else {
			return new Point2D.Double(-1f,-1f);
		    }
		}
	    }
	}
    }

    private double distanceToRobot(Point2D point) {
	return point.distance(_robotX, _robotY);
    }

    private Point2D intersection(Line2D line, Polygon polygon) {
	Point2D res = new Point2D.Double(-1f,-1f);
	PathIterator pathIterator = 
	    polygon.getPathIterator(new AffineTransform());

	double[] coordinates = new double[6];	

	if (!pathIterator.isDone()) {
	    pathIterator.currentSegment(coordinates);
	    pathIterator.next();
	    coordinates[2] = coordinates[0];
	    coordinates[3] = coordinates[1];
	}
	while(!pathIterator.isDone()) {
	    pathIterator.currentSegment(coordinates);
	    pathIterator.next();
	    Point2D point = intersection(line, new Line2D.Double(coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
	    if (point.getX() != -1) {
		if (res.getX() == -1 || distanceToRobot(point) < distanceToRobot(res)) {
		    res = point;
		} 
	    }
	    coordinates[2] = coordinates[0];
	    coordinates[3] = coordinates[1];
	}
	return res;
    }

    private Point2D intersection(LinkedList beams, Polygon polygon) {
	Point2D min = new Point2D.Double(-1, -1);
	ListIterator listIterator = beams.listIterator();
	while(listIterator.hasNext()) {
	    Point2D res = intersection((Line2D)listIterator.next(), polygon);
	    if (min.getX() == -1) {
		min = res;
	    }
	    else {
		if (res.getX() != -1) {
		    if (distanceToRobot(res) < distanceToRobot(min))
			min = res;
		}
	    }
	}
	return min;
    }

    private Point2D intersection(LinkedList beams) {
	Point2D min = new Point2D.Double(-1, -1);
	ListIterator listIterator = _polygonList.listIterator();
	while(listIterator.hasNext()) {
	    Point2D res = intersection(beams, (Polygon)listIterator.next());
	    if (min.getX() == -1) {
		min = res;
	    }
	    else {
		if (res.getX() != -1) {
		    if (distanceToRobot(res) < distanceToRobot(min))
			min = res;
		}
	    }
	}
	return min;
    }
	
    public BufferedImage draw(JViewport vp) {
	Dimension d = vp.getSize();
	if (d.width <= 0 || d.height <= 0) return null;
		
	BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
	Graphics2D g = image.createGraphics();

	int side = Math.min(d.width, d.height);
	double tr = side / 1000.0;

	g.setTransform(new AffineTransform(tr, 0, 0, tr, 0, 0));

	g.setColor(new Color(0,125,0));	    
	g.fillRect(0, 0, (int)Math.round(1000.0 * d.width / side), (int)Math.round(1000 * d.height / side));

	int w = d.width;
	int h = d.height;

	int margin  = 15;
	if (_polygonList.size() == 0) {
	    _polygonList.add(new Polygon(new int[]{0, (int)Math.round(1000.0 * w / side), 
						   (int)Math.round(1000.0 * w / side), 0, 0},
					 new int[]{0, 0, margin, margin, 0}, 5));
	    _polygonList.add(new Polygon(new int[]{0, margin, margin, 0, 0},
					 new int[]{0, 0, (int)Math.round(1000.0 * h / side), 
						   (int)Math.round(1000.0 * h / side), 0}, 5));
	    _polygonList.add(new Polygon(new int[]{0, (int)Math.round(1000.0 * w / side), 
						   (int)Math.round(1000.0 * w / side), 0, 0},
					 new int[]{(int)Math.round(1000.0 * h / side - margin), 
						   (int)Math.round(1000.0 * h / side - margin), 
						   (int)Math.round(1000.0 * h / side), 
						   (int)Math.round(1000.0 * h / side),
						   (int)Math.round(1000.0 * h / side - margin)}, 5));
	    _polygonList.add(new Polygon(new int[]{(int)Math.round(1000.0 * w / side - margin), 
						   (int)Math.round(1000.0 * w / side), 
						   (int)Math.round(1000.0 * w / side), 
						   (int)Math.round(1000.0 * w / side - margin),
						   (int)Math.round(1000.0 * w / side - margin)},
					 new int[]{0, 0, (int)Math.round(1000.0 * h / side), 
						   (int)Math.round(1000.0 * h / side), 0}, 5));
	} 
	else {
	    _polygonList.set(0, new Polygon(new int[]{0, (int)Math.round(1000.0 * w / side), 
						      (int)Math.round(1000.0 * w / side), 0, 0},
					    new int[]{0, 0, margin, margin, 0}, 5));
	    _polygonList.set(1, new Polygon(new int[]{0, margin, margin, 0, 0},
					    new int[]{0, 0, (int)Math.round(1000.0 * h / side), 
						      (int)Math.round(1000.0 * h / side), 0}, 5));
	    _polygonList.set(2, new Polygon(new int[]{0, (int)Math.round(1000.0 * w / side), 
						      (int)Math.round(1000.0 * w / side), 0, 0},
					    new int[]{(int)Math.round(1000.0 * h / side - margin), 
						      (int)Math.round(1000.0 * h / side - margin), 
						      (int)Math.round(1000.0 * h / side), 
						      (int)Math.round(1000.0 * h / side),
						   (int)Math.round(1000.0 * h / side - margin)}, 5));
	    _polygonList.set(3, new Polygon(new int[]{(int)Math.round(1000.0 * w / side - margin), 
						      (int)Math.round(1000.0 * w / side), 
						      (int)Math.round(1000.0 * w / side), 
						      (int)Math.round(1000.0 * w / side - margin),
						      (int)Math.round(1000.0 * w / side - margin)},
					    new int[]{0, 0, (int)Math.round(1000.0 * h / side), 
						      (int)Math.round(1000.0 * h / side), 0}, 5));
	}
	g.setColor(new Color(0,0,0));
	
	PathIterator pathIterator = 
	    _polygon.getPathIterator(new AffineTransform());

	double[] coordinates = new double[6];	
	if (!pathIterator.isDone()) {
	    pathIterator.currentSegment(coordinates);
	    pathIterator.next();
	    coordinates[2] = coordinates[0];
	    coordinates[3] = coordinates[1];
	}
	while(!pathIterator.isDone()) {
	    pathIterator.currentSegment(coordinates);
	    pathIterator.next();
	    g.draw(new Line2D.Double(coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
	    coordinates[2] = coordinates[0];
	    coordinates[3] = coordinates[1];
	}

	ListIterator listIterator = _polygonList.listIterator();
	while(listIterator.hasNext()) {
	    g.fill((Shape)listIterator.next());
	}

	if (_robotX != -1f && _robotY != -1f) {
	    Point2D collision = intersection(createRobotBoundary());
	    if (collision.getX() != -1) {
		System.out.println("I am the king of the world, glou glou glou ...");
		_robotX = _robotY = -1;
	    }
	    else {
		g.setColor(new Color(255,0,0));
		Polygon robot = createRobotPolygon();
		g.fill((Shape)robot);
		LinkedList frontRadarBeams = createFrontRadarBeams();
		listIterator = frontRadarBeams.listIterator();
		while (listIterator.hasNext()) {
		    g.draw((Shape)listIterator.next());
		}
		LinkedList leftRadarBeams = createLeftRadarBeams();
		listIterator = leftRadarBeams.listIterator();
		while (listIterator.hasNext()) {
		    g.draw((Shape)listIterator.next());
		}
		LinkedList rightRadarBeams = createRightRadarBeams();
		listIterator = rightRadarBeams.listIterator();
		while (listIterator.hasNext()) {
		    g.draw((Shape)listIterator.next());
		}
		Point2D front = intersection(frontRadarBeams);
		Point2D left = intersection(leftRadarBeams);
		Point2D right = intersection(rightRadarBeams);
		g.setColor(new Color(0,0,255));
		if (front.getX() != -1) g.fill(new Ellipse2D.Double(front.getX() - 5, front.getY() - 5, 10, 10));
		if (left.getX() != -1) g.fill(new Ellipse2D.Double(left.getX() - 5, left.getY() - 5, 10, 10));
		if (right.getX() != -1) g.fill(new Ellipse2D.Double(right.getX() - 5, right.getY() - 5, 10, 10));	    
	    }
	}

	return image;
    }
	
	
    public void mouseClicked(int whichButton, int x, int y) {
	Dimension d = _interface.getViewport().getSize();
	if (_obstaclesItem.isSelected()) {
	    switch (whichButton) {
	    case 1:
		double tr = 1000.0 / Math.min(d.width, d.height);
		int newX = (int)Math.round(tr * x);
		int newY = (int)Math.round(tr * y);
		_firstX = (_firstX == -1 ? newX : _firstX);
		_firstY = (_firstY == -1 ? newY : _firstY);
		_polygon.addPoint(newX,newY);
		_interface.redraw();
		break;
	    case 2:
		_polygon.addPoint(_firstX, _firstY);
		_firstX = _firstY = -1;
		_polygonList.add(_polygon);
		_polygon = new Polygon();
		_interface.redraw();
		break;
	    case 3:
		_firstX = _firstY = -1;
		_polygon = new Polygon();
		_interface.redraw();
		break;
	    }
	}
	else {
	    double tr = 1000.0 / Math.min(d.width, d.height);	    
	    _robotX = tr * x;
	    _robotY = tr * y;
	    _interface.redraw();
	}
    }

    public void move() {
	Thread worker = new Thread() {
		public synchronized void run() {
		    try {
			while(_robotX != 1 && _robotY != -1) {
			    double front = distanceToRobot(intersection(createFrontRadarBeams()));
			    double left = distanceToRobot(intersection(createLeftRadarBeams()));
			    double right = distanceToRobot(intersection(createRightRadarBeams()));
			    if (_automaticItem.isSelected()) {
				_angle = _learner.getValue(new double[]{front, left, right})[0];
			    }
			    else {
				_learner.setConclusions(new double[]{front, left, right}, new double[]{_angle});
			    }
			    _robotAngle = _robotAngle + (1 - _robotInertia) * _angle;
			    System.out.println("angle = " + _angle);

			    if (_robotAngle >= 2 * Math.PI)
				_robotAngle -= 2 * Math.PI;
			    if (_robotAngle <= - 2 * Math.PI)
				_robotAngle += 2 * Math.PI;
			    _robotSpeed = 10;
			    _robotX += _robotSpeed * Math.cos(_robotAngle);
			    _robotY -= _robotSpeed * Math.sin(_robotAngle); 
			    _interface.redraw();
			    Thread.sleep(100);
			}
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    };
	worker.start();
    }


    public void keyTyped(char c) {
	if (c == 'a') _automaticItem.setSelected(true);
	if (c == 'm') _manualItem.setSelected(true);
	if (_robotX != -1 && _robotY != -1 && _manualItem.isSelected()) {
	    switch (c) {
	    case '4':
		_angle = Math.min(_angle + Math.PI / 12, Math.PI / 6);
		break;
	    case '6':
		_angle = Math.max(_angle - Math.PI / 12, -Math.PI / 6);
		break;
	    }
	}
    }

	
    public void write() {
	System.out.println("Vous êtes dans RobotEngine");
    }
	
    public void terminate() {
	// supprimer les menus
	_interface.getMenu().remove(_robotMenu);
	_interface.getMenu().validate();
	_interface.getMenu().repaint();		
    }
}
