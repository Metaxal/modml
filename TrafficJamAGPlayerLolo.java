import java.util.Arrays;

public class TrafficJamAGPlayerLolo extends TrafficJamAGPlayer {
	protected TrafficJamAGIndividuLolo[] _individus;
	protected TrafficJamAGIndividuLolo[] _individus2;
	protected double _bestFitness;
	protected double _avgFitness;
	
	protected TrafficJamAGIndividuLolo _best;
	protected int _numCoup;

	public TrafficJamAGPlayerLolo() {
		super();
	}
	
	public void initLearning() {
		_best = null;
		
		genese();
	}

	public void resetJeu() {
		_numCoup = 0;
	}

	/*
	 * Initialise une nouvelle population d'individus
	 * avec des gènes ayant des valeurs aléatoires,
	 * donc probablement très mauvaises.
	 */
	public void genese() {
		int nbi = nbIndividus();
		_individus = new TrafficJamAGIndividuLolo[nbi];
		_individus2 = new TrafficJamAGIndividuLolo[nbi];
		
		for(int i = 0; i < nbi; i++)
			_individus[i] = new TrafficJamAGIndividuLolo(this);
	}
	
	public void learnAG(){
		System.out.println("nb Generation " + nbGenerations() + " stop = " + stop());
		for (int i = 0; i < nbGenerations() && !stop(); i++ ) {
			uneGeneration();
			nouvelleGeneration();
			if( i % 100 == 0 || (i % (nbGenerations()/10) == 0 && nbGenerations()/10 < 100)) {
				System.out.println("Apprentissage... Generation " + i);
				System.out.println("Best fitness = " + _bestFitness);
				System.out.println("Avg  fitness = " + _avgFitness);
			}
		}

		System.out.println("Fin apprentissage...");
		System.out.println("Best fitness = " + _bestFitness);
		System.out.println("Avg  fitness = " + _avgFitness);
	}
	
	public void uneGeneration() {
		// calcul de la performance
		_avgFitness = 0.;
		for(int i = 0; i < nbIndividus(); i++) {
			_individus[i].calculeFitness();
			_avgFitness += _individus[i].fitness();
		}
		_avgFitness /= nbIndividus();

		// tri du tableau
		Arrays.sort(_individus);
		
		_best = _individus[0];
		_bestFitness = _best.fitness();
		
		// Sélection, croisement, mutation
		// puis remplissage des cases vides avec de nouveux inidividus aléatoires
		_individus2[0] = _individus[0];
		int i2;
		for(i2 = 1; i2 < nbIndividus()*tauxSelection(); i2++) {
			double x1 = Math.random(); // strictement < 1.0
			x1 = x1*x1*x1; // pour s'aprocher de 0, donc du meilleur
			double x2 = Math.random();
			x2 = x2*x2*x2;
			int n1 = (int)Math.floor(x1*nbIndividus()); // on évite le best ?
			int n2 = (int)Math.floor(x2*nbIndividus());
			if(Math.random() < tauxCroisement())
				_individus2[i2] = _individus[n1].croisement(_individus[n2]);
			else
				_individus2[i2] = new TrafficJamAGIndividuLolo(_individus[n1]);

			_individus2[i2].mutation(tauxMutation());
		}
		
		// on comble avec de l'aléatoire
		for(; i2 < nbIndividus(); i2++) 
			_individus2[i2] = new TrafficJamAGIndividuLolo(this);
		
	}
	
	public void nouvelleGeneration() {
		_individus = _individus2;
	}
	
	public int joue() {
		if(_best != null)
			return _best.gene(_numCoup++); // séquence des coups du meilleur individu/solution
		else
			return -1;
	}
}
