
public class Outcast {
	// constructor takes a WordNet object
	private WordNet wordnet;
	
	
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int nSize = nouns.length;
		int maxDist = -1;
		int sumDist = 0;
		String ret = "";
		for (int cand = 0; cand < nSize; cand++) {
			sumDist = 0;
			for (int other = 0; other < nSize; other++) {
				if (cand == other) {
					continue;
				}
				sumDist += wordnet.distance(nouns[cand], nouns[other]);
			}
			if (sumDist > maxDist) {
				maxDist = sumDist;
				ret = nouns[cand];
			}
		}
		return ret;
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
		WordNet wordNet = new WordNet("data/wordnet/synsets.txt",
				"data/wordnet/hypernyms.txt");
		
		Outcast outcast = new Outcast(wordNet);
		String[] nouns = In.readStrings("data/wordnet/outcast5.txt");
		StdOut.println("outcast5.txt" + ": " + outcast.outcast(nouns));
			
	}
	
}
