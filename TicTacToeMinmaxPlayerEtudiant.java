/*
 * @author: Laurent Orseau
 */

/*
 * points d'entrée :
 * bool _board.joueCase(int numeroCase, int numeroJoueur) : modifie _board, renvoit vrai si opération réussie
 * bool _board.endOfGame() : est-ce que _board est dans une position terminale ?
 * _board.setCellValue(int c, -1) : mets -1 (aucun joueur) dans la case numéro c
 * int mePlayer() : numéro du joueur (nous)
 * int nextPlayer(int joueur) : l'autre joueur
 * int _board.result() : numéro du joueur gagnant s'il y en a un. 
 */

public class TicTacToeMinmaxPlayerEtudiant extends TicTacToeVirtualPlayer {
	TicTacToeBoard _board; // 9 cases de 0 à 8, permet de faire une copie du tableau de jeu pour le modifier à volonté
	int _tour; // à quel tour en est-on dans le développement de minmax/alphabeta ?
	int _nbNoeuds; // nombres de noeurs visités lors d'un parcours minmax/alphabeta
	int _caseAJouer; // variable à mettre à jour désignant la case dans laquelle jouer

	public TicTacToeMinmaxPlayerEtudiant() {
	}

	/*
	 * Méthode appelée par l'interface lorsque c'est à notre tour de jouer
	 * Il faut renvoyer le numéro (0 à 9) de la case dans laquelle on veut jouer
	 * (si ce numéro est mauvais, l'interface appelle joue() à nouveau) 
	 * 
	 */
	public int joue() {
		_board = copyBoard(); // on copie le board (pas le droit de modifier celui qui existe)
		_tour = 0;
		_nbNoeuds = 0;
		_caseAJouer = -1;
		
		// ICI : appel à minmax ou alphabeta
		
		System.out.println("Nb Noeuds parcourus = "+_nbNoeuds);
		
		return _caseAJouer;
	}
}
