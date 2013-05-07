
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
	private static int R = 256;
	
    private static void mtf(char[] moveToFront, char inChar, int charPos) {
    	char lastOne = ' ';
    	char temp = ' ';
    	lastOne = moveToFront[0];
		for(int j = 1; j <= charPos; j++) {
			temp = moveToFront[j];
			moveToFront[j] = lastOne;
			lastOne = temp;
		}
		moveToFront[0] = inChar;
    }
    
    public static void encode() {
    	char[] moveToFront = new char[R];
    	for (int r = 0; r < R; r++)
    		moveToFront[r] = (char)r;
    	int charPos = 0;
    	
    	String s = BinaryStdIn.readString();
    	for(int i = 0; i < s.length(); i++) {
    		char inChar = s.charAt(i);
    		for(int k = 0; k < R; k++) {
    			if(moveToFront[k] == inChar) {
    				charPos = k;
    				break;
    			}
    		}	
    		BinaryStdOut.write((char)charPos);
    		mtf(moveToFront, inChar, charPos);
    	}
    	BinaryStdOut.close();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	char[] moveToFront = new char[R];
    	for (int r = 0; r < R; r++)
    		moveToFront[r] = (char)r;
    	String s = BinaryStdIn.readString();
    	for(int k = 0; k < s.length(); k++) {
    		int ind = (int) s.charAt(k);
    		
    		BinaryStdOut.write(moveToFront[ind]);
    		
    		mtf(moveToFront, moveToFront[ind], ind);
    	}
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
    	if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new RuntimeException("Illegal command line argument");
    }
}