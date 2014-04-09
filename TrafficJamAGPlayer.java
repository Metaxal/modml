import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class TrafficJamAGPlayer extends TrafficJamPlayer implements ActionListener {
	protected Interface 		_interface;

	private JMenu 				_menuLearn;
	private JMenuItem 			_continueItem;
	private JMenuItem 			_startItem; // et un autre continue learning, sans continue
	private JMenuItem 			_stopItem;
	private JMenuItem 			_paramsItem;
	private AGParameters		_AGParameters;

	private int 				_nbGenerations;
	private int 				_nbIndividus;
	private float				_tauxSelection;
	private float 				_tauxMutation;
	private float				_tauxCroisement;
	private boolean 			_stop;

	public boolean stop() { return _stop; }
	public int nbGenerations() { return _nbGenerations; }
	public void setNbGenerations(int n) { if(n >= 0) _nbGenerations = n; }
	public int nbIndividus() { return _nbIndividus; }
	public void setNbIndividus(int n) { if(n > 0 && n < 1000) _nbIndividus = n; }
	public float tauxSelection() { return _tauxSelection; }
	public void setTauxSelection(float s) { if(s >= 0.f && s <= 1.f) _tauxSelection = s; }
	public float tauxMutation() { return _tauxMutation; }
	public void setTauxMutation(float m) { if(m >= 0.f && m <= 1.f) _tauxMutation = m; }
	public float tauxCroisement() { return _tauxCroisement; }
	public void setTauxCroisement(float c) { if(c >= 0.f && c <= 1.f) _tauxCroisement = c; }

	public int nbCases() { return _engine.nbCases(); }
	public int joueCase(int[] tableau, int c) { return _engine.joueCase(tableau, c); }
	public int[] copieTableauJeu() { return (int[])_engine.tableau().clone(); }

	public TrafficJamAGPlayer() {
		super();

		_nbGenerations = 100;
		_nbIndividus = 20;
		_tauxSelection = 0.9f;
		_tauxMutation = 0.1f;
		_tauxCroisement = 0.2f;
		_stop = true;
	}

	public void init(TrafficJamEngine tje) {
		super.init(tje);
		_AGParameters = new AGParameters(this);

		_interface = _engine.getInterface();

		_menuLearn = new JMenu("LearnAG");
		_menuLearn.setMnemonic('L');

		_paramsItem = new JMenuItem("Parameters");
		_paramsItem.addActionListener(this);
		_menuLearn.add(_paramsItem);

		_menuLearn.addSeparator();

		_startItem = new JMenuItem("Start");
		_startItem.addActionListener(this);
		_menuLearn.add(_startItem);

		_continueItem = new JMenuItem("Continue");
		_continueItem.addActionListener(this);
		_menuLearn.add(_continueItem);

		_stopItem = new JMenuItem("Stop");
		_stopItem.addActionListener(this);
		_menuLearn.add(_stopItem);

		// ajout des menus d'apprentissage.
		_interface.getMenu().addMenu(_menuLearn);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == _continueItem){
			continueLearning();
			return;
		}
		if(event.getSource() == _startItem){
			learn();
			return;
		}
		if(event.getSource() == _stopItem){
			_stop = true;
			return;
		}
		if(event.getSource() == _paramsItem) {
			_AGParameters.getFrame().setVisible(true);
			return;
		}
	}

	public final void terminate() {
		_interface.getMenu().removeMenu(_menuLearn);
	}

	public int joue() {
		return -1;//(int)Math.round( Math.random() * _engine.nbCases() );
	}

	public final void continueLearning() {
		Thread worker = new Thread() {
			public void run() {
				_stop = false;
				learnAG();
				/*for (int i = 0; i < 10; i++) {
					System.out.println("Start learning by auto-play");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {}
				}*/
			}
		};
		worker.start();

		_stop = true;
	}

	public final void learn() {

		Thread worker = new Thread() {
			public void run() {
				_stop = false;
				initLearning();
				learnAG();
				/*for (int i = 0; i < 10; i++) {
					System.out.println("Start learning by auto-play");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {}
				}*/
			}
		};
		worker.start();

		_stop = true;
	}

	public void initLearning() {

	}

	public void learnAG() {
		System.out.println("AGPlayer.LearnAG...");
	}
}
