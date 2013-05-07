
public class BurrowsWheeler {
	// apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
    	String s = BinaryStdIn.readString();
    	
    	CircularSuffixArray cfa = new CircularSuffixArray(s);
    	int size = cfa.length();
    	//find index for which index[i] == 0
    	for (int i = 0; i < size; i++) {
    		if(cfa.index(i) == 0) {
    			BinaryStdOut.write(i);
    			break;
    		}
    	}
    	for (int i = 0; i < size; i++) {
    		//StdOut.println(i);
    		BinaryStdOut.write(s.charAt((size + cfa.index(i) - 1) % size));
    	}
    	
    	BinaryStdOut.close();
    	
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
    	int first = BinaryStdIn.readInt();
    	String s = BinaryStdIn.readString();
    	
    	char[] t = s.toCharArray();
    	char[] aux = s.toCharArray();
    	int next[] = new int[s.length()];
    	//Arrays.sort(firstCol);
    	
    	int R = 256;
    	int N = t.length;
    	int[] count = new int[R + 1];
    	
    	for (int i = 0; i < N; i++)
    		count[t[i]+1]++;
    	for (int r = 0; r < R; r++)
    		count[r+1] += count[r];
    	for (int i = 0; i < N; i++) {
    		aux[count[t[i]]] = t[i];
    		next[count[t[i]]] = i;
    		count[t[i]]++;
    	}
    	
    	//for (int i = 0; i < N; i++)
    	//	sorted[i] = aux[i];
    	
    	int ind = first;
    	for (int i = 0; i < N; i++) {
    		//BinaryStdOut.write(sorted[ind]);
    		BinaryStdOut.write(aux[ind]);
    		ind = next[ind];
    	}
    	BinaryStdOut.close();
    }
    

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
    	if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new RuntimeException("Illegal command line argument");
    }
}
