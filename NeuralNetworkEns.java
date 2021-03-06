import java.util.Arrays;

/*
 * @author Pascal Garcia & Laurent Orseau
 * Class NeuralNetworkEns : fonction de base d'un perceptron multicouches (1 couche cachée)
 * avec rétropropagation et moment.
 */
public class NeuralNetworkEns {

    private double[]    _inputs; // le vecteur des activations d'entrée, y compris le biais
    private double[]    _hiddens; // le vecteur des activations des neurones cachés
    private double[]    _outputs; // le vecteur des activations des neurones de sortie
    private double[][] _inputHiddenWeights; // poids des entrées vers la couche cachée
    private double[][] _hiddenOutputWeights; // poids de la couche cachée vers la couche de sortie
    // pour la retropropagation :
    private double[][] _deltaHiddenOutputWeights; // les deltas des connexions cachés->sorties
    private double[][] _deltaInputHiddenWeights; // les delta des connexions entrées->cachés


    private int        _nbInput; // nombre d'entrées, y compris le biais
    private int        _nbHidden; // nombre de neurones cachés
    private int        _nbOutput; // nombre de sorties, nombre de neurones de sortie
    private double     _epsilon; // taux d'apprentissage
    private double     _alpha; // moment d'apprentissage, inertie
    private double     _lambda; // weight decay

    public double epsilon() { return _epsilon; }
    public void setEpsilon(double e) { _epsilon = e; }
    public double alpha() { return _alpha; }
    public void setAlpha(double a) { _alpha = a; }
    public double lambda() { return _lambda; }
    public void setLambda(double l) { _lambda = l; }

    /*
     * Constructeur : Création d'un nouveau réseau de neurones
     * @param nbInput : nombres d'entrées dont a besoin l'utilisateur. Ne tient pas compte du biais
     * @param nbHidden : nombre de neurones cachés
     * @param nbOutput : nombre de neurones en sortie / nombre de sorties
     * @param epsilon : facteur d'apprentissage
     * @param alpha : moment d'apprentissage / inertie
     */
    public NeuralNetworkEns(int nbInput, int nbHidden, int nbOutput, double epsilon, double alpha, double lambda) {
        _nbInput            = nbInput+1; // +1 pour le biais
        _nbHidden           = nbHidden+1; // +1 pour le biais
        _nbOutput           = nbOutput;
        _epsilon            = epsilon;
        _alpha              = alpha;
        _lambda             = lambda;

        // Création des tableaux :
        _inputs = new double[_nbInput];
        _hiddens = new double[_nbHidden];
        _outputs = new double[_nbOutput];
        _inputHiddenWeights   = new double[_nbInput][_nbHidden];
        _hiddenOutputWeights  = new double[_nbHidden][_nbOutput];
        _deltaInputHiddenWeights  = new double[_nbInput][_nbHidden];
        _deltaHiddenOutputWeights = new double[_nbHidden][_nbOutput];

        // Initialisation des poids à des valeurs aléatoires, voir :
        // Fernández-Redondo, Mercedes, and Carlos Hernández-Espinosa. "Weight initialization methods for multilayer feedforward." In ESANN, pp. 119-124. 2001.
        // Actually, initializing in [-8, 8] works pretty well because of the high values of the coordinates (but now it's normalized in [-1;1] so not useful anymore)
        for (int i = 0; i < _nbInput; i++)
            for (int j = 0; j < _nbHidden; j++)
                _inputHiddenWeights[i][j] = (Math.random()*2.-1.)*.05; // [-0.05; 0.05]

        for (int i = 0; i < _nbHidden; i++)
            for (int j = 0; j < _nbOutput; j++)
                _hiddenOutputWeights[i][j] = (Math.random()*2.-1.)*.05; // [-0.05; 0.05]
    }

