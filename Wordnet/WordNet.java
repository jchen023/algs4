import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    private final HashMap<String, List<Integer>> synnoun;
    private final HashMap<Integer, String> synnounrev;
    private final SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        synnoun = new HashMap<String, List<Integer>>();
        synnounrev = new HashMap<Integer, String>();
        In synsetin = new In(synsets);
        while (!synsetin.isEmpty()) {
            String line = synsetin.readLine();
            String[] components = line.split(",");
            int id = Integer.parseInt(components[0]);
            synnounrev.put(id, components[1]);
            String[] s = components[1].split(" ");
            for (int i = 0; i < s.length; i++) {
                if (!synnoun.containsKey(s[i])) synnoun.put(s[i], new ArrayList<Integer>());
                synnoun.get(s[i]).add(id);
            }
        }
        synsetin.close();

        Digraph nouns = new Digraph(synnounrev.size());

        In hypernymsin = new In(hypernyms);
        while ((!hypernymsin.isEmpty())) {
            String line = hypernymsin.readLine();
            String[] components = line.split(",");
            int hyponym = Integer.parseInt(components[0]);
            for (int i = 1; i < components.length; i++) {
                int hypern = Integer.parseInt(components[i]);
                nouns.addEdge(hyponym, hypern);
            }
        }

        hypernymsin.close();

        sap = new SAP(nouns);

        DirectedCycle dc = new DirectedCycle(nouns);
        if (dc.hasCycle()) throw new IllegalArgumentException();

        int count = 0;
        for (int v = 1; v < nouns.V(); v++) {
            if (nouns.outdegree(v) == 0) count++;
        }
        if (count > 1) throw new IllegalArgumentException();
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synnoun.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        else return synnoun.containsKey(word);
    }


    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB))) throw new IllegalArgumentException();
        List<Integer> a = synnoun.get(nounA);
        List<Integer> b = synnoun.get(nounB);
        return sap.length(a, b);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB))) throw new IllegalArgumentException();
        List<Integer> a = synnoun.get(nounA);
        List<Integer> b = synnoun.get(nounB);
        int id = sap.ancestor(a, b);
        if (id == -1) return "No Ancestor";
        return synnounrev.get(id);
    }

    public static void main(String[] args) {
    }
}
