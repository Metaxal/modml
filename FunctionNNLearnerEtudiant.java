import java.util.List;


/**
 * @author Laurent Orseau
 *
 */
public class FunctionNNLearnerEtudiant extends FunctionLearner {
	
	private NeuralNetworkEtudiant _net;
	
	public FunctionNNLearnerEtudiant() {
		super();
		
		nouveauReseau();
	}
	
	public void nouveauReseau() {
		_net = new NeuralNetworkEtudiant(1, nbNeurones(), 1, epsilon(), alpha()) ;
		// 1 entrée, N neurones cachés, 1 sortie
	}
	
	/*
	 * Apprentissage des points en fonction du réseau de neurones courant.
	 * Appelle la fonction update du RN une fois par exemple d'apprentissage.
	 * Il ne faut pas apprendre par coeur un point puis passer au deuxième, etc.,
	 * car dans ce cas on oublierait tout du point précédent, ou presque.
	 * Il faut donc fournir les exemples d'apprentissage l'un après l'autre, une seule fois,
	 * mais il faut ensuite répéter cette séquence n fois.
	 * @param liste : la liste des points, les exemples d'apprentissage 
	 */
	public void learn(List liste) {
		if(liste.size() == 0)
			return;
		
		System.out.println("Learning...");
		
		double[] input = new double[1];
		double[] output = new double[1];
		for(int i = 0; i < nbIterations() && !stop(); i++) {
			int n = i % liste.size();//(int)Math.floor(Math.random()*liste.size());
			FunctionEngine.Point p = (FunctionEngine.Point)liste.get(n);
			input[0] = p.x();
			output[0] = p.y();
			_net.update(input, output);
			
			if(i % _refreshSpeed == 0)
				_interface.redraw();
		}
		
		System.out.println("Learning finished...");
	}
	
	/*
	 * Et s'il reste du temps :
	 * Calcul de l'erreur quadratique moyenne EQM
	 */
	
	

	/*
	 * Inutilisé
	 */
	public void continueLearning() {
	}
	
	/*
	 * Renvoie la valeur de la fonction calculée par le réseau de neurones en un point
	 * Appelée par l'interface lors de l'affichage
	 * @param x : la valeur d'entrée de la fonction
	 * @return : la valeur de sortie 
	 */
	public double y(double x) {
		double[] input = new double[1];
		input[0] = x;
		return _net.result(input)[0];
	}
	
}
