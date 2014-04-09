import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
//import java.io.*;
import java.awt.*;

/*import java.awt.event.*;
 import javax.swing.event.*;
 import java.beans.*;
 import java.awt.geom.*;*/

public class TicTacToeEngine extends ObjectEngine implements ClassOpener {
	final int PLAYER1 = 0;
	final int PLAYER2 = 1;
	

	private TicTacToeBoard _board;

	private TicTacToePlayer _player1;
	private TicTacToePlayer _player2;
	private int _currentPlayer;
	
	private JMenu _menuTicTacToe;

	private JMenuItem _playItem;

	private ClassLoadMenu _classLoadMenuP1;
	private ClassLoadMenu _classLoadMenuP2;

	public TicTacToeEngine() {
		super();
		reset();
	}
	
	TicTacToePlayer player(int p) {
		if(p == PLAYER1)
			return _player1;
		else if(p == PLAYER2)
			return _player2;
		else
			return null;
	}
	
	int nextPlayer(int p) {
		if(p == PLAYER1)
			return PLAYER2;
		else if(p == PLAYER2)
			return PLAYER1;
		else
			return -1;
	}

	private void reset() {
		_currentPlayer = -1;
		resetBoard();
	}

	public void resetBoard() {
		_board = new TicTacToeBoard();
	}

	public void initInterface(Interface i) {
		super.initInterface(i);

		// ajout du menu propre à TicTacToeEngine
		// En classe chargée dynamiquement, il est nécessaire de créer les menus
		// à la volée et non en tête de classe !
		_menuTicTacToe = new JMenu("TicTacToe");

		_playItem = new JMenuItem("Play");
		_playItem.addActionListener(this);
		_menuTicTacToe.add(_playItem);
		
		_classLoadMenuP1 = new ClassLoadMenu("Player 1", this, TicTacToePlayer.class, _interface);
		_menuTicTacToe.add(_classLoadMenuP1.getMenu());

		_classLoadMenuP2 = new ClassLoadMenu("Player 2", this, TicTacToePlayer.class, _interface);
		_menuTicTacToe.add(_classLoadMenuP2.getMenu());
		
		_interface.getMenu().add(_menuTicTacToe);
		_interface.getMenu().validate();
		_interface.getFrame().getContentPane().validate();
	}

	public void actionPerformed(ActionEvent event) {
		System.out.println("Menu TicTacToe item [" + event.getActionCommand()
				+ "] was pressed.");
		
		if(event.getSource() == _playItem) {
			joue();
			return;
		}
	}

	public void classOpened(Object object, Object caller) {
		if(caller == _classLoadMenuP1)
		{
			if(_player1 != null)
				_player1.terminate(); // supprimer les menus spécifiques	
			_player1 = (TicTacToePlayer)object; 
			_player1.init(this, PLAYER1); // ne pas oublier !
		}
		else
		{
			if(_player2 != null)
				_player2.terminate(); // supprimer les menus spécifiques	
			_player2 = (TicTacToePlayer)object; 
			_player2.init(this, PLAYER2); // ne pas oublier !
		}
		_interface.redraw();
	}

