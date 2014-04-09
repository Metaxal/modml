
/*
 * @author Pascal Garcia & Laurent Orseau
 * Class NeuralNetwork : fonction de base d'un perceptron multicouches (1 couche cach�e)
 * avec r�tropropagation et moment.
 */
public class NeuralNetwork {
	
	private double[]	_inputs; // le vecteur des activations d'entr�e, y compris le biais
	private double[]	_hiddens; // le vecteur des activations des neurones cach�s
	private double[]	_outputs; // le vecteur des activations des neurones de sortie
	private double[][] _inputHiddenWeights; // poids des entr�es vers la couche cach�e
	private double[][] _hiddenOutputWeights; // poids de la couche cach�e vers la couche de sortie
	// pour la retropropagation :
	private double[][] _deltaHiddenOutputWeights; // les deltas des connexions cach�s->sorties
	private double[][] _deltaInputHiddenWeights; // les delta des connexions entr�es->cach�s
	
	
	private int        _nbInput; // nombre d'entr�es, y compris le biais
	private int        _nbHidden; // nombre de neurones cach�s
	private int        _nbOutput; // nombre de sorties, nombre de neurones de sortie
	private double     _epsilon; // taux d'apprentissage
	private double     _alpha; // moment d'apprentissage, inertie
	
	public double epsilon() { return _epsilon; }
	public void setEpsilon(double e) { _epsilon = e; }  
	public double alpha() { return _alpha; }
	public void setAlpha(double a) { _alpha = a; }
	
	/*
	 * Constructeur : Cr�ation d'un nouveau r�seau de neurones
	 * @param nbInput : nombres d'entr�es dont a besoin l'utilisateur. Ne tient pas compte du biais
	 * @param nbHidden : nombre de neurones cach�s
	 * @param nbOutput : nombre de neurones en sortie / nombre de sorties
	 * @param epsilon : facteur d'apprentissage
	 * @param alpha : moment d'apprentissage / inertie
	 */
	public NeuralNetwork(int nbInput, int nbHidden, int nbOutput, double epsilon, double alpha) {
		_nbInput            = nbInput+1; // +1 pour le biais
		_nbHidden           = nbHidden;
		_nbOutput           = nbOutput;
		_epsilon            = epsilon;
		_alpha              = alpha;
		
		// Cr�ation des tableaux :
		_inputs = new double[_nbInput];
		_hiddens = new double[_nbHidden];
		_outputs = new double[_nbOutput];
		_inputHiddenWeights   = new double[_nbInput][_nbHidden];
		_hiddenOutputWeights  = new double[_nbHidden][_nbOutput];
		_deltaInputHiddenWeights  = new double[_nbInput][_nbHidden];
		_deltaHiddenOutputWeights = new double[_nbHidden][_nbOutput];
		
		// Initialisation des poids � des valeurs al�atoires :
		for (int i = 0; i < _nbInput; i++)
			for (int j = 0; j < _nbHidden; j++)
				_inputHiddenWeights[i][j] = (Math.random()*2.-1.)*8.; // *8. -> meilleure r�partition initiale pour des valeurs entre -1 et 1
		
		for (int i = 0; i < _nbHidden; i++)
			for (int j = 0; j < _nbOutput; j++)
				_hiddenOutputWeights[i][j] = (Math.random()*2.-1.);
	}
	
