public class TrafficJamAGIndividuLolo implements Comparable {
	protected int[] _genes;
	protected double _fitness;
	protected TrafficJamAGPlayer _player;
	
	public double fitness() { return _fitness; } 

	public int gene(int i) { if(i < _genes.length) return _genes[i]; else return -1; }
	
	public TrafficJamAGIndividuLolo(TrafficJamAGPlayer player) {
		_player = player;
		int nbMax = (_player.nbCases()/2)*(_player.nbCases()/2+1)*2; 
		// *2 pour laisser un peu de marge pour les coups ill�gaux
		
		_fitness = -nbMax;
		
		_genes = new int[nbMax]; // une majoration du nombre de d�placements possibles (en fait -1)
		mutation(1.f);
	}
	
	public TrafficJamAGIndividuLolo(TrafficJamAGIndividuLolo ind) {
		_fitness = ind._fitness;		
		_player = ind._player;
		
		_genes = new int[ind._genes.length];
		for(int i = 0; i < _genes.length; i++)
			_genes[i] = ind._genes[i];
	}

	public void calculeFitness() { 
		_fitness = 0.;
		int[] tableauJeu = _player.copieTableauJeu();
		
		// ici, il faut appliquer la s�quence de jeu d�finie par les g�nes
		// pour savoir jusqu'o� on va dans le jeu.
		// le plus loin on va, le meilleur le score.
		for(int i=0; i < _genes.length && _genes[i] != -1; i++)
			if(_player.joueCase(tableauJeu, _genes[i]) == 1)
				_fitness += 1.;
			else
				_fitness -= 1./(i+1.);
		/*
		 * 1./(i+1.) : permet de favoriser les optimisations (enlever les coups ill�gaux) 
		 * en favorisant ceux qui sont au d�but de la s�quence.
		 * On atteint alors moins facilement des minima locaux.
		 */
	}
	
	public void mutation(double taux) {
		for(int i = 0; i < _genes.length; i++)
			if(Math.random() < taux)
				_genes[i] = (int)Math.floor(Math.random() * (_player.nbCases())); // +1 -1 on veut apprendre l'arret du jeu
		/*
		 * ceci est une repr�sentation simpliste, mais qui fonctionne pas mal.
		 * Une meilleure version consisterait � donner plus de coups l�gaux,
		 * par exemple � fournir soit jouer � gauche soit jouer � droite 
		 * (par rapport � la case vide)
		 */
	}

	public TrafficJamAGIndividuLolo croisement(TrafficJamAGIndividuLolo  individu) {
		TrafficJamAGIndividuLolo ind = new TrafficJamAGIndividuLolo(this);
		
		int n1 = (int)Math.floor(Math.random()*_genes.length);
		int n2 = (int)Math.floor(Math.random()*(_genes.length-n1)) + n1; // n2 compris entre n1 et _genes.length

		for(int i = n1; i < n2; i++)
			ind._genes[i] = individu._genes[i]; // croisement
		
		return ind; 
	}

	public int compareTo(Object obj) {
		TrafficJamAGIndividuLolo ind = (TrafficJamAGIndividuLolo) obj;
		
		if(_fitness > ind._fitness)
			return -1; // on veut le plus grand fitness au d�but
		else if(_fitness < ind._fitness)
			return 1;
		return 0;
	}
}
