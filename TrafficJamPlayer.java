
public abstract class TrafficJamPlayer {
	protected TrafficJamEngine _engine;

	public TrafficJamPlayer() {
		resetJeu();
	}
	
	public void init(TrafficJamEngine tje) {
		_engine = tje;
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
