import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JViewport;

public class TrafficJamEngine extends ObjectEngine implements ClassOpener {
	
	private JMenu 				_menuTrafficJam;
	private JMenuItem 			_runItem;
	private JMenuItem 			_oneTurnItem;
	private JMenuItem 			_resetItem;
	private JMenuItem 			_paramsItem;
	private ClassLoadMenu 		_classLoadMenu;
	
	protected Image 	_image1;
	protected Image 	_image2;
	protected int 		_nbCases;
	protected int[] 	_tableau;
	protected int 		_caseDX;
	protected int 		_caseDY;
	protected int 		_xInit;
	protected int 		_yInit;
	
	protected int 		_playingSpeed;
	
	protected TrafficJamPlayer 	_player;
	protected TrafficJamParameters _params;
	
	public int nbCases() { return _nbCases; }
	public int playingSpeed() { return _playingSpeed; }
	public void setPlayingSpeed(int sp) { if(sp >= 0 && sp < 5000) _playingSpeed = sp; }
	public /*final?*/ int[] tableau() { return _tableau; }

	public TrafficJamEngine() {
		super();
		
		
		_caseDX = 64+3;
		_caseDY = 64+3;
      
		_image1 = null;
		_image2 = null;
		
		_player = null;
		
		_playingSpeed = 200;
		
		initTableau(7);

	}
	
	public synchronized void initTableau(int nbc) {
		if(nbc < 2 || nbc > 15)
			return;
		nbc = (nbc/2)*2 + 1;			
		
		_nbCases = nbc;
		_tableau = new int[_nbCases];
		
		/*
		int largeur = (_caseDX)*_nbCases+20;
		if(_interface != null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			_interface.getFrame().setBounds((screenSize.width - largeur) / 2,
					(screenSize.height - (screenSize.height / 2)) / 2,
					largeur,
					screenSize.height / 2);
			_interface.getViewport().validate();
		}*/

		resetJeu();
	}
	
