import java.util.Arrays;

/**
 * @author lorseau
 * classe TrafficJamAGPlayerEtudiant
 * appel�e par l'interface de jeu � travers les m�thodes :
 * 	void resetJeu()
 *  int joue()
 */

public class TrafficJamAGPlayerEtudiant extends TrafficJamAGPlayer {
	// A FAIRE : ajouter ici les attributs n�cessaires
	// taux de mutation, taux de croisement, nombre d'individus par population, 
	// nombre de g�n�rations avant arr�t de l'apprentissage, 
	// tableau des individus (TrafficJamAGIndividuEtudiant)

	/*
	 * num�ro du prochain coup que l'on va jouer lorsque l'interface appelera notre fonction joue()
	 * (initialis� � 0, d�s qu'on "clique" sur une case, on joue un coup et on passe au coup suivant)  
	 */
	protected int _numCoup;

	/**
	 * Constructeur
	 */
	public TrafficJamAGPlayerEtudiant() {
		super(); // � laisser : appelle le constructeur de TrafficJamAGPlayer pour faire ses initialisatations
		
		// A FAIRE : ajouter ici le code n�cessaire.
	}

	/*
	 * Initialisation de la phase d'apprentissage
	 * Appel�e par l'interface lorsque l'utilisateur clique dur LearnAG/Start
	 * mais pas sur LearnAG/Continue 
	 */
	public void initLearning() {
		System.out.println("initLearning �tudiant");
		// A FAIRE : une nouvelle population d'individus initialis�s al�atoirement ! 
	}

	/*
	 * Appel�e par l'interface lorsque l'utilisateur clique sur TrafficJam/Reset
	 */
	public void resetJeu() {
		_numCoup = 0; // on recommence le jeu de 0
	}
	
	/*
	 * Appel�e par l'interface lorsque l'utilisateur clique dur LearnAG/Start ou 
	 * sur LearnAG/Continue
	 */
	public void learnAG(){
		System.out.println("learnAG �tudiant");
		
		// A FAIRE : s�lection/croisement/mutation, le tout sur plusieurs g�n�rations
		
		// On peut utiliser Arrays.sort(Truc[] tableau) pour trier le tableau de Trucs 
		// selon l'ordre d�fini par la m�thode Truc.Compare  
	}
	
	/*
	 * renvoie le coup jou� par le joueur courant
	 * = num�ro de la case (0 � gauche)
	 * Appel� par l'interface lorsque l'utilisateur clique sur TrafficJam/One Turn 
	 * ou sur LearnAG/Run (-> appels multiples successifs de joue())
	 * -1 arr�te les appels par Run, on arr�te de jouer.
	 */
	public int joue() {
		System.out.println("joue �tudiant, coup n�" + _numCoup);
		
		// A FAIRE : renvoyer le coup courant
		
		_numCoup++; // on passe au coup suivant
		return -1;
	}
}
