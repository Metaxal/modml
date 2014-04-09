
public abstract class TicTacToeVirtualPlayer extends TicTacToePlayer {

	public TicTacToeVirtualPlayer() {
		super();
	}
	
	int cellValue(int c) {
		return _engine.cellValue(c);	
	}
	
	TicTacToeBoard copyBoard() {
		return _engine.copyBoard();
	}

	int nextPlayer(int player){
		return _engine.nextPlayer(player);
	}
}
