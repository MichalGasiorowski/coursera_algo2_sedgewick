import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SAP {
	// constructor takes a digraph (not necessarily a DAG)
	private Digraph diG;
	private final static int cacheSize = 100;
	//private boolean[][] anc_computed;
	//private int[][] l_cache;
	//private int[][] a_cache;
	
	private Queue<P> cacheQ;
	private Map<P, Integer> lCache;
	private Map<P, Integer> aCache;
	
	public SAP(Digraph G) {
		this.diG = new Digraph(G);
		cacheQ = new Queue<SAP.P>();
		lCache = new HashMap<SAP.P, Integer>();
		aCache = new HashMap<SAP.P, Integer>();
	}
	
	
	private class P {
		int v;
		int w;
		public P(int v, int w) {this.v = v;this.w=w;}
		public P() {}
	}
	
	
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		return anc_len(v, w)[1];
	}
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		return anc_len(v, w)[0];
	}
	
	
	private int[] anc_len(int v, int w) {
		if (!argsOk(v, w)) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		int[] ret = new int[2];
		P p = new P(v, w);
		if (lCache.containsKey(p)) {
			ret[0] = aCache.get(p);
			ret[1] = lCache.get(p);
			//StdOut.printf("used cache for: [%d, %d]", v, w);
			return ret;
		}
		
		BreadthFirstDirectedPaths vb = new BreadthFirstDirectedPaths(diG, v);
		BreadthFirstDirectedPaths wb = new BreadthFirstDirectedPaths(diG, w);
		
		int anc = -1;
		int a = -1;
		int shortPath = 2 * diG.V();
		int sumAnc = -1;
		while (a++ < diG.V() - 1) {
			if (!vb.hasPathTo(a) || !wb.hasPathTo(a)) {
				continue;
			}
			sumAnc = vb.distTo(a) + wb.distTo(a);
			if (sumAnc < shortPath) {
				anc = a;
				shortPath = sumAnc;
			}
		}
		
		if (anc == -1) {
			shortPath = -1;
		}
		ret[0] = anc; 
		ret[1] = shortPath;
		if (cacheQ.size() == cacheSize) {
			P pp = cacheQ.dequeue();
			aCache.remove(pp);
			lCache.remove(pp);
		}
		cacheQ.enqueue(p);
		aCache.put(p, ret[0]);
		lCache.put(p, ret[1]);
		return ret;
	}
	
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		return anc_len(v, w)[3];
	}
	
	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		return anc_len(v, w)[2];
	}
	
	private int[] anc_len(Iterable<Integer> v, Iterable<Integer> w) {
		
		for (int vv : v) {
			if (vv < 0 || vv >= diG.V()) {
				throw new java.lang.IndexOutOfBoundsException();
			}
		}
		for (int ww : w) {
			if (ww < 0 || ww >= diG.V()) {
				throw new java.lang.IndexOutOfBoundsException();
			}
		}
		
		int[] ret = new int[4];
		ret[0] = ret[1] = ret[2] = ret[3] = -1;
		
		int anc = -1;
		int a = -1;
		int shortPath = 2 * diG.V();
		int sumAnc = -1;
		
		BreadthFirstDirectedPaths vb = new BreadthFirstDirectedPaths(diG, v);
		BreadthFirstDirectedPaths wb = new BreadthFirstDirectedPaths(diG, w);
		
		while (a++ < diG.V() - 1) {
			//StdOut.println("while() a:" + a);
			if (!vb.hasPathTo(a) || !wb.hasPathTo(a)) {
				//StdOut.print("while() noPath a:" + a);
				continue;
			} 
			sumAnc = vb.distTo(a) + wb.distTo(a);
			if (sumAnc < shortPath) {
				anc = a;
				shortPath = sumAnc;
				ret[0] = vb.pathTo(a).iterator().next();
				ret[1] = wb.pathTo(a).iterator().next();
			}
		}
		if (anc == -1) {
			shortPath = -1;
		}
		ret[2] = anc; 
		ret[3] = shortPath;
		return ret;
		
		
	}
	
	private boolean argsOk(int v, int w) {
		if (v >= 0 && v < diG.V() && w >= 0 && w < diG.V()) {
			return true;
		}
		return false;
	}
	
	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
		//In in = new In(args[0]);
		In in = new In("data/wordnet/digraph1.txt");
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    //StdOut.printf("yo!" + args[0] + "\n" + G);
	    /*
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	    */
	    Integer [] v = new Integer []{7, 8};
		Integer [] w = new Integer []{11, 12};
		int l = sap.length(Arrays.asList(v), Arrays.asList(w));
		int a = sap.ancestor(Arrays.asList(v), Arrays.asList(w));
		StdOut.printf("length = %d, ancestor = %d\n", l, a);
	}
	
	
	
}
