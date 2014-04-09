import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JViewport;

public class TetrisEngine extends ObjectEngine {
	int[][] _tableau;
	
	private JMenu 				_menuTetris;
	private JMenuItem 			_runItem;
	
	protected int _score;
	protected int _niveau;
	protected int _nbLignesFaites;
	
	protected boolean _fin;

	protected int _nbCasesX;
	protected int _nbCasesY;
	protected int _caseDX;
	protected int _caseDY;
	
	protected int _nbFormes;
	protected int[][] _formes= { 
			{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0}, // vide
			{0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0}, // barre
			{0,0,0,0, 0,1,1,0, 0,0,1,1, 0,0,0,0}, // S inversé
			{0,0,0,0, 0,0,1,1, 0,1,1,0, 0,0,0,0}, // S
			{0,0,0,0, 0,1,1,0, 0,1,1,0, 0,0,0,0}, // carré
			{0,0,0,0, 1,1,1,0, 0,1,0,0, 0,0,0,0}, // T
			{0,0,0,0, 1,1,1,0, 0,0,1,0, 0,0,0,0}, // L inversé
			{0,0,0,0, 1,1,1,0, 1,0,0,0, 0,0,0,0}  // L
	};
	
	protected int[][] _forme;
	protected int[][] _formeSuivante;
	protected int _posX;
	protected int _posY;
	
	protected Color[] _couleurs;
	protected Color[] _couleurs2;

	public TetrisEngine() {
		super();
		
		_nbCasesX = 10;
		_nbCasesY = 20;
		
		_caseDX = 16;
		_caseDY = 16;
		
		_tableau = new int[_nbCasesX][_nbCasesY];
		
		_nbFormes = _formes.length;
		
		_forme = new int[4][4];
		_formeSuivante = new int[4][4];
		ajoutPiece();
	
		_couleurs = new Color[_nbFormes];
		_couleurs[0] = new Color(0,0,0);    // vide
		_couleurs[1] = new Color(128,0,0);  // barre
		_couleurs[2] = new Color(0,128,0);  // S inversé
		_couleurs[3] = new Color(0,0,128);  // S
		_couleurs[4] = new Color(128,128,0);// carré
		_couleurs[5] = new Color(128,0,128);// T
		_couleurs[6] = new Color(0,128,128);// L inversé
		_couleurs[7] = new Color(80,80,200);// L

		_couleurs2 = new Color[_nbFormes];
		_couleurs2[0] = new Color(0,0,0);    // vide
		_couleurs2[1] = new Color(175,0,0);  // barre
		_couleurs2[2] = new Color(0,175,0);  // S inversé
		_couleurs2[3] = new Color(0,0,175);  // S
		_couleurs2[4] = new Color(175,175,0);// carré
		_couleurs2[5] = new Color(150,0,175);// T
		_couleurs2[6] = new Color(0,175,175);// L inversé
		_couleurs2[7] = new Color(100,100,220);// L
	}
	
	public void initInterface(Interface i) {
		super.initInterface(i);
		
		// ajout du menu propre à LearnAG
		
		// En classe chargée dynamiquement, il est nécessaire de créer les menus à la volée et non en tête de classe !
		_menuTetris = new JMenu("Tetris");

		_runItem = new JMenuItem("New Game");
		_runItem.addActionListener(this);
		_menuTetris.add(_runItem);
		
		_interface.getMenu().addMenu(_menuTetris);
	}
	
