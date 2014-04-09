/*
 * @author: Laurent Orseau
 */

/*
 * points d'entr�e :
 * bool _board.joueCase(int numeroCase, int numeroJoueur) : modifie _board, renvoit vrai si op�ration r�ussie
 * bool _board.endOfGame() : est-ce que _board est dans une position terminale ?
 * _board.setCellValue(int c, -1) : mets -1 (aucun joueur) dans la case num�ro c
 * int mePlayer() : num�ro du joueur (nous)
 * int nextPlayer(int joueur) : l'autre joueur
 * int _board.result() : num�ro du joueur gagnant s'il y en a un. 
 */

public class TicTacToeMinmaxPlayerEtudiant extends TicTacToeVirtualPlayer {
	TicTacToeBoard _board; // 9 cases de 0 � 8, permet de faire une copie du tableau de jeu pour le modifier � volont�
	int _tour; // � quel tour en est-on dans le d�veloppement de minmax/alphabeta ?
	int _nbNoeuds; // nombres de noeurs visit�s lors d'un parcours minmax/alphabeta
	int _caseAJouer; // variable � mettre � jour d�signant la case dans laquelle jouer

	public TicTacToeMinmaxPlayerEtudiant() {
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
		_caseAJouer = -1;
		
		// ICI : appel � minmax ou alphabeta
		
		System.out.println("Nb Noeuds parcourus = "+_nbNoeuds);
		
		return _caseAJouer;
	}
}
