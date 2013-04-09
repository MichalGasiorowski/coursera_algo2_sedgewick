import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WordNet {
	
	private SAP wordNetSap;
	//private Set<String> wordNetNouns;
	private Map<String, Bag<Integer>> nounSynsetMap;
	private Map<Integer, String> idSynsetMap;
	//private Map<Integer, String> synsetDefinitionMap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		In synsetsIn = new In(synsets);
		String r;
		String[] tokens, nouns;
		Integer synId;
		nounSynsetMap = new HashMap<String, Bag<Integer>>();
		idSynsetMap = new HashMap<Integer, String>();
		//wordNetNouns = new HashSet<String>();
		//synsetDefinitionMap = new HashMap<Integer, String>();
		while (!synsetsIn.isEmpty()) {
			r = synsetsIn.readLine();
			//StdOut.printf("r[%s] \n",r);
			tokens = r.split(",");
			//for(String s: tokens)
			//	StdOut.printf("s:" + s + " ");
			//StdOut.println();
			nouns = tokens[1].split(" ");
			synId = Integer.parseInt(tokens[0]);
			Bag<String> nounBag = new Bag<String>();
			//idSynsetMap.put(synId, tokens[1]);
			//synsetDefinitionMap.put(synId, tokens[2]);
			for (String s : nouns) {
				nounBag.add(s);
				//wordNetNouns.add(s);
				if (!nounSynsetMap.containsKey(s)) {
					nounSynsetMap.put(s, new Bag<Integer>());
				}
				nounSynsetMap.get(s).add(synId);
			}
			idSynsetMap.put(synId, tokens[1]);
		}
		synsetsIn.close();
		Digraph G = new Digraph(idSynsetMap.size());
			
		In hypernymsIn = new In(hypernyms);
		while (!hypernymsIn.isEmpty()) {
			r = hypernymsIn.readLine();
			//StdOut.printf("r[%s] \n",r);
			tokens = r.split(",");
			int syn = Integer.parseInt(tokens[0]);
			for (int i = 1; i < tokens.length; i++) {
				G.addEdge(syn, Integer.parseInt(tokens[i]));
			}
		}
		hypernymsIn.close();
		DirectedCycle finder = new DirectedCycle(G);
		if (finder.hasCycle()) {
			throw new java.lang.IllegalArgumentException();
		}
		int outDegNum = 0;
		int tempCount;
		for (int v = 0; v < G.V(); v++) {
			tempCount = 0;
			for (int e : G.adj(v)) { tempCount++; }
			if (tempCount == 0) { outDegNum++; }
		}
		if (outDegNum != 1) { throw new java.lang.IllegalArgumentException(); }
		
		wordNetSap = new SAP(G);
	}
	
	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return new HashSet(nounSynsetMap.keySet());
	}
	
	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return nounSynsetMap.containsKey(word);
		
	}
	
	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException();
		}
		return wordNetSap.length(nounSynsetMap.get(nounA), 
				nounSynsetMap.get(nounB)); 	
	}
	
	// a synset (second field of synsets.txt) that is the 
	// common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException();
		}
		int anc = wordNetSap.ancestor(nounSynsetMap.get(nounA), 
				nounSynsetMap.get(nounB));
		return idSynsetMap.get(anc);
	}
	
	// for unit testing of this class
	public static void main(String[] args) {
		WordNet wordNet = new WordNet("data/wordnet/synsets15.txt",
				"data/wordnet/hypernymsPath15.txt");
		StdOut.println(wordNet.isNoun("a"));
		StdOut.println(wordNet.nouns().iterator().next());
		
	}
}