	public synchronized void terminate() {
		// supprimer les menus
		_interface.getMenu().removeMenu(_menuTetris);
		_fin = true;
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == _runItem) {
			joue();
			return;
		}
	}
	
	
	public void affCase(Graphics2D g, int x, int y, int coul) {
		g.setColor(_couleurs[coul]);
		g.fillRect(x, y, _caseDX, _caseDY);
		
		g.setColor(_couleurs2[coul]);
		g.fillRect(x+2, y+2, _caseDX-4, _caseDY-4);
	}

	public BufferedImage draw(JViewport vp) {
		Dimension d = vp.getSize();
		if (d.width <= 0 || d.height <= 0) return null;
		
		BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		int xDeb = (int)Math.round(0.5 * (d.width-_caseDX*_nbCasesX));
		int yDeb = (int)Math.round(0.5 * (d.height-_caseDY*_nbCasesY));
		int xFin = (int)Math.round(0.5 * (d.width+_caseDX*_nbCasesX));
		int yFin = (int)Math.round(0.5 * (d.height+_caseDY*_nbCasesY));
		
		g.setColor(new Color(0,125,0));	    
		g.fillRect(0, 0, d.width, d.height);
		
		g.setColor(new Color(200,200,0));	    
		g.fillRect(xDeb-2, yDeb, _nbCasesX * _caseDX+4, _nbCasesY * _caseDY+4); // zone de tableau
		g.fillRect(xFin + 2* _caseDX-2, yFin -7*_caseDY-2, 4*_caseDX+4, 4*_caseDY+4); // zone de forme suivante
		g.setColor(new Color(0,0,0));	    
		g.fillRect(xDeb, yDeb, _nbCasesX * _caseDX, _nbCasesY * _caseDY); // zone de tableau
		g.fillRect(xFin + 2* _caseDX, yFin -7*_caseDY, 4*_caseDX, 4*_caseDY); // zone de forme suivante
		
		//g.setStroke(new BasicStroke(1));
		// on affiche le tableau
		for (int i = 0; i < _nbCasesX; i++)
			for (int j = 0; j < _nbCasesY; j++) {
				int c = _tableau[i][j];
				if(c != 0)
					affCase(g, xDeb + i * _caseDX, yDeb + j * _caseDY, c);
			}
		// on affiche la forme 
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				int c = _forme[i][j];
				if(c != 0)
					affCase(g, xDeb + (_posX+i) * _caseDX, yDeb + (_posY+j) * _caseDY, c);

				c = _formeSuivante[i][j];
				if(c != 0)
					affCase(g, xFin + (2+i) * _caseDX, yFin + (-7+j) * _caseDY, c);
			}

		g.setColor(new Color(192,192,192));	    
		String s;
		g.setFont(new Font("Dialog",Font.BOLD,16));
		int h = g.getFontMetrics().getHeight();		
		s = "Score : " + _score;
		g.drawString(s, xFin+2*_caseDX, yDeb+50);
		s = "Lines : " + _nbLignesFaites;
		g.drawString(s, xFin+2*_caseDX, yDeb+50+h*2);
		s = "Level : " + _niveau;
		g.drawString(s, xFin+2*_caseDX, yDeb+50+h*4);

		if(_fin) {
			s = "Game Over";
			g.setFont(new Font("Dialog",Font.BOLD,42));
			int w = g.getFontMetrics().stringWidth(s);
			g.setColor(new Color(255,164,0));
			g.drawString(s, d.width/2-w/2+1, d.height/2-1);
			g.setColor(new Color(255,0,0));
			g.drawString(s, d.width/2-w/2, d.height/2);
		}

		return image;
	}
	
	public void joue() {
		Thread worker = new Thread() {
			public synchronized void run() {
				try {
					_score = 0;
					_niveau = 1;
					_nbLignesFaites = 0;
					initTableau();
					boolean descendu = false;
					_fin = false;
					ajoutPiece();
					for(int n=0; !_fin; n++) {
						descendu = descendre();
						if(!descendu) 
							_fin = ajoutPiece();
						
						_interface.redraw();
						
						Thread.sleep(300-_niveau*20);
					}
					System.out.println("Game Over!\nScore : "+_score);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		worker.start();
	}
	
	public boolean dansTableau(int x, int y) {
		return x >= 0 && x < _nbCasesX && y >= 0 && y < _nbCasesY; 
	}
	
	// teste si l'emplacement est valide
	// teste aussi si on a pas dépassé les limites
	public boolean collision(int[][] forme, int x, int y) {
		
		for(int i=0; i < 4; i++)
			for(int j=0; j < 4; j++)
				if(forme[i][j] != 0)
					if(!dansTableau(x+i, y+j))
						return true;
					else if(_tableau[x+i][y+j] != 0)
						return true;

		return false;
	}
	
	public synchronized boolean ajoutPiece() {
		int f = (int)(Math.floor(Math.random()*(_nbFormes-1)))+1;
		for(int i=0; i < 4; i++)
			for(int j=0; j < 4; j++) {
				_forme[i][j] = _formeSuivante[i][j];
				_formeSuivante[i][j] = _formes[f][i+j*4]*f; // *f pour la couleur
			}
		
		_posX = 3;
		_posY = 0;

		return collision(_forme, _posX, _posY);
	}
	
	public synchronized boolean descendre() {
		if(collision(_forme, _posX, _posY+1)) {
			for(int i=0; i < 4; i++)
				for(int j=0; j < 4; j++)
					if(dansTableau(i+_posX, j+_posY) && _forme[i][j] != 0)
						_tableau[i+_posX][j+_posY] = _forme[i][j];
			virerLignes();
			return false;
		}
		
		_posY++;
		return true;
	}
	
	public synchronized void virerLignes() {
		int nbl = 0;
		
		for(int j=0; j < 4 && _posY+j < _nbCasesY; j++) { // attention à bien commencer par la fin
			boolean avirer = true;
			for(int i = 0; i < _nbCasesX; i++)
				if(_tableau[i][_posY+j] == 0) {
					avirer = false;
					break;
				}

			// on supprime
			if(avirer) {
				nbl++;
				for(int k=_posY+j; k > 0; k--)
					for(int i = 0; i < _nbCasesX; i++)
						_tableau[i][k] = _tableau[i][k-1];
			}
		}
		System.out.println("nblignes = " + nbl);
		
		_nbLignesFaites += nbl;
		_score += nbl*nbl*_niveau*10;
		_niveau = _nbLignesFaites/10 +1;
	}
	
	public synchronized int[][] tourne(int[][] forme) {
		int n = forme.length; // censé être carré !
		int[][] fo = new int[n][n];
		
		for(int i=0; i < n; i++)
			for(int j=0; j < n; j++)
				fo[i][j] = forme[j][n-1-i];
				
		return fo;
	}
	
	public synchronized void keyTyped(char c) {
		if(_fin)
			return;
		if(c == '4') {
			if(!collision(_forme, _posX-1, _posY)) {
				_posX--;
				_interface.redraw();
			}
			return;
		}
		if(c == '6') {
			if(!collision(_forme, _posX+1, _posY)) {
				_posX++;
				_interface.redraw();
			}
			return;
		}
		if(c == '5') {
			while(descendre()) 
				;
			_fin = ajoutPiece(); // gerer la fin ??
			_interface.redraw();
			return;
		}
		if(c == '8') {
			int[][] forme = tourne(_forme);
			if(!collision(forme, _posX, _posY)) {
				_forme = forme;
				_interface.redraw();
			}
			return;
		}
	}
	
	public boolean stop() {
		return false;
	}
	
	protected void initTableau() {
		for (int i = 0; i < _nbCasesX; i++)
			for (int j = 0; j < _nbCasesY; j++)
				_tableau[i][j] = 0;
	}
}
