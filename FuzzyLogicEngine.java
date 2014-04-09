public abstract class FuzzyLogicEngine {
    protected StrongFuzzyPartitionEngine[] _FPEs;
    
    protected FuzzyLogicEngine(int input_size) {
	_FPEs = new StrongFuzzyPartitionEngine[input_size];
    }
    protected StrongFuzzyPartitionEngine getStrongFuzzyPartitions(int index) {
	return _FPEs[index];
    }
    protected void setFuzzyPartition(int index, StrongFuzzyPartitionEngine sfp) {
	_FPEs[index] = sfp;
    }
    public abstract double[] getValue(double[] input);
    public abstract void setConclusions(double[] input, double[] output);
}
