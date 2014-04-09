public interface RLEngine {
    public void newGame();
    public void reset();
    public void setAlpha(float a);
    public void setGamma(float g);
    public void setEpsilon(float epsilon);
    public void setWinReinforcement(float r);
    public void setLoseReinforcement(float r);
    public void setDrawReinforcement(float r);
    public void setOtherReinforcement(float r);
    public int getBestAction(State s);
    public State getCurrentState();
    public float[] getActionsValues(State s);
    public int oneMove();
}
