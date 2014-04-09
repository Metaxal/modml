import java.* ;

/**
 * Implementation of the Tic Tac Toe board. The board is represented by an int array (_board).
 * The value of the left most bottom most cell of the board is the value (EMPTY, PLAYER1, PLAYER2) 
 * of _board[0], ...:
 * <PRE>
 * +-+-+-+
 * |6|7|8|
 * +-+-+-+
 * |3|4|5|
 * +-+-+-+
 * |0|1|2|
 * +-+-+-+
 * </PRE>
 *
 * @author Pascal Garcia
 */

public class TicTacToeBoard implements Cloneable {
	private final int PLAYER1 = 0;
	private final int PLAYER2 = 1;
	private final int EMPTY = -1;
	private final int DRAW = -1;
	private final int PLAYING = -2;
	
	
	private int[] _board;
	
	/**
	 * Create an empty board
	 *
	 * @author Pascal Garcia
	 */    
	public TicTacToeBoard() {
		_board = new int[9];
		for (int i = 0; i < _board.length; i++) {
			_board[i] = EMPTY;
		}
	}
	
	/**
	 * Did the first player win ?
	 *
	 * @author Pascal Garcia
	 * @return true if first player won, false otherwise
	 */    
	public boolean firstPlayerWon() {
		boolean res = false;
		res = res || (_board[0] == PLAYER1 && _board[1] == PLAYER1 && _board[2] == PLAYER1);
		res = res || (_board[3] == PLAYER1 && _board[4] == PLAYER1 && _board[5] == PLAYER1); 
		res = res || (_board[6] == PLAYER1 && _board[7] == PLAYER1 && _board[8] == PLAYER1); 
		res = res || (_board[0] == PLAYER1 && _board[3] == PLAYER1 && _board[6] == PLAYER1); 
		res = res || (_board[1] == PLAYER1 && _board[4] == PLAYER1 && _board[7] == PLAYER1); 
		res = res || (_board[2] == PLAYER1 && _board[5] == PLAYER1 && _board[8] == PLAYER1); 
		res = res || (_board[0] == PLAYER1 && _board[4] == PLAYER1 && _board[8] == PLAYER1); 
		res = res || (_board[2] == PLAYER1 && _board[4] == PLAYER1 && _board[6] == PLAYER1); 
		return res;
	}
	
	/**
	 * Did the second player win ?
	 *
	 * @author Pascal Garcia
	 * @return true if second player won, false otherwise
	 */    
	public boolean secondPlayerWon() {
		boolean res = false;
		res = res || (_board[0] == PLAYER2 && _board[1] == PLAYER2 && _board[2] == PLAYER2); 
		res = res || (_board[3] == PLAYER2 && _board[4] == PLAYER2 && _board[5] == PLAYER2); 
		res = res || (_board[6] == PLAYER2 && _board[7] == PLAYER2 && _board[8] == PLAYER2); 
		res = res || (_board[0] == PLAYER2 && _board[3] == PLAYER2 && _board[6] == PLAYER2); 
		res = res || (_board[1] == PLAYER2 && _board[4] == PLAYER2 && _board[7] == PLAYER2); 
		res = res || (_board[2] == PLAYER2 && _board[5] == PLAYER2 && _board[8] == PLAYER2); 
		res = res || (_board[0] == PLAYER2 && _board[4] == PLAYER2 && _board[8] == PLAYER2); 
		res = res || (_board[2] == PLAYER2 && _board[4] == PLAYER2 && _board[6] == PLAYER2); 
		return res;
	}
	
	/**
	 * Is it a draw game ?
	 *
	 * @author Pascal Garcia
	 * @return true if it is a draw game, false otherwise
	 */    
	public boolean drawGame() {
		for (int i = 0; i < _board.length; i++) {
			if (_board[i] == EMPTY) return false;
		}
		return true;	
	}
	
	/**
	 * Is the game finished ?
	 *
	 * @author Pascal Garcia
	 * @return true if the game is finished, false otherwise
	 */    
	public boolean endOfGame() {
		return firstPlayerWon() || secondPlayerWon()|| drawGame();
	}
	
	public int result() {
		if(firstPlayerWon())
			return PLAYER1;
		if(secondPlayerWon())
			return PLAYER2;
		if(drawGame())
			return DRAW;
		return PLAYING;
	}
	
	public boolean isFirstPlayer(int i) {
		return _board[i] == PLAYER1;
	}
	
	public boolean isSecondPlayer(int i) {
		return _board[i] == PLAYER2;
	}
	
	public boolean isEmpty(int i) {
		return _board[i] == EMPTY;
	}
	
	public void setFirstPlayer(int i) {
		_board[i] = PLAYER1;
	}
	
	public void setSecondPlayer(int i) {
		_board[i] = PLAYER2;
	}
	
	public void setEmpty(int i) {
		_board[i] = EMPTY;
	}
	
	public int cellValue(int i) {
		return _board[i];
	}
	
	public int setCellValue(int i, int v) {
		return _board[i] = v;
	}
	
	public boolean joueCase(int c, int joueur)
	{
		if(c < 0 || c > 8)
			return false;
		
		if(_board[c] != EMPTY)
			return false;

		_board[c] = joueur;
		return true;
	}
	
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof TicTacToeBoard)) {
			TicTacToeBoard b = (TicTacToeBoard)o;
			if (_board.length != b._board.length) return false;
			for (int i = 0; i < _board.length; i++) {
				if (b.cellValue(i) != _board[i]) return false;
			}
			return true;
		}
		return false;
	}
	
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
			((TicTacToeBoard) o)._board = new int[_board.length];
			for (int i = 0; i < _board.length; i++) {
				((TicTacToeBoard) o).setCellValue(i, _board[i]);
			}
		} catch (CloneNotSupportedException _) {}
		return o;
	}
	
	public int[] getPossibleMoves() {
		int nb = 0;	
		for (int i = 0;  i < _board.length; i++) {
			if (_board[i] == EMPTY) nb++;
		}
		int[] res = new int[nb];
		nb = 0;	
		for (int i = 0;  i < _board.length; i++) {
			if (_board[i] == EMPTY) {
				res[nb] = i;
				nb++;
			}
		}
		return res;
	}
}