	public void initInterface(Interface i) {
		super.initInterface(i);
		
		// ajout du menu propre à LearnAG
		
		// En classe chargée dynamiquement, il est nécessaire de créer les menus à la volée et non en tête de classe !
		_menuTrafficJam = new JMenu("TrafficJam");
		_params = new TrafficJamParameters(this);

		_classLoadMenu = new ClassLoadMenu("Player", this, TrafficJamPlayer.class, _interface);
		_menuTrafficJam.add(_classLoadMenu.getMenu());

		_menuTrafficJam.addSeparator();
		
		_paramsItem = new JMenuItem("Parameters");
		_paramsItem.addActionListener(this);
		_menuTrafficJam.add(_paramsItem);
		
		_menuTrafficJam.addSeparator();
		
		_resetItem = new JMenuItem("Reset");
		_resetItem.addActionListener(this);
		_menuTrafficJam.add(_resetItem);

		_oneTurnItem = new JMenuItem("One Turn");
		_oneTurnItem.addActionListener(this);
		_menuTrafficJam.add(_oneTurnItem);
		
		_runItem = new JMenuItem("Run");
		_runItem.addActionListener(this);
		_menuTrafficJam.add(_runItem);
		
		_interface.getMenu().addMenu(_menuTrafficJam);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == _runItem) {
			joue();
			return;
		}
		if(event.getSource() == _oneTurnItem) {
			joueUnTour();
			return;
		}
		if(event.getSource() == _resetItem) {
			resetJeu();
			return;
		}
		if(event.getSource() == _paramsItem) {
			_params.getFrame().setVisible(true);
			return;
		}
		super.actionPerformed(event); // au début de la fonction plutôt non ?
	}
	
	public synchronized void terminate() {
		// supprimer les menus
		if(_player != null)
			_player.terminate(); // à ne pas oublier !
		
		_interface.getMenu().removeMenu(_menuTrafficJam);
	}
	
	public void classOpened(Object object, Object caller) {
		if(_player != null)
			_player.terminate(); // supprimer les menus spécifiques	
		_player = (TrafficJamPlayer)object; 
		_player.init(this); // ne pas oublier !
	}
	

	public void ecrire() {
		System.out.println("Vous êtes dans TrafficJam");
	}
	
    public synchronized void mouseClicked(int whichButton, int x, int y) {
		System.out.println("Mouse Clicked in TrafficJam : (" + x + ", " + y + ")");
		int xc = (x - _xInit) / _caseDX;
		int yc = (y - _yInit) / _caseDY;
		if(_player != null && !horsLimites(xc, yc))
			_player.cliqueCase(xc);
	}

	public BufferedImage draw(JViewport vp) {
		Dimension d = vp.getSize();
		if (d.width <= 0 || d.height <= 0) return null;
		
		_xInit = Math.round(0.5f * (d.width - _nbCases*_caseDX));
		_yInit = Math.round(0.5f * (d.height - _caseDY));
		if(_image1 == null) { // premier appel
	        _image1 = vp.getToolkit().getImage("img/image1.gif");
	        _image2 = vp.getToolkit().getImage("img/image2.gif");
	        try {
	            MediaTracker tracker = new MediaTracker(vp);
	            tracker.addImage(_image1, 0);
	            tracker.addImage(_image2, 0);
	            tracker.waitForAll();
	        }
	        catch ( Exception e ) {
	        	e.printStackTrace();
	        }
		}
		
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setColor(new Color(0,125,0));	    
		g.fillRect(0, 0, d.width, d.height);
		
		g.setColor(new Color(255, 255, 125));
		g.fillRect(_xInit, _yInit, _nbCases * _caseDX, _caseDY);
		g.setColor(new Color(0,0,0));
		g.setStroke(new BasicStroke(3));
		for (int i = 0; i < _nbCases; i++)
			g.drawRect(_xInit+ i * _caseDX, _yInit, _caseDX, _caseDY);
		
		for (int i = 0; i < _nbCases; i++) {
			if(_tableau[i] == 1)
				g.drawImage(_image1, _xInit + i * _caseDX, _yInit, vp);
			else if(_tableau[i] == -1)
				g.drawImage(_image2, _xInit + i * _caseDX, _yInit, vp);
		}

		if(solution()) {
			g.setColor(new Color(255, 0, 0));
			String s = "Victory !";
			g.scale(3., 3.);
			//g.setStroke(new BasicStroke(3));
			g.drawString(s, (d.width/2-s.length()*8)/3, (d.height/2 - 2*_caseDY)/3);
			System.out.println(s);
		}
		
		return image;
	}
	
	/********************************/
	/* Limite Graphique/Fonctionnel */
	/********************************/
	
	public synchronized void resetJeu() {
		for (int i = 0; i < _nbCases/2; i++) {
			_tableau[i] = 1;
			_tableau[_nbCases-1-i] = -1;
		}
		_tableau[_nbCases/2] = 0;
		
		if(_player != null)
			_player.resetJeu();

		if(_interface != null)
			_interface.redraw();
	}
	
	public synchronized boolean solution() {
		for (int i = 0; i < _nbCases/2; i++)
			if(_tableau[i] != -1 || _tableau[_nbCases-1-i] != 1)
				return false;
		
		return true;
	}

	protected synchronized boolean horsLimites(int xc, int yc) {
		return ( xc < 0 || xc >= _nbCases || yc < 0 || yc >= 1);
	}
	
	public synchronized boolean horsLimites(int[] tableau, int xc) {
		return ( xc < 0 || xc >= _nbCases);
	}
	
	protected synchronized void joueCase(int xc) {
		int j = joueCase(_tableau, xc);
		if(j == -2)
			System.out.println("Hors limites du tableau : " + xc);
		else if(j == -1)
			System.out.println("Coup illégal : " + xc);
		/*else
			System.out.println("Coup ok : " + xc);
			*/

		redraw();
	}
	
	public synchronized int joueCase(int[] tableau, int xc) {
		if (horsLimites(tableau, xc))
			return -2;
		
		int d = tableau[xc];
		if(d == 0)
			return -1;
		int dd = d+d;
		if(horsLimites(xc+d, 0)) 
			return -1;
		if(tableau[xc+d] == 0) {
			tableau[xc+d] = d;
			tableau[xc] = 0;
			return 1;
		}
		if(horsLimites(xc+dd, 0))
			return -1;
		if(tableau[xc+dd] == 0) {
			tableau[xc+dd] = d;
			tableau[xc] = 0;
			return 1;
		}
		
		return -1;
	}
	
	public void joueUnTour() {
		if(_player != null) {
			int c = _player.joue();
			joueCase(c);
		}
	}
	
	public void joue() {
		if(_player != null) {
			Thread worker = new Thread() {
				public void run() {
					int c;
					do {
						c = _player.joue();
						joueCase(c);
						try {
							if(_playingSpeed > 0)
								Thread.sleep(_playingSpeed);
						} catch (Exception e) {}
					} while(c != -1 && !solution());
				}
			};
			worker.start();
		}
	}
}
