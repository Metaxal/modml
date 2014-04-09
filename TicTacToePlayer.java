
public abstract class TicTacToePlayer {
	protected TicTacToeEngine _engine;
	protected int _mePlayer;

	public TicTacToePlayer() {
		resetJeu();
	}
	
	public int mePlayer() { return _mePlayer; }
	
	public void init(TicTacToeEngine ttte, int mePlayer) {
		_engine = ttte;
		_mePlayer = mePlayer;
	}
	
	public void terminate() {
	}

	public void cliqueCase(int c) {
	}
	
	public void resetJeu() {
		
	}
	
	public int joue() {
		// doit retourner la case à jouer
		return -1;
	}

}
