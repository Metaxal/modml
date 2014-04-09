public abstract class State implements Cloneable, Comparable {
    public abstract int[] getPossibleActions();
    public boolean equals(Object o) {
    if ((o != null) && (o instanceof State))
        return super.equals(o);
    return false;
    }
    public Object clone() {
    Object o = null;
    try {
        o = super.clone();
    } catch (CloneNotSupportedException e) {}
    return o;
    }
}
