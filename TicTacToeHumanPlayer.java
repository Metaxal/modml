
public class TicTacToeHumanPlayer extends TicTacToePlayer {
	int _caseAJouer;

	public TicTacToeHumanPlayer() {
		super();
		
		_caseAJouer = -1;
	}
	
	public void terminate() {
	}
	
	public void cliqueCase(int c) {
		_caseAJouer = c;
	}
	
	public int joue() {
		_caseAJouer = -1;

		while(_caseAJouer == -1) ; // on attend d'avoir une case à jouer (attentions aux threads !)

		return _caseAJouer;
	}
}
