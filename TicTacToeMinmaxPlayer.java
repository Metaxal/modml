/*
 * points d'entr�e :
 * bool _board.joueCase(int numeroCase, int numeroJoueur) : modifie _board, renvoit vrai si op�ration r�ussie
 * bool _board.endOfGame() : est-ce que _board est dans une position terminale ?
 * _board.setCellValue(int c, -1) : mets -1 (aucun joueur, cellule vide) dans la case num�ro c
 * int mePlayer() : num�ro du joueur (nous)
 * int nextPlayer(int joueur) : l'autre joueur
 * int _board.result() : num�ro du joueur gagnant s'il y en a un. 
 */
public class TicTacToeMinmaxPlayer extends TicTacToeVirtualPlayer {
	TicTacToeBoard _board; // 9 cases de 0 � 8, permet de faire une copie du tableau de jeu pour le modifier � volont�
	int _tour; // � quel tour en est-on dans le d�veloppement de minmax/alphabeta ?
	int _nbNoeuds; // nombres de noeurs visit�s lors d'un parcours minmax/alphabeta
	int _caseAJouer; // variable � mettre � jour d�signant la case dans laquelle jouer

	public TicTacToeMinmaxPlayer() {
	}
	
	protected int valueBoard() {
		int res = _board.result();
		if(res == mePlayer())
			return 1;//20-_tour; (pour le forcer � gagner au plus vite)
		if(res == nextPlayer(mePlayer()))
			return -1;
		return 0;
	}
	
	protected int max(boolean niveau0) {
		_nbNoeuds++;
		
		if(_board.endOfGame()) {
			int v = valueBoard();
			return v;
		}

		_tour++;
		
		int vmax = -2000;
		for(int c = 0; c < 9; c++) { // pour chacune des cases
			if(!_board.joueCase(c, mePlayer())) // on joue dans la case c
				continue;
			
			int v = min();
			_board.setCellValue(c, -1); // on restore la configuration originale
			if(v > vmax) {
				vmax = v;
				if(niveau0)
					_caseAJouer = c;
			}
		}
		
		_tour--;
		
		return vmax;
	}

	protected int min() {
		_nbNoeuds++;
		
		if(_board.endOfGame()) {
			int v = valueBoard();
			return v;
		}
		
		_tour++;
		
		int vmin = 2000;
		for(int c = 0; c < 9; c++) { // pour chacune des cases
			if(!_board.joueCase(c, nextPlayer(mePlayer()))) // nextPlayer � ne surtout pas oublier !!
				continue;
			
			int v = max(false);
			_board.setCellValue(c, -1);//EMPTY); // on restore la configuration originale
			if(v < vmin)
				vmin = v;
		}
		
		_tour--;
		
		return vmin;
	}
	
	protected int alpha(int al, int be, boolean niveau0) {
		_nbNoeuds++;

		if(_board.endOfGame()) {
			int v = valueBoard();
			return v;
		}

		_tour++;
		
		int vmax = -2000;
		for(int c = 0; c < 9; c++) { // pour chacune des cases
			if(!_board.joueCase(c, mePlayer()))
				continue;
			
			int v = beta(al, be);
			_board.setCellValue(c, -1); // on restore la configuration originale
			if(v > vmax) {
				vmax = v;
				if(niveau0)
					_caseAJouer = c;
			}
			if(v > be) // la valeur du noeud max sera donc au moins v, mais comme c'est >= au min du dessus, pas besoin de chercher les autres branches !
				return v; 
			if(v > al) { 
				al = v; 
			}
		}
		
		_tour--;

		return vmax;
	}

	protected int beta(int al, int be) {
		_nbNoeuds++;

		if(_board.endOfGame()) {
			int v = valueBoard();
			return v;
		}

		_tour++;

		int vmin = 2000;
		for(int c = 0; c < 9; c++) { // pour chacune des cases
			if(!_board.joueCase(c, nextPlayer(mePlayer())))
				continue;
			
			int v = alpha(al, be, false);
			_board.setCellValue(c, -1); // on restore la configuration originale
			if(v < vmin)
				vmin = v;
			if(v <= al) // la valeur du noeud min sera donc au plus v, mais comme c'est <= au min du dessus, pas besoin de chercher les autres branches !
				return v;
			if(v < be) {
				be = v; 
			}
		}
		
		_tour--;
		
		return vmin;
	}

	/*
	 * M�thode appel�e par l'interface lorsque c'est � notre tour de jouer
	 * Il faut renvoyer le num�ro (0 � 9) de la case dans laquelle on veut jouer
	 * (si ce num�ro est mauvais, l'interface appelle joue() � nouveau) 
	 * 
	 */
	public int joue() {
		_board = copyBoard(); // on copie le board (pas le droit de modifier celui qui existe)
		_tour = 0;
		_nbNoeuds = 0;
		_caseAJouer = -1; // par d�faut on joue dans une case qui n'existe pas...
		
		//max(true); // minmax
		alpha(-2000, 2000, true); // alphabeta
		
		System.out.println("Nb Noeuds parcourus = "+_nbNoeuds);
		
		return _caseAJouer;
	}
}
