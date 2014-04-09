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
	 * Retourne la valeur d'un objet EtatAction. Si l'�tat correspondant � e_a.getEtat() n'existe pas dans la table,
	 * on cr�e les entr�es dans la table pour tous les objets EtatAction correspondant �
	 * l'�tat de e_a.getEtat() et � toutes les actions possibles dans cet �tat. On associe a chacune de ces
	 * entr�es la valeur 0.0
	 *
	 * @param sA l'objet EtatAction
	 * @return la valeur associ�e � cet objet
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
	 * Retourne la valeur maximum des diff�rentes entr�es dans la table associ� � cet �tat 
	 * (c'est � dire les diff�rentes entr�es ayant pour �tat cet �tat et pour action les diff�rentes
	 * actions possibles dans cet �tat).<BR> 
	 * Si on ne trouve aucune entr�e dans la table contenant cet �tat, 
	 * on cr�e les entr�es dans la table pour tous les objets EtatAction correspondant �
	 * l'�tat de e_a.getEtat() et � toutes les actions possibles dans cet �tat. On associe a chacune de ces
	 * entr�es la valeur 0.0
	 *
	 * @param e l'objet Etat
	 * @return la valeur maximum que l'on peut obtenir � partir de cet �tat
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
	 * Met � jour la valeur de l'objet EtatAction avec la valeur val.<BR> 
	 * Si on ne trouve aucune entr�e dans la table contenant l'�tat de e_a.getEtat(), 
	 * on cr�e les entr�es dans la table pour tous les objets EtatAction correspondant �
	 * l'�tat de e_a.getEtat() et � toutes les actions possibles dans cet �tat. On associe a chacune de ces
	 * entr�es la valeur 0.0
	 *
	 * @param e_a l'objet EtatAction
	 * @param val la valeur associ� maintenant avec e_a
	 * @return la valeur maximum que l'on peut obtenir � partir de cet �tat
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
