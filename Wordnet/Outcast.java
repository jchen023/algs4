import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException();
        int[] dist = new int[nouns.length];
        int max = 0;
        int id = 0;
        for (int i = 0; i < nouns.length; i++) {
            dist[i] = 0;
        }
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                dist[i] = dist[i] + wordnet.distance(nouns[i], nouns[j]);
            }
        }
        for (int i = 0; i < nouns.length; i++) {

            if (max <= dist[i]) {
                max = dist[i];
                id = i;
            }
        }
        return nouns[id];
    }


    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