	public BufferedImage draw(JViewport vp) {
		Dimension d = vp.getSize();
		if (d.width <= 0 || d.height <= 0)
			return null;

		BufferedImage image = new BufferedImage(d.width, d.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();

		int x_init = (int) Math.round(0.1 * d.width);
		int y_init = (int) Math.round(0.1 * d.height);
		int width = (int) Math.round((0.8 * d.width) / 3);
		int height = (int) Math.round((0.8 * d.height) / 3);

		g.setColor(new Color(0, 125, 0));
		g.fillRect(0, 0, d.width, d.height);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				g.setColor(new Color(0, 0, 0));
				g.setStroke(new BasicStroke(5));
				g.drawRect(x_init + i * width, y_init + j * height, width,
						height);

				if (_board.isFirstPlayer((2 - j) * 3 + i)) {
					g.setColor(new Color(255, 40, 40));
					g.setStroke(new BasicStroke(5));
					g.drawLine(x_init + i * width
							+ (int) Math.round(width * 0.2), y_init + j
							* height + (int) Math.round(height * 0.2), x_init
							+ (i + 1) * width - (int) Math.round(width * 0.2),
							y_init + (j + 1) * height
									- (int) Math.round(height * 0.2));
					g.drawLine(x_init + i * width
							+ (int) Math.round(width * 0.2), y_init + (j + 1)
							* height - (int) Math.round(height * 0.2), x_init
							+ (i + 1) * width - (int) Math.round(width * 0.2),
							y_init + j * height
									+ (int) Math.round(height * 0.2));
				} else if (_board.isSecondPlayer((2 - j) * 3 + i)) {
					g.setColor(new Color(50, 40, 190));
					g.setStroke(new BasicStroke(5));
					g.drawOval(x_init + i * width
							+ (int) Math.round(width * 0.2), y_init + j
							* height + (int) Math.round(height * 0.2),
							(int) Math.round(width * 0.6), (int) Math
									.round(height * 0.6));
				}
			}
		// affichage des valeurs des différentes actions

		if (_board.endOfGame()) {
			String s = "";
			if (_board.firstPlayerWon()) {
				s = "Player 1 wins !";
			} else if (_board.secondPlayerWon()) {
				s = "Player 2 wins !";
			} else {
				s = "Draw game !";
			}
			g.setColor(new Color(255, 0, 0));
			g.setFont(new Font("Dialog", Font.BOLD, 28));
			int w = g.getFontMetrics().stringWidth(s);
			g.drawString(s, (int) Math.round(d.width / 2. - w / 2.), d.height
					- (int) Math.round(d.height * 0.04));
		}
		return image;
	}

	public int getCell(int x, int y) {
		Dimension d = _interface.getViewport().getSize();
		int x_init = (int) Math.round(0.1 * d.width);
		int y_init = (int) Math.round(0.1 * d.height);
		int width = (int) Math.round((0.8 * d.width) / 3);
		int height = (int) Math.round((0.8 * d.height) / 3);
		int j = (x - x_init) / width;
		int i = (y - y_init) / height;

		if (x < x_init || i > 2 || y < y_init || j > 2)
			return -1;
		return (2 - i) * 3 + j;
	}

	public void mouseClicked(int whichButton, int x, int y) {
		System.out.println("Mouse Clicked in ticTacToe : (" + x + ", " + y + ")");
		int c = getCell(x, y);
		if(c != -1 && _currentPlayer != -1)
			player(_currentPlayer).cliqueCase(c);
		//mClicked(x, y);
	}

	public void ecrire() {
		System.out.println("Vous êtes dans TicTacToeEngine");
	}

	public void terminate() {
		// supprimer les menus
		_interface.getMenu().remove(_menuTicTacToe);
		_interface.getMenu().validate();
		_interface.getMenu().repaint();
		_interface.getFrame().getContentPane().validate();
		_interface.getFrame().getContentPane().repaint();

	}
	
	public void joue() {
		reset();
		redraw();
		if(_player1 != null && _player2 != null) {
			Thread worker = new Thread() {
				public void run() {
					int c;
					_currentPlayer = PLAYER1;
					do {
						c = player(_currentPlayer).joue();
						System.out.println("Joueur "+_currentPlayer+" a joué case "+c);
						if(!_board.joueCase(c, _currentPlayer))
							continue;
						
						redraw();
						
						_currentPlayer = nextPlayer(_currentPlayer);
					} while(!_board.endOfGame());
				}
			};
			worker.start();
		}
		else
			System.out.println("Players not set!");
	}
	
	int cellValue(int c) {
		if(c < 0 || c > 9)
			return -2;
		return _board.cellValue(c);	
	}
	
	TicTacToeBoard copyBoard() 	{
		return (TicTacToeBoard)_board.clone();
	}
}
