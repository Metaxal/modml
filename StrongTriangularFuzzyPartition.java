public class StrongTriangularFuzzyPartition implements StrongFuzzyPartitionEngine {
    private String _name;
    private double[] _modalities;

    public StrongTriangularFuzzyPartition(String name, double[] modalities) {
	_name = name;
	_modalities = new double[modalities.length];
	for (int i = 0; i < modalities.length; i++)
	    _modalities[i] = modalities[i];
    }

    public double[] getDegrees(double input) {
	double[] res = new double[_modalities.length];
	for (int i = 0; i < res.length; i++) res[i] = 0f;
	if (input <= _modalities[0]) res[0] = 1f;
	if (input >= _modalities[_modalities.length - 1]) res[res.length - 1] = 1f;

	for (int i = 1; i < res.length; i++) {
	    if (_modalities[i - 1] < input && input <= _modalities[i]) {
		res[i - 1] = 1 - ((input - _modalities[i - 1]) / (_modalities[i] - _modalities[i - 1]));
		res[i] = ((input - _modalities[i - 1]) / (_modalities[i] - _modalities[i - 1]));
	    }
	}
	return res;
    }

    public int size() {
	return _modalities.length;
    }

    public void setParameters(Object[] values) {
	_name = (String)values[0];
	for (int i = 1; i < values.length; i++)
	    _modalities[i] = ((Double)values[i]).doubleValue();
    }

    public Object[] getParameters() {
	Object[] res = new Object[_modalities.length + 1];
	res[0] = _name;
	for (int i = 0; i < _modalities.length; i++)
	    res[i+1] = new Double(_modalities[i]);
	return res;
    }

    public String toString() {
	String res = _name + " [ ";
	for (int i = 0; i < _modalities.length; i++)
	    res += _modalities[i] + " ";
	return res + "]";
    }

    public static void main(String[] args) {
	double[] modalities = {0, 1, 2};
	StrongTriangularFuzzyPartition stfp = 	    
	    new StrongTriangularFuzzyPartition("x1", modalities);
	System.out.println(stfp);
	double[] d = stfp.getDegrees(0.75);
	for (int i = 0; i < d.length; i++)
	    System.out.print(d[i] + " ");
	System.out.println();
    }
}
