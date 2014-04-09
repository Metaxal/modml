//import java.* ;

/**
 * Couple (Etat, Action) permettant d'associer une action à un état
 *
 * @author Garcia Pascal
 * @see State
 */
public class StateAction implements Comparable {

    private State _state;
    private int  _action;

    /**
     * Crée un couple (Etat, Action)
     *
     * @author Etudiant
     * @param e objet Etat
     * @param a entier représentant l'action (la case du Morpion)
     */
    public StateAction(State s, int a) {
    _state = s;
    _action = a;
    }

    /**
     * Retourne l'Etat du couple (Etat, Action)
     *
     * @author Etudiant
     * @return l'Etat du couple
     */
    public State getState() {
    return _state;
    }

    /**
     * Retourne l'Action du couple (Etat, Action)
     *
     * @author Etudiant
     * @return l'Action (int) du couple (Etat, Action)
     */
    public int getAction() {
    return _action;
    }

    /**
     * Retourne une copie de l'objet courant
     *
     * @author Etudiant
     * @return une copie de l'objet courant
     */
    public Object clone() {
    Object o = null;
    try {
        o = super.clone();
        ((StateAction)o)._state = (State)_state.clone();
        ((StateAction)o)._action = _action;
    } catch (CloneNotSupportedException e) {}
    return o;
    }

    /**
     * Test l'égalité de 2 objets de type EtatAction
     *
     * @author Etudiant
     * @param e_a l'objet de type EtatAction à comparer avec l'objet courant
     * @return true si e_a est identique à l'objet courant, false sinon
     */
    public boolean equals(Object o) {
    if ((o != null) && (o instanceof StateAction)) {
        StateAction sA = (StateAction)o;
        return _state.equals(sA.getState()) && _action == sA.getAction();
    }
    return false;
    }

    public int compareTo(Object o) {
    int res = 0 ;
    if (o instanceof StateAction) {
        if (equals(o)) res = 0;
        else {
        int c1 = _state.compareTo(((StateAction)o)._state);
        if (c1 == -1 || (c1 == 0 && _action < ((StateAction)o)._action)) res = -1;
        else res = 1;
        }
    }
    else throw new ClassCastException("this object is not an instance of a StateAction");
    return res;
    }

    public static void main(String[] args) {
    }

}




