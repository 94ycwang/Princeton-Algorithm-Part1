/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G.V());
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                this.G.addEdge(v, w);
            }
        }
    }

    private int[] shortest(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        int shortestLength = Integer.MAX_VALUE;
        int anc = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dist = bfsv.distTo(i) + bfsw.distTo(i);
                if (dist < shortestLength) {
                    shortestLength = dist;
                    anc = i;
                }
            }
        }

        int[] res = new int[2];
        res[0] = anc;
        if (anc == -1) res[1] = -1;
        else res[1] = shortestLength;
        return res;
    }

    private int[] shortest(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        int shortestLength = Integer.MAX_VALUE;
        int anc = -1;
        for (Integer i : v) {
            for (Integer j : w) {
                if (i == null || j == null) throw new IllegalArgumentException();
                int[] res = shortest(i, j);
                if (res[1] != -1 && res[1] < shortestLength) {
                    shortestLength = res[1];
                    anc = res[0];
                }
            }
        }

        int[] res = new int[2];
        res[0] = anc;
        if (anc == -1) res[1] = -1;
        else res[1] = shortestLength;
        return res;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] res = shortest(v, w);
        return res[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] res = shortest(v, w);
        return res[0];
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = shortest(v, w);
        return res[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = shortest(v, w);
        return res[0];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
