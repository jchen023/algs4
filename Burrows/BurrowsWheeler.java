import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String st = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(st);
        int length = csa.length();
        int[] index = new int[length];
        for (int i = 0; i < length; i++) {
            index[i] = csa.index(i);
            if (index[i] == 0) {
                BinaryStdOut.write(i);
            }
        }

        for (int i = 0; i < length; i++) {
            if (index[i] != 0) BinaryStdOut.write(st.charAt(index[i] - 1));
            else BinaryStdOut.write(st.charAt(length - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String st = BinaryStdIn.readString();
        char[] t1 = st.toCharArray();
        int length = t1.length;

        int[] count = new int[R + 1];
        int[] next = new int[length];

        for (int i = 0; i < length; i++)
            count[t1[i] + 1]++;
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < length; i++)
            next[count[t1[i]]++] = i;


        for (int i = 0; i < length; i++) {
            first = next[first];
            BinaryStdOut.write(t1[first]);

        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
