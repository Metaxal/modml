import java.util.Arrays;

public class TrafficJamAGPlayerEns extends TrafficJamAGPlayer {
	protected TrafficJamAGIndividuEns[] _individus;
	protected TrafficJamAGIndividuEns[] _individus2;
	protected double _bestFitness;
	protected double _avgFitness;

	protected TrafficJamAGIndividuEns _best;
	protected int _numCoup;

	public TrafficJamAGPlayerEns() {
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
		_individus = new TrafficJamAGIndividuEns[nbi];
		_individus2 = new TrafficJamAGIndividuEns[nbi];

		for(int i = 0; i < nbi; i++)
			_individus[i] = new TrafficJamAGIndividuEns(this);
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

		// Sélection élitiste, on conserve le meilleur
		_individus2[0] = _individus[0];

		// Sélection, croisement, mutation
		// puis remplissage des cases vides avec de nouveux inidividus aléatoires
		int i2;
		for(i2 = 1; i2 < nbIndividus()*tauxSelection(); i2++) {
			// Sélection
			TrafficJamAGIndividuEns indiv1 = _individus[selection()];
			TrafficJamAGIndividuEns indiv2 = _individus[selection()];

			// Croisement
			if(Math.random() < tauxCroisement())
				_individus2[i2] = indiv1.croisement(indiv2);
			else
				// Sinon copie de l'ancien individu
				_individus2[i2] = new TrafficJamAGIndividuEns(indiv1);

			// Mutation
			_individus2[i2].mutation(tauxMutation());
		}

		// on comble avec de l'aléatoire pour conserver de la diversité
		for(; i2 < nbIndividus(); i2++)
			_individus2[i2] = new TrafficJamAGIndividuEns(this);

	}

	public int selection() {
		double x = Math.random(); // strictement < 1.0
		return (int)Math.floor(x*x*nbIndividus());
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
