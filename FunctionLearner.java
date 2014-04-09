import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lorseau
 *
 */
public abstract class FunctionLearner implements ActionListener {
	private FunctionEngine 		_engine;
	protected Interface 		_interface;

	private JMenu 				_menuLearn;
	private JMenuItem 			_resetItem;
	private JMenuItem 			_startItem;
	private JMenuItem 			_stopItem;
	private JMenuItem 			_paramsItem;
	private NNParameters		_NNParameters;

	private boolean 			_stop;
	protected int				_nbNeurones;
	protected double 			_epsilon;
	protected double			_alpha;
	protected int				_refreshSpeed;
	protected int				_nbIterations;

	public Interface getInterface() 	{ return _interface; }
	public int nbNeurones() 			{ return _nbNeurones; }
	public double epsilon() 			{ return _epsilon; }
	public void setEpsilon(double e) 	{ if(e >= 0. && e < 10.) _epsilon = e; }
	public double alpha() 				{ return _alpha; }
	public void setAlpha(double a) 		{ if(a >= 0. && a < 1.) _alpha = a; }
	public void setRefreshSpeed(int sp) { if(sp >= 0) _refreshSpeed = sp; }
	public int refreshSpeed() 			{ return _refreshSpeed; }
	public boolean stop() 				{ return _stop; }
	public int nbIterations() 			{ return _nbIterations; }
	public void setNbIterations(int n) 	{ _nbIterations = n; }

	public FunctionLearner() {
		super();

		_stop = true;
		_nbIterations = 50000;
		_nbNeurones = 5;
		_epsilon = 0.01;
		_alpha = 0.9;
		_refreshSpeed = 2000;
	}

	public void init(FunctionEngine engine) {
		_engine = engine;
		_interface = _engine.getInterface();

		_NNParameters = new NNParameters(this);

		_menuLearn = new JMenu("LearnNN");
		_menuLearn.setMnemonic('L');

		_paramsItem = new JMenuItem("Parameters");
		_paramsItem.addActionListener(this);
		_menuLearn.add(_paramsItem);

		_menuLearn.addSeparator();

		_resetItem = new JMenuItem("Reset");
		_resetItem.addActionListener(this);
		_menuLearn.add(_resetItem);

		_startItem = new JMenuItem("Learn/Continue");
		_startItem.addActionListener(this);
		_menuLearn.add(_startItem);

		_stopItem = new JMenuItem("Stop");
		_stopItem.addActionListener(this);
		_menuLearn.add(_stopItem);

		// ajout des menus d'apprentissage.
		_interface.getMenu().addMenu(_menuLearn);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == _resetItem){
			reset();
			_interface.redraw();
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
			_NNParameters.getFrame().setVisible(true);
			return;
		}
	}

	public final void terminate() {
		_interface.getMenu().removeMenu(_menuLearn);
	}

	public void setNbNeurones(int n) {
		if(n > 0 && n < 1000 && n != _nbNeurones) {
			_nbNeurones = n;
		}
	}

	public void learn() {
		Thread worker = new Thread() {
			public void run() {
				_stop = false;
				learn(_engine.getPoints());
				_stop = true;
			}
		};
		/*Thread worker2 = new Thread() {
			public void run() {
				while(!_stop)
					try {
						_interface.redraw();
						Thread.sleep(_refreshSpeed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		};
		worker2.start();*/
		worker.start();
	}

	public abstract void reset();

	public abstract void learn(List liste);

	public abstract double y(double x);

	public abstract void nouveauReseau();

}
