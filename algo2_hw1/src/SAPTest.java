import static org.junit.Assert.*;

import org.junit.Test;


import static org.junit.Assert.assertEquals;


import java.util.Arrays;


import org.junit.BeforeClass;
import org.junit.Test;


public class SAPTest {


	private static SAP sap1;
	private static SAP sap2;
	private static SAP sap3;
	private static SAP sap4;
	private static SAP sap5;
	private static SAP sap6;
	private static SAP sap7;


	@BeforeClass
	public static void setup() {
		In in = new In("data/wordnet/digraph1.txt");
		Digraph G1 = new Digraph(in);
		sap1 = new SAP(G1);


		in = new In("data/wordnet/digraph2.txt");
		Digraph G2 = new Digraph(in);
		sap2 = new SAP(G2);


		in = new In("data/wordnet/digraph3.txt");
		Digraph G3 = new Digraph(in);
		sap3 = new SAP(G3);


		in = new In("data/wordnet/digraph4.txt");
		Digraph G4 = new Digraph(in);
		sap4 = new SAP(G4);


		in = new In("data/wordnet/digraph5.txt");
		Digraph G5 = new Digraph(in);
		sap5 = new SAP(G5);


		in = new In("data/wordnet/digraph6.txt");
		Digraph G6 = new Digraph(in);
		sap6 = new SAP(G6);


		
		in = new In("data/wordnet/digraph-ambiguous-ancestor.txt");
		Digraph G7 = new Digraph(in);
		sap7 = new SAP(G7);
		
	}


	@Test
	public void testDigraph1() {
		assertEquals(4, sap1.length(3, 11));
	}


	@Test
	public void testDigraph1Anc() {
		assertEquals(1, sap1.ancestor(3, 11));
	}


	
	@Test
	public void testDigraph1Ex2() {
		assertEquals(3, sap1.length(9, 12));
		assertEquals(5, sap1.ancestor(9, 12));
	}
	
	@Test
	public void testDigraph1Ex3() {
		assertEquals(4, sap1.length(7, 2));
		assertEquals(0, sap1.ancestor(7, 2));
	}
	
	@Test
	public void testDigraph1Ex4Negative() {
		assertEquals(-1, sap1.length(1, 6));
		assertEquals(-1, sap1.ancestor(1,6));
	}
	
	@Test
	public void testDigraph1WithOneBeingAncestor() {
		assertEquals(2, sap1.length(3, 0));
		assertEquals(0, sap1.ancestor(3, 0));
	}


	@Test
	public void testDigraph1WithAnotherBeingAncestor() {
		assertEquals(1, sap1.length(1, 5));
		assertEquals(1, sap1.ancestor(1, 5));
	}
	
	@Test
	public void testDigraph1WithSources() {
		Integer [] v = new Integer []{7,8};
		Integer [] w = new Integer []{11,12};
		assertEquals(5, sap1.length(Arrays.asList(v), Arrays.asList(w)));
		assertEquals(1, sap1.ancestor(Arrays.asList(v), Arrays.asList(w)));
	}
	
	
	@Test
	public void testDigraph1WithSourcesContainingAncestors() {
		Integer [] v = new Integer []{7,3};
		Integer [] w = new Integer []{11,10};
		assertEquals(3, sap1.length(Arrays.asList(v), Arrays.asList(w)));
		assertEquals(1, sap1.ancestor(Arrays.asList(v), Arrays.asList(w)));
	}
	
	@Test
	public void testDigraph2() {
		assertEquals(2, sap2.length(1, 5));
	}


	@Test
	public void testDigraph2Anc() {
		assertEquals(0, sap2.ancestor(1, 5));
	}


	@Test
	public void testDigraph3() {
		assertEquals(2, sap3.length(1, 5));
	}


	@Test
	public void testDigraph3Anc() {
		assertEquals(1, sap3.ancestor(1, 5));
	}




	@Test
	public void testDigraph3FromDiffSubgraph() {
		assertEquals(-1, sap3.ancestor(1, 0));
		assertEquals(-1, sap3.length(1, 0));
	}


	
	@Test
	public void testDigraph4() {
		assertEquals(4, sap4.length(1, 9));
	}


	@Test
	public void testDigraph4Anc() {
		assertEquals(8, sap4.ancestor(1, 9));
	}


	@Test
	public void testDigraph5() {
		assertEquals(6, sap5.length(7, 19));
	}


	@Test
	public void testDigraph5Anc() {
		assertEquals(9, sap5.ancestor(7, 19));
	}


	@Test
	public void testDigraph6() {
		assertEquals(2, sap6.length(2, 7));
	}


	@Test
	public void testDigraph6Anc() {
		assertEquals(3, sap6.ancestor(2, 7));
	}


	
	@Test
	public void testDigraph7() {
		assertEquals(5, sap7.length(0, 6));
	}


	@Test
	public void testDigraph7Anc() {
		assertEquals(2, sap7.ancestor(0, 6));
	}
	
	
}