    /*
     * Calcul en passe avant, des entrées vers les sorties, pour obtenir le résultat du réseau.
     * @param entry: le tableau des valeurs des entrées fournies par l'utilisateur. Ne tient pas compte du biais
     * @return le vecteur des sorties, mises à jour.
     */
    public double[] result(double[] inputs) {

        // on utilise _inputs comme tableau des entrées contenant en plus le biais
        for (int i = 0; i < _nbInput-1; i++) // _nbInput == inputs.length normalement !
            _inputs[i] = inputs[i];
        _inputs[_nbInput-1] = 1.; // biais sur la derniere case du tableau

        // maintenant, on ne raisonne plus que sur _inputs,  et pas inputs

        // Sommation pondérée des entrées pour chaque neurone caché :
        for (int i = 0; i < _nbHidden; i++) {
            _hiddens[i] = 0.;
            for (int j = 0; j < _nbInput; j++)
                _hiddens[i] += _inputs[j] * _inputHiddenWeights[j][i] ;
        }

        // calcul de la fonction d'activation de chaque neurone caché :
        for (int i = 0; i < _nbHidden; i++)
            _hiddens[i] = sigmoid(_hiddens[i]) ;
        _hiddens[0] = 1.; // on force le biais à 1 sur la première unité cachée

        // somme pondérée des activations des neurones cachés pour chaque sortie :
        for (int i = 0; i < _nbOutput; i++) {
            _outputs[i] = 0.;
            for (int j = 0; j < _nbHidden; j++)
                _outputs[i] += _hiddens[j] * _hiddenOutputWeights[j][i] ;
        }

        // activation linéaire, donc pas de fonction d'activation en sortie.

        return _outputs ;
    }

    /*
     * Mise à jour des poids : apprentissage
     * On calcule l'erreur commise sur chaque sortie,
     * que l'on répatie ensuite sur chaque neurone caché.
     * On modifie les poids en conséquence.
     * @param input : le vecteur des entrées (sans le biais)
     * @param output : le vecteur des sorties désirées par l'utilisateur
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
            
		//System.out.println("Output error:\n" + Arrays.deepToString(delta_exit).replaceAll("], ", "],").replaceAll(",", "\n "));

        // vecteur de l'erreur répartie sur les neurones cachés :
        double[] delta_hidden = new double[_nbHidden];

        // Calcul de l'erreur "commise" par chaque neurone caché, proportionnellement à son activation
        // et à sa contribution à l'erreur de chaque neurone de sortie
        for (int i = 0; i < _nbHidden ; i++) {
            double d = 0;
            for (int j = 0; j < _nbOutput; j++)
                d += _hiddenOutputWeights[i][j] * delta_exit[j] ;

            delta_hidden[i] = sigmoidDerivation(_hiddens[i]) * d ;
        }


        // Mise à jours des poids :

        // poids des neurones cachés vers les neurones de sortie
        // avec moment d'inertie sur les poids
        for (int i = 0; i < _nbHidden; i++)
            for (int j = 0; j < _nbOutput; j++) {
                _deltaHiddenOutputWeights[i][j] = _epsilon * delta_exit[j] * _hiddens[i] +
                    _alpha * _deltaHiddenOutputWeights[i][j] ;

                _hiddenOutputWeights[i][j] += _deltaHiddenOutputWeights[i][j] ;
            }

        // poids des entrées vers les neurones cachés
        // avec moment d'inertie sur les poids
        for (int i = 0; i < _nbInput; i++) {
            for (int j = 0; j < _nbHidden; j++) {
                _deltaInputHiddenWeights[i][j] = _epsilon * delta_hidden[j] * _inputs[i] +
                    _alpha * _deltaInputHiddenWeights[i][j] ;

                _inputHiddenWeights[i][j] *= 1. - _lambda; // Weight decay, penalize large weights.
                // Note: This works to prevent the large weights problem for the linear curve, 
                // but prevents the net from learning the Gaussian and Sinus curves...
                _inputHiddenWeights[i][j] += _deltaInputHiddenWeights[i][j];

            }
        }

    }

    /*
     * Fonction d'activation
     */
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /*
     * Fonction dérivée de la fonction d'activation
     */
    private double sigmoidDerivation(double x) {
        double s = sigmoid(x) ;
        return  s * (1. - s) ;
    }

    public void printWeights() {

        System.out.println("Input->Hidden:\n" + Arrays.deepToString(_inputHiddenWeights).replaceAll("], ", "],").replaceAll(",", "\n "));
        System.out.println("Hidden->Output:\n" + Arrays.deepToString(_hiddenOutputWeights).replaceAll("],", "],\n") + "\n");
    }
}
