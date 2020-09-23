public class CircularSuffixArray {
    private final int length;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        length = s.length();
        index = new int[length];
        int[] aux = new int[length];
        int[] indexAux = new int[length];
        int R = 256;


        for (int i = 0; i < length; i++) {
            index[i] = i;
        }


        for (int d = length - 1; d >= 0; d--) {
            int count[] = new int[R + 1];
            for (int i = 0; i < length; i++)
                count[s.charAt((d + index[i]) % length) + 1]++;
            for (int i = 0; i < R; i++)
                count[i + 1] += count[i];
            for (int i = 0; i < length; i++)
                aux[count[s.charAt((d + index[i]) % length)]++] = index[i];
            for (int i = 0; i < length; i++)
                index[i] = aux[i];
        }
    }


    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray CSA = new CircularSuffixArray("ABRACADABRA!");
        //StdOut.println(CSA.index(3));
    }

}
