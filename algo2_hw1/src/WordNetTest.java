
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;


public class WordNetTest {
	WordNet wordNet = new WordNet("data/wordnet/synsets15.txt",
			"data/wordnet/hypernymsPath15.txt");


	WordNet wordNetLarge = new WordNet("data/wordnet/synsets.txt",
			"data/wordnet/hypernyms.txt");


	
	@Test
	public void testConstructor() {
		assertTrue("a is a noun", wordNet.isNoun("a"));
	}
	
	@Test
	public void testAllNouns() {
		assertEquals("f", wordNet.nouns().iterator().next());
	}


	@Test
	public void testLarge() {
		assertEquals(7, wordNetLarge.distance("horse", "cat"));
	}
	
	@Test
	public void testLargeIsNoun() {
		assertTrue(wordNetLarge.isNoun("'s_Gravenhage"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDistanceInvalid() {
		assertEquals(7, wordNetLarge.distance("horse", "eleventeen"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSapInvalid() {
		assertEquals("", wordNetLarge.sap("horse", "eleventeen"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCyclesInvalid() {
		WordNet wordNetInvalidCycle = new WordNet("data/wordnet/synsets3.txt", "data/wordnet/hypernymsInvalidCycle.txt");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTwoRootsInvalid() {
		WordNet wordNetTwoRoots = new WordNet("data/wordnet/synsets3.txt", "data/wordnet/hypernymsInvalidTwoRoots.txt");
	}


}
