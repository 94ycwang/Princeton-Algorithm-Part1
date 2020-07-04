/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private int nt;
    private ArrayList<String> teams = new ArrayList<>();
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        nt = in.readInt();
        wins = new int[nt];
        losses = new int[nt];
        remaining = new int[nt];
        against = new int[nt][nt];
        for (int i = 0; i < nt; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < nt; j++) {
                against[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return nt;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    private int getIndex(String team) {
        if (team == null) throw new IllegalArgumentException();
        for (int i = 0; i < nt; i++)
            if (teams.get(i).equals(team))
                return i;
        throw new IllegalArgumentException();
    }

    // number of wins for given team
    public int wins(String team) {
        int ind = getIndex(team);
        return wins[ind];
    }

    // number of losses for given team
    public int losses(String team) {
        int ind = getIndex(team);
        return losses[ind];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int ind = getIndex(team);
        return remaining[ind];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int i = getIndex(team1);
        int j = getIndex(team2);
        return against[i][j];

    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int ind = getIndex(team);
        int v = ((nt - 2) * (nt - 2) + nt - 2) / 2;

        int wr = wins[ind] + remaining[ind];
        for (int i = 0; i < nt; i++) {
            if (wr - wins[i] < 0)
                return true;
        }

        FordFulkerson ff = maxFF(ind);
        for (int i = 1; i <= v; i++) {
            if (ff.inCut(i))
                return true;
        }
        return false;
    }

    // // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int ind = getIndex(team);
        int v = ((nt - 2) * (nt - 2) + nt - 2) / 2;
        int wr = wins[ind] + remaining[ind];
        ArrayList<String> res = new ArrayList<>();

        for (int i = 0; i < nt; i++) {
            if (wr - wins[i] < 0) {
                res.add(teams.get(i));
                return res;
            }
        }

        int add;
        FordFulkerson ff = maxFF(ind);
        for (int i = 0; i < nt; i++) {
            if (i != ind) {
                add = (i < ind) ? 1 : 0;
                if (ff.inCut(v + i + add))
                    res.add(teams.get(i));
            }
        }
        return (res.size() == 0) ? null : res;
    }

    private FordFulkerson maxFF(int ind) {
        int wr = wins[ind] + remaining[ind];
        int v = ((nt - 2) * (nt - 2) + nt - 2) / 2;
        int s = 0;
        int t = v + nt;
        FlowNetwork G = new FlowNetwork(v + nt + 1);

        int k = 1;
        for (int i = 0; i < nt; i++) {
            for (int j = i + 1; j < nt; j++) {
                if (i != ind && j != ind) {
                    FlowEdge e = new FlowEdge(s, k, against[i][j]);
                    G.addEdge(e);

                    int add = (i < ind) ? 1 : 0;
                    e = new FlowEdge(k, v + i + add, Double.POSITIVE_INFINITY);
                    G.addEdge(e);

                    add = (j < ind) ? 1 : 0;
                    e = new FlowEdge(k, v + j + add, Double.POSITIVE_INFINITY);
                    G.addEdge(e);

                    k += 1;
                }
            }
        }

        for (int i = 0; i < nt; i++) {
            if (i != ind) {
                int add = (i < ind) ? 1 : 0;
                FlowEdge e = new FlowEdge(v + i + add, t, wr - wins[i]);
                G.addEdge(e);
            }
        }

        return new FordFulkerson(G, s, t);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {

            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
