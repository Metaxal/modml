public class TrafficJamAGIndividuEns implements Comparable {
	protected int[] _genes;
	protected double _fitness;
	protected TrafficJamAGPlayer _player;
	
	public double fitness() { return _fitness; } 

	public int gene(int i) { if(i < _genes.length) return _genes[i]; else return -1; }
	
	public TrafficJamAGIndividuEns(TrafficJamAGPlayer player) {
		_player = player;
		int nbMax = (_player.nbCases()/2)*(_player.nbCases()/2+1)*2; 
		// *2 pour laisser un peu de marge pour les coups illégaux
		
		_fitness = -nbMax;
		
		_genes = new int[nbMax]; // une majoration du nombre de déplacements possibles (en fait -1)
		mutation(1.f);
	}
	
	public TrafficJamAGIndividuEns(TrafficJamAGIndividuEns ind) {
		_fitness = ind._fitness;		
		_player = ind._player;
		
		_genes = new int[ind._genes.length];
		for(int i = 0; i < _genes.length; i++)
			_genes[i] = ind._genes[i];
	}

	public void calculeFitness() { 
		_fitness = 0.;
		int[] tableauJeu = _player.copieTableauJeu();
		
		// ici, il faut appliquer la séquence de jeu définie par les gènes
		// pour savoir jusqu'où on va dans le jeu.
		// le plus loin on va, le meilleur le score.
		for(int i=0; i < _genes.length && _genes[i] != -1; i++)
			if(_player.joueCase(tableauJeu, _genes[i]) == 1)
				_fitness += 1.;
			else
				_fitness -= 1./(i+1.);
		/*
		 * 1./(i+1.) : permet de favoriser les optimisations (enlever les coups illégaux) 
		 * en favorisant ceux qui sont au début de la séquence.
		 * On atteint alors moins facilement des minima locaux.
		 */
	}
	
	public void mutation(double taux) {
		for(int i = 0; i < _genes.length; i++)
			if(Math.random() < taux)
				_genes[i] = (int)Math.floor(Math.random() * (_player.nbCases())); // +1 -1 on veut apprendre l'arret du jeu
		/*
		 * ceci est une représentation simpliste, mais qui fonctionne pas mal.
		 * Une meilleure version consisterait à donner plus de coups légaux,
		 * par exemple à fournir soit jouer à gauche soit jouer à droite 
		 * (par rapport à la case vide)
		 */
	}

	public TrafficJamAGIndividuEns croisement(TrafficJamAGIndividuEns  individu) {
		TrafficJamAGIndividuEns ind = new TrafficJamAGIndividuEns(this);
		
		int n1 = (int)Math.floor(Math.random()*_genes.length);
		int n2 = (int)Math.floor(Math.random()*(_genes.length-n1)) + n1; // n2 compris entre n1 et _genes.length

		for(int i = n1; i < n2; i++)
			ind._genes[i] = individu._genes[i]; // croisement
		
		return ind; 
	}

	public int compareTo(Object obj) {
		TrafficJamAGIndividuEns ind = (TrafficJamAGIndividuEns) obj;
		
		if(_fitness > ind._fitness)
			return -1; // on veut le plus grand fitness au début
		else if(_fitness < ind._fitness)
			return 1;
		return 0;
	}
}
