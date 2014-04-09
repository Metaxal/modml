import java.util.*;

public class FuzzyLogicBasic extends FuzzyLogicEngine {
    private double[][] _conclusions;

    public FuzzyLogicBasic(StrongFuzzyPartitionEngine[] sfps, int output_size) {
	super(sfps.length);
	int nb = 1;
	for (int i = 0; i < sfps.length; i++) {
	    setFuzzyPartition(i, sfps[i]);
	    nb *= sfps[i].size();
	}

	_conclusions = new double[nb][output_size];

	for (int i = 0; i < _conclusions.length; i++) {
	    for (int j = 0; j < _conclusions[i].length; j++) {
		_conclusions[i][j] = 0f;
	    }
	}
    }
    
    private void addInteger(Integer index, List indexesList) {
	ListIterator indexesListIterator = indexesList.listIterator();
	while (indexesListIterator.hasNext()) {
	    List l = (List)indexesListIterator.next();
	    l.add(index);
	}
    }

    private void mulDouble(Double degree, List degreesList) {
	ListIterator degreesListIterator = degreesList.listIterator();
	while (degreesListIterator.hasNext()) {
	    Double f = (Double)degreesListIterator.next();
	    Double fNew = new Double(f.doubleValue() * degree.doubleValue());
	    degreesListIterator.set(fNew);
	}
    }

    private void add(List integerList, List indexesList, List doubleList, List degreesList) {
	if (indexesList.isEmpty()) {
	    ListIterator integerListIterator = integerList.listIterator();
	    while (integerListIterator.hasNext()) {
		List l = new LinkedList();
		l.add(integerListIterator.next());
		indexesList.add(l);
	    }
	    degreesList.addAll(doubleList);
	}
	else {
	    LinkedList[] indexesListArray = new LinkedList[integerList.size()];
	    LinkedList[] degreesListArray = new LinkedList[doubleList.size()];

	    for (int i = 0; i < integerList.size(); i++) {
		LinkedList indexesListClone = new LinkedList();
		ListIterator indexesListIterator = indexesList.listIterator();

		while (indexesListIterator.hasNext()) {
		    indexesListClone.add(((LinkedList)indexesListIterator.next()).clone());
		}	    
		indexesListArray[i] = indexesListClone;

		LinkedList degreesListClone = new LinkedList();
		ListIterator degreesListIterator = degreesList.listIterator();

		while (degreesListIterator.hasNext()) {
		    degreesListClone.add(new Double(((Double)degreesListIterator.next()).doubleValue()));
		}	    
		degreesListArray[i] = degreesListClone;
	    }

	    ListIterator integerListIterator = integerList.listIterator();
	    ListIterator doubleListIterator = doubleList.listIterator();
	    
	    int cpt = 0;
	    while (integerListIterator.hasNext() && doubleListIterator.hasNext()) {
		addInteger((Integer)integerListIterator.next(), indexesListArray[cpt]);
		mulDouble((Double)doubleListIterator.next(), degreesListArray[cpt]);
		cpt++;
	    }
	    
	    indexesList.clear();
	    degreesList.clear();
	    for (int i = 0; i < integerList.size(); i++) {
		indexesList.addAll(indexesListArray[i]);
		degreesList.addAll(degreesListArray[i]);
	    }
	}
    }

    private int getIndexFromList(List l) {
	int res = 0;
	ListIterator listIterator = l.listIterator(l.size());
	
	int i = _FPEs.length - 1;
	int cpt = 0;
	int size = 1;
	while (listIterator.hasPrevious()) {
	    cpt += size * ((Integer)listIterator.previous()).intValue();
	    if (i != 0) size *= _FPEs[i--].size();
	}
	return cpt;
    }

    private void getIndexesAndDegreesLists(double[] input, List indexesList, List degreesList) {
	for (int i = 0; i < input.length; i++) {
	    double[] degrees = _FPEs[i].getDegrees(input[i]);
	    List integerList = new LinkedList();
	    List doubleList = new LinkedList();
	    for (int j = 0; j < degrees.length; j++) {
		if (degrees[j] != 0) {
		    integerList.add(new Integer(j));
		    doubleList.add(new Double(degrees[j]));
		}
	    }
	    add(integerList, indexesList, doubleList, degreesList);
	}
    }

    public double[] getValue(double[] input) {
	List indexesList = new LinkedList();
	List degreesList = new LinkedList();
	getIndexesAndDegreesLists(input, indexesList, degreesList);
	ListIterator indexesListIterator = indexesList.listIterator();
	ListIterator degreesListIterator = degreesList.listIterator();
	double[] res = new double[_conclusions[0].length];
	for (int i = 0; i < res.length; i++) res[i] = 0f;
	while (indexesListIterator.hasNext() && degreesListIterator.hasNext()) {
	    List l = (List)indexesListIterator.next();
	    int i = getIndexFromList(l);
	    double degree = ((Double)degreesListIterator.next()).doubleValue();
	    for (int j = 0; j < res.length; j++) res[j] += _conclusions[i][j] * degree;
	}
	return res;
    }

    public void setConclusions(double[] input, double[] output) {
	List indexesList = new LinkedList();
	List degreesList = new LinkedList();
	getIndexesAndDegreesLists(input, indexesList, degreesList);
	ListIterator indexesListIterator = indexesList.listIterator();
	ListIterator degreesListIterator = degreesList.listIterator();

	double threshold = Math.pow(0.7, _FPEs.length); 

	while (indexesListIterator.hasNext() && degreesListIterator.hasNext()) {
	    List l = (List)indexesListIterator.next();
	    int i = getIndexFromList(l);
	    double degree = ((Double)degreesListIterator.next()).doubleValue();
	    for (int j = 0; j < _conclusions[i].length; j++) {
		System.out.println(degree + " " + " " + output[j]);
		if (degree >= threshold) {
		    _conclusions[i][j] = (1 - degree) * _conclusions[i][j] + degree * output[j];
		    //_conclusions[i][j] = output[j];
		}
	    }
	}
    }

    public static void main(String[] args) {
	StrongTriangularFuzzyPartition[] sftps = new StrongTriangularFuzzyPartition[3];
	sftps[0] = new StrongTriangularFuzzyPartition("x1", new double[]{0, 1, 2});
	sftps[1] = new StrongTriangularFuzzyPartition("x2", new double[]{0, 5, 10});
	sftps[2] = new StrongTriangularFuzzyPartition("x3", new double[]{-3, -2, -1});

	FuzzyLogicBasic flb = new FuzzyLogicBasic(sftps, 1);
	for (int i = 0; i < 10; i++)
	    flb.setConclusions(new double[]{1, 5, -3}, new double[]{10});
	System.out.println("Value at (0.5,2.5,-3) = " + flb.getValue(new double[]{0.5, 5, -3})[0]);
    }
}
