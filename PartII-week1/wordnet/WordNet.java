/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    private HashMap<String, HashSet<Integer>> wordsMap;
    private ArrayList<String> wordsList;
    private Digraph G;
    private SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);
        wordsMap = new HashMap<>();
        wordsList = new ArrayList<>();

        int num = 0;
        String line = inSynsets.readLine();
        while (line != null) {
            num++;
            String[] string = line.split(",");
            int ind = Integer.parseInt(string[0]);
            wordsList.add(ind, string[1]);
            HashSet<Integer> indSet;
            for (String s : string[1].split(" ")) {
                if (wordsMap.containsKey(s)) indSet = wordsMap.get(s);
                else indSet = new HashSet<Integer>();
                indSet.add(ind);
                wordsMap.put(s, indSet);
            }
            line = inSynsets.readLine();
        }

        G = new Digraph(num);
        line = inHypernyms.readLine();
        while (line != null) {
            String[] string = line.split(",");
            int v = Integer.parseInt(string[0]);
            for (int i = 1; i < string.length; i++) {
                int w = Integer.parseInt(string[i]);
                G.addEdge(v, w);
            }
            line = inHypernyms.readLine();
        }
        isRootedDAG();
        sap = new SAP(G);
    }

    private void isRootedDAG() {
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) throw new IllegalArgumentException("Not DAG!");
        int count = 0;
        for (int i = 0; i < G.V(); i++) {
            if (!G.adj(i).iterator().hasNext()) count++;
        }
        if (count > 1) throw new IllegalArgumentException("More than one root!");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null input!");
        return wordsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Not in wordnet!");
        return sap.length(wordsMap.get(nounA), wordsMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Not in wordnet!");
        int ancId = sap.ancestor(wordsMap.get(nounA), wordsMap.get(nounB));
        return wordsList.get(ancId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
    }
}
