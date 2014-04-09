import java.util.Arrays;

/**
 * @author lorseau
 * classe TrafficJamAGPlayerEtudiant
 * appelée par l'interface de jeu à travers les méthodes :
 * 	void resetJeu()
 *  int joue()
 */

public class TrafficJamAGPlayerEtudiant extends TrafficJamAGPlayer {
	// A FAIRE : ajouter ici les attributs nécessaires
	// taux de mutation, taux de croisement, nombre d'individus par population, 
	// nombre de générations avant arrêt de l'apprentissage, 
	// tableau des individus (TrafficJamAGIndividuEtudiant)

	/*
	 * numéro du prochain coup que l'on va jouer lorsque l'interface appelera notre fonction joue()
	 * (initialisé à 0, dès qu'on "clique" sur une case, on joue un coup et on passe au coup suivant)  
	 */
	protected int _numCoup;

	/**
	 * Constructeur
	 */
	public TrafficJamAGPlayerEtudiant() {
		super(); // à laisser : appelle le constructeur de TrafficJamAGPlayer pour faire ses initialisatations
		
		// A FAIRE : ajouter ici le code nécessaire.
	}

	/*
	 * Initialisation de la phase d'apprentissage
	 * Appelée par l'interface lorsque l'utilisateur clique dur LearnAG/Start
	 * mais pas sur LearnAG/Continue 
	 */
	public void initLearning() {
		System.out.println("initLearning étudiant");
		// A FAIRE : une nouvelle population d'individus initialisés aléatoirement ! 
	}

	/*
	 * Appelée par l'interface lorsque l'utilisateur clique sur TrafficJam/Reset
	 */
	public void resetJeu() {
		_numCoup = 0; // on recommence le jeu de 0
	}
	
	/*
	 * Appelée par l'interface lorsque l'utilisateur clique dur LearnAG/Start ou 
	 * sur LearnAG/Continue
	 */
	public void learnAG(){
		System.out.println("learnAG étudiant");
		
		// A FAIRE : sélection/croisement/mutation, le tout sur plusieurs générations
		
		// On peut utiliser Arrays.sort(Truc[] tableau) pour trier le tableau de Trucs 
		// selon l'ordre défini par la méthode Truc.Compare  
	}
	
	/*
	 * renvoie le coup joué par le joueur courant
	 * = numéro de la case (0 à gauche)
	 * Appelé par l'interface lorsque l'utilisateur clique sur TrafficJam/One Turn 
	 * ou sur LearnAG/Run (-> appels multiples successifs de joue())
	 * -1 arrête les appels par Run, on arrête de jouer.
	 */
	public int joue() {
		System.out.println("joue étudiant, coup n°" + _numCoup);
		
		// A FAIRE : renvoyer le coup courant
		
		_numCoup++; // on passe au coup suivant
		return -1;
	}
}
