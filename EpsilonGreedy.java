import java.* ;

/**
 * Cette classe implémente la politique pseudo-stochastique.<BR>
 * Avec une probabilité epsilon, l'action choisie sera la meilleure possible,
 * avec une probabilité (1.0 - epsilon) on choisira au hazard une des autres actions. 
 *
 * @author Garcia Pascal
 * @see Table
 * @see StateAction
 * @see State
 */

public class EpsilonGreedy {
	
	private float _epsilon;
	
	/**
	 * Crée une politique pseudo-stochastique
	 *
	 * @param e probabilité de choisir la meilleure action
	 */    
	public EpsilonGreedy(float e) {
		_epsilon = e;
	}
	
	/**
	 * Retourne l'action dictée par la politique pseudo-stochastique dans l'etat e
	 *
	 * @param t la table contenant les valeurs des différents objets EtatAction
	 * @param e l'état dans lequel on doit appliquer la politique pseudo-stochastique
	 * @return l'action selectionnée
	 */    
	public int getAction(Table t, State s) {	
		int bestAction = getBestAction(t, s);
		int[] actions = s.getPossibleActions();
		if (Math.random() < _epsilon || actions.length == 1) return bestAction;       
		int index = (int)(Math.random() * (float)(actions.length - 1));
		if (actions[index] == bestAction) return actions[actions.length - 1];
		else return actions[index];	
	}
	
	/**
	 * Retourne la meilleure action dans l'état e
	 *
	 * @param t la table contenant les valeurs des différents objets EtatAction
	 * @param e l'état dans lequel on doit appliquer la politique pseudo-stochastique
	 * @return l'action selectionnée
	 */    
	public int getBestAction(Table t, State s) {	
		int[] actions = s.getPossibleActions();
		int bestAction = actions[0];
		float bestValue = t.getValue(new StateAction(s,bestAction));
		for (int i = 1; i < actions.length; i++) {
			float tmp = t.getValue(new StateAction(s,actions[i]));	    
			if (tmp > bestValue 
					|| (tmp == bestValue && Math.random() < 0.5) ) { // ajouter de l'aléatoire dans les coups de valeur identique
				bestValue = tmp;
				bestAction = actions[i];
			}
		}
		return bestAction;
	}
	
}
