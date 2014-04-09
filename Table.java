import java.util.TreeMap ;

/**
 *
 * @author Garcia Pascal
 * @see StateAction
 * @see TicTacToeState
 */
public class Table {
	
	private TreeMap _qValues;
	
	public Table() {
		_qValues = new TreeMap();
	}
	
	private void create(State s) {
		int[] actions = s.getPossibleActions();	
		State newState = (State)s.clone();
		/*if(newState == null)
			System.out.println("newState null !!");*/
		for (int i = 0; i < actions.length; i++) {
			//System.out.println("push "+actions[i]);
			_qValues.put(new StateAction(newState, actions[i]), new Float(0.0f));
		}	
	}
	
	private boolean exists(State s) {	
		int[] actions = s.getPossibleActions();
		for (int i = 0; i < actions.length; i++) {
			//System.out.println(actions[i]);
			if (_qValues.containsKey(new StateAction(s, actions[i]))) return true;
		}	
		return false;
	}
	
	/**
	 * Retourne la valeur d'un objet EtatAction. Si l'état correspondant à e_a.getEtat() n'existe pas dans la table,
	 * on crée les entrées dans la table pour tous les objets EtatAction correspondant à
	 * l'état de e_a.getEtat() et à toutes les actions possibles dans cet état. On associe a chacune de ces
	 * entrées la valeur 0.0
	 *
	 * @param sA l'objet EtatAction
	 * @return la valeur associée à cet objet
	 */    
	public float getValue(StateAction sA) {
		if (!exists(sA.getState())) {
			create(sA.getState());
		}
		return ((Float)_qValues.get(sA)).floatValue();	       
	}
	
	public float getValue(State s,  int action) {
		/*for(int i = 0; i < s.getPossibleActions().length; i++) {
			System.out.println(s.getPossibleActions()[i]);
		}
		System.out.println("action : " + action);*/
		StateAction sA = new StateAction(s,action);
		if (!exists(sA.getState())) {
			//System.out.println("Creation");
			create(sA.getState());
		}
		/*else {
			System.out.println("Existe");
		}*/
		//	System.out.println(_qValues.get(sA));
		/*if(_qValues == null)
			System.out.println("qValues null!!");
		if(_qValues.get(sA) == null)
			System.out.println("qValues.get(sA) null!!");*/
		return ((Float)_qValues.get(sA)).floatValue();	       
	}
	
	/**
	 * Retourne la valeur maximum des différentes entrées dans la table associé à cet état 
	 * (c'est à dire les différentes entrées ayant pour état cet état et pour action les différentes
	 * actions possibles dans cet état).<BR> 
	 * Si on ne trouve aucune entrée dans la table contenant cet état, 
	 * on crée les entrées dans la table pour tous les objets EtatAction correspondant à
	 * l'état de e_a.getEtat() et à toutes les actions possibles dans cet état. On associe a chacune de ces
	 * entrées la valeur 0.0
	 *
	 * @param e l'objet Etat
	 * @return la valeur maximum que l'on peut obtenir à partir de cet état
	 */    
	public float getMaxValue(State s) {
		if (!exists(s)) {
			create(s);
			return 0.0f;
		}
		int[] actions = s.getPossibleActions();
		float value = getValue(new StateAction(s, actions[0]));
		
		for (int i = 0; i < actions.length; i++) {
			float tmp = getValue(new StateAction(s, actions[i]));
			if (tmp > value) {
				value = tmp;
			} 
		}
		return value;
	}
	
	/**
	 * Met à jour la valeur de l'objet EtatAction avec la valeur val.<BR> 
	 * Si on ne trouve aucune entrée dans la table contenant l'état de e_a.getEtat(), 
	 * on crée les entrées dans la table pour tous les objets EtatAction correspondant à
	 * l'état de e_a.getEtat() et à toutes les actions possibles dans cet état. On associe a chacune de ces
	 * entrées la valeur 0.0
	 *
	 * @param e_a l'objet EtatAction
	 * @param val la valeur associé maintenant avec e_a
	 * @return la valeur maximum que l'on peut obtenir à partir de cet état
	 */    
	public void setValue(StateAction sA, float val) {
		if (!exists(sA.getState())) {
			create(sA.getState());
		}	
		_qValues.put(sA, new Float(val)) ;
	}
	
	public void setValue(State s, int action, float val) {
		StateAction sA = new StateAction(s,action);
		if (!exists(sA.getState())) {
			create(sA.getState());
		}	
		_qValues.put(sA, new Float(val)) ;
	}
}
