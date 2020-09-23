import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] asc = new char[R];
        for (char c = 0; c < R; c++) {
            asc[c] = c;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (int i = 0; i < R; i++) {
                if (c == asc[i]) {
                    BinaryStdOut.write(i, 8);
                    char t = asc[i];
                    for (int j = i; j > 0; j--) {
                        asc[j] = asc[j - 1];
                    }
                    asc[0] = t;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] asc = new char[R];
        for (char c = 0; c < R; c++) {
            asc[c] = c;
        }

        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readInt(8);
            char t = asc[c];
            BinaryStdOut.write(t);
            for (int i = c; i > 0; i--) {
                asc[i] = asc[i - 1];
            }
            asc[0] = t;
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");

    }
}
