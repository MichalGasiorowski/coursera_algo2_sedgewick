import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class OutcastTest {
	WordNet wordNet = new WordNet("data/wordnet/synsets.txt",
			"data/wordnet/hypernyms.txt");

	@Test
	public void testOutcast5() {
		Outcast outcast = new Outcast(wordNet);
		String[] nouns = In.readStrings("data/wordnet/outcast5.txt");
		assertEquals("table", outcast.outcast(nouns));
	}

	@Test
	public void testOutcast8() {
		Outcast outcast = new Outcast(wordNet);
		String[] nouns = In.readStrings("data/wordnet/outcast8.txt");
		assertEquals("bed", outcast.outcast(nouns));
	}

	
	@Test
	public void testOutcast11() {
		Outcast outcast = new Outcast(wordNet);
		String[] nouns = In.readStrings("data/wordnet/outcast11.txt");
		assertEquals("potato", outcast.outcast(nouns));
	}
	
	//File contains a single noun	
        @Test
	public void testOutcast1() {
		Outcast outcast = new Outcast(wordNet);
		String[] nouns = In.readStrings("data/wordnet/outcast1.txt");
		assertEquals("Turing", outcast.outcast(nouns));
	}


}