	/*
	 * Calcul en passe avant, des entr�es vers les sorties, pour obtenir le r�sultat du r�seau. 
	 * @param entry: le tableau des valeurs des entr�es fournies par l'utilisateur. Ne tient pas compte du biais
	 * @return le vecteur des sorties, mises � jour.
	 */
	public double[] result(double[] inputs) {
		
		// on utilise _inputs comme tableau des entr�es contenant en plus le biais
		_inputs[_nbInput-1] = 1.; // biais sur la derniere case du tableau
		for (int i = 0; i < _nbInput-1; i++) // _nbInput == inputs.length normalement !
			_inputs[i] = inputs[i];
		
		// maintenant, on ne raisonne plus que sur _inputs,  et pas inputs
		
		// Sommation pond�r�e des entr�es pour chaque neurone cach� :
		for (int i = 0; i < _nbHidden; i++) {
			_hiddens[i] = 0.;
			for (int j = 0; j < _nbInput; j++)
				_hiddens[i] += _inputs[j] * _inputHiddenWeights[j][i] ;
		}
		
		// calcul de la fonction d'activation de chaque neurone cach� :
		for (int i = 0; i < _nbHidden; i++)
			_hiddens[i] = sigmoid(_hiddens[i]) ;
		
		// somme pond�r�e des activations des neurones cach�s pour chaque sortie : 
		for (int i = 0; i < _nbOutput; i++) {
			_outputs[i] = 0.;
			for (int j = 0; j < _nbHidden; j++)
				_outputs[i] += _hiddens[j] * _hiddenOutputWeights[j][i] ; 
		}
		
		// activation lin�aire, donc pas de fonction d'activation en sortie.
		
		return _outputs ;
	}
	
	/*
	 * Mise � jour des poids : apprentissage
	 * On calcule l'erreur commise sur chaque sortie,
	 * que l'on r�patie ensuite sur chaque neurone cach�.
	 * On modifie les poids en cons�quence.
	 * @param input : le vecteur des entr�es (sans le biais)
	 * @param output : le vecteur des sorties d�sir�es par l'utilisateur
	 */
	public void update(double[] input, double[] output) {
		
		// Calcul des valeurs d'activation :
		result(input);
		
		// maintenant on n'utilise plus que _input et pas input.

		// vecteur l'erreur commise sur chaque sortie :
		double[] delta_exit = new double[_nbOutput];
		
		// Calcul de l'erreur commise par chaque neurone de sortie :
		for (int i = 0; i < _nbOutput; i++)
			delta_exit[i] = (output[i] - _outputs[i]);
		
		// vecteur de l'erreur r�partie sur les neurones cach�s :
		double[] delta_hidden = new double[_nbHidden];

		// Calcul de l'erreur "commise" par chaque neurone cach�, proportionnellement � son activation
		// et � sa contribution � l'erreur de chaque neurone de sortie
		for (int i = 0; i < _nbHidden ; i++) {
			double d = 0;
			for (int j = 0; j < _nbOutput; j++)
				d += _hiddenOutputWeights[i][j] * delta_exit[j] ;

			delta_hidden[i] = sigmoidDerivation(_hiddens[i]) * d ;
		}
		
		
		// Mise � jours des poids :
		
		// poids des neurones cach�s vers les neurones de sortie
		// avec moment d'inertie sur les poids
		for (int i = 0; i < _nbHidden; i++)
			for (int j = 0; j < _nbOutput; j++) {
				_deltaHiddenOutputWeights[i][j] = _epsilon * delta_exit[j] * _hiddens[i] + 
					_alpha * _deltaHiddenOutputWeights[i][j] ;
				
				_hiddenOutputWeights[i][j] += _deltaHiddenOutputWeights[i][j] ;
			}
		
		// poids des entr�es vers les neurones cach�s 
		// avec moment d'inertie sur les poids
		for (int i = 0; i < _nbInput; i++) {
			for (int j = 0; j < _nbHidden; j++) {
				_deltaInputHiddenWeights[i][j] = _epsilon * delta_hidden[j] * _inputs[i] +
					_alpha * _deltaInputHiddenWeights[i][j] ;
				
				_inputHiddenWeights[i][j] += _deltaInputHiddenWeights[i][j] ;
				
			}
		}
		
	}
	
	/*
	 * Fonction d'activation
	 */
	private double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x)) ;
	}
	
	/*
	 * Fonction d�riv�e de la fonction d'activation 
	 */
	private double sigmoidDerivation(double x) {
		double s = sigmoid(x) ;
		return  s * (1. - s) ;
	}
}
