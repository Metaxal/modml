
public class TrafficJamHumanPlayer extends TrafficJamPlayer {
	int _caseAJouer;

	public TrafficJamHumanPlayer() {
		super();
		
		_caseAJouer = -1;
	}
	
	public void terminate() {
	}
	
	public void cliqueCase(int c) {
		_caseAJouer = c;
		_engine.joueUnTour(); // on force à jouer le coup
	}
	
	public int joue() {
		return _caseAJouer;
	}
}
