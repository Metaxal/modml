
public class TrafficJamAGIndividuEtudiant implements Comparable {
	
	/*
	 * _player va nous permettre d'utiliser les fonctions :
	 * 	_player.nbCases() : nombre de cases dans le tableau de jeu.
	 *  _player.joueCoup(int[] tableauJeu, indice) : joue le coup indice dans le tableau de jeu
	 *  	et renvoie 1 si le coup �tait valide, -2 si hors limite du tableau, 
	 *  	ou -1 si le coup est invalide.
	 */
	protected TrafficJamAGPlayer _player;
	protected double _fitness; // la valeur de l'individu, calcul�e par la fonction d'�valuation
	
	// A FAIRE : ajouter ici les attibuts n�cessaires
	
	public double fitness() { return _fitness; } 

	/*
	 * Constructeur
	 * r�cup�re le joueur
	 */
	public TrafficJamAGIndividuEtudiant(TrafficJamAGPlayer player) {
		_player = player;
	}

	/*
	 * Fonction d'�valuation.
	 * tableauJeu
	 */
	public void computeFitness() {
		_fitness = 0.;
		int[] tableauJeu = _player.copieTableauJeu(); // on va faire nos calculs sur une copie du tableau de jeu
		
		// A FAIRE : ajouter ici le code pour calculer la valeur de l'individu
		// ne pas oublier de mettre _fitness � jour !
	}
	
	/*
	 * Renvoie une copie de l'individu this � l'identique
	 */
	public TrafficJamAGIndividuEtudiant copie() {
		TrafficJamAGIndividuEtudiant ie = new TrafficJamAGIndividuEtudiant(_player);
		// A FAIRE : ajouter ici les copies des attributs
		
		return ie; 
	}
	
	/*
	 * Op�re une mutation sur chaque g�ne de this avec une probabilit� taux
	 */
	public void mutation(double taux) {
		// A FAIRE : muter les g�nes de this
	}
	
	/*
	 * Op�re un croisement entre this et individu et renvoie un nouvel individu
	 */
	public TrafficJamAGIndividuEtudiant croisement(TrafficJamAGIndividuEtudiant individu) {
		// faire une copie de individu :
		TrafficJamAGIndividuEtudiant ind = individu.copie();
		
		// A FAIRE : modifier les attributs de ind en les croisant avec this
		
		return ind;
	}

	/*
	 * Compare deux individus
	 * permet de trier un tableau d'individus pour Arrays.sort
	 * par ordre croissant selon la valeur renvoy�e par compareTo (voir TrafficJamAGPlayerEtudiant.java)
	 * si this < ind, renvoie -1
	 * si ind < this, renvoie 1
	 * si ind == this, renvoie 0
	 */
	public int compareTo(Object obj) {
		TrafficJamAGIndividuEtudiant ind = (TrafficJamAGIndividuEtudiant) obj;
		
		// A FAIRE : ind est maintenant un individu que l'on peut comparer � this
		
		return 0; // si les deux individus sont � �galit�
	}
}
