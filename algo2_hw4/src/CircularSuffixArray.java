
public class CircularSuffixArray {
	
	private String s;
	private int size;
	private int[] suffixes;
	private static int R = 256;
	
    public CircularSuffixArray(String s)  {// circular suffix array of s
    	this.s = s;
    	this.size = s.length();
    	suffixes = new int[size];
    	for(int i = 0; i < size; i++) {
    		suffixes[i] = i;
    	}
    	
    	sort(suffixes);
    	//threeWaySort(suffixes);
    	for(int i = 0; i < size; i++) {
    		StdOut.println(suffixes[i] + ":" + s.charAt(suffixes[i] % size));
    	}
    }
    
    public int length() { // length of s
    	return size;
    }
    
    public int index(int i) { // returns index of ith sorted suffix
    	return suffixes[i];
    }
    
    
    private void sort(int[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    // quicksort the subarray from a[lo] to a[hi]
    private void sort(int[] a, int lo, int hi) { 
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
        //assert isSorted(a, lo, hi);
    }
    
 // partition the subarray a[lo .. hi] by returning an index j
    // so that a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
    private int partition(int[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        int v = a[lo];

        while (true) { 

            // find item on lo to swap
            while (less(a[++i], v))
                if (i == hi) break;

            // find item on hi to swap
            while (less(v, a[--j]))
                if (j == lo) break;      // redundant since a[lo] acts as sentinel

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }

        // put v = a[j] into position
        exch(a, lo, j);

        // with a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

   /***********************************************************************
    *  Rearranges the elements in a so that a[k] is the kth smallest element,
    *  and a[0] through a[k-1] are less than or equal to a[k], and
    *  a[k+1] through a[n-1] are greater than or equal to a[k].
    ***********************************************************************/
    private int select(int[] a, int k) {
        if (k < 0 || k >= a.length) {
            throw new RuntimeException("Selected element out of bounds");
        }
        StdRandom.shuffle(a);
        int lo = 0, hi = a.length - 1;
        while (hi > lo) {
            int i = partition(a, lo, hi);
            if      (i > k) hi = i - 1;
            else if (i < k) lo = i + 1;
            else return a[i];
        }
        return a[lo];
    }
    
 // exchange a[i] and a[j]
    private void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
    
    // is v < w ?
    private boolean less(int v, int w) {
    	for (int i = 0; i < size; i++) {
    		char vs = s.charAt((v + i) % size);
    		char ws = s.charAt((w + i) % size);
    		if (vs < ws) return true;
    		if (vs > ws) return false;
    	}
        return false;
    }
    
    
    private void threeWaySort(int[] a)
    { threeWaySort(a, 0, a.length - 1, 0); }
    
    private int charAt(int[] a, int i, int d)
    {
    	return s.charAt((i + d) % size);
    }
    
    private void threeWaySort(int[] a, int lo, int hi, int d)
    {
    	if (hi <= lo) return;
    	int lt = lo, gt = hi;
    	int v = charAt(a, lo, d);
    	int i = lo + 1;
    	while (i <= gt)
    	{
    		int t = charAt(a, i, d);
    		if (t < v) exch(a, lt++, i++);
    		else if (t > v) exch(a, i, gt--);
    		else i++;
    	}
    	threeWaySort(a, lo, lt-1, d);
    	if (v >= 0) threeWaySort(a, lt, gt, d+1);
    	threeWaySort(a, gt+1, hi, d);
    }
    
    
    public static void main(String[] args) {
    	CircularSuffixArray cfa = new CircularSuffixArray("ABRACADABRA!");
    		
    }
    
}