import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BaseballElimination {
    private final int teamCount;
    private final String[] teams;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private final boolean[] eliminated;
    private final Bag<String>[] subsetR;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamCount = in.readInt();
        teams = new String[teamCount];
        w = new int[teamCount];
        l = new int[teamCount];
        r = new int[teamCount];
        g = new int[teamCount][teamCount];
        eliminated = new boolean[teamCount];
        subsetR = (Bag<String>[]) new Bag[teamCount];
        for (int i = 0; i < teamCount; i++) {
            teams[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            eliminated[i] = false;
            subsetR[i] = new Bag<String>();
            for (int j = 0; j < teamCount; j++) {
                g[i][j] = in.readInt();
            }
        }
        in = null;
        int s = teamCount;
        int t = teamCount + 1;

        int V = 2 + teamCount + (teamCount - 1) * (teamCount - 2) / 2;
        for (int i = 0; i < teamCount; i++) {
            FlowNetwork fn = new FlowNetwork(V);

            for (int j = 0; j < teamCount; j++) {
                if (i == j) continue;
                int capacity = w[i] + r[i] - w[j];
                if (capacity < 0) {
                    eliminated[i] = true;
                    subsetR[i].add(teams[j]);
                    break;
                }
                else {
                    fn.addEdge(new FlowEdge(j, t, capacity));
                }
            }
            if (eliminated[i]) {
                continue;
            }

            int start = t + 1;
            int totalCapacity = 0;
            for (int j = 0; j < teamCount - 1; j++) {
                if (j == i) continue;
                for (int k = j + 1; k < teamCount; k++) {
                    if (i == k) continue;
                    fn.addEdge(new FlowEdge(s, start, g[j][k]));
                    totalCapacity += g[j][k];
                    fn.addEdge(new FlowEdge(start, j, Double.POSITIVE_INFINITY));
                    fn.addEdge(new FlowEdge(start++, k, Double.POSITIVE_INFINITY));
                }
            }
            FordFulkerson ff = new FordFulkerson(fn, s, t);
            if (ff.value() == totalCapacity) {
                // StdOut.print(ff.value());
                // StdOut.println(totalCapacity);
                eliminated[i] = false;
            }
            else {
                eliminated[i] = true;
                for (int j = 0; j < teamCount; j++) {
                    if (i != j) if (ff.inCut(j))
                        subsetR[i].add(teams[j]);
                }
            }
        }
    }

    public int numberOfTeams() {
        return teamCount;
    }

    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    private int lookupteam(String team) {
        for (int i = 0; i < teamCount; i++) {
            if (team.equals(teams[i])) return i;
        }
        throw new IllegalArgumentException();
    }

    public int wins(String team) {
        return w[lookupteam(team)];
    }

    public int losses(String team) {
        return l[lookupteam(team)];
    }

    public int remaining(String team) {
        return r[lookupteam(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int t1 = lookupteam(team1);
        int t2 = lookupteam(team2);
        return g[t1][t2];
    }

    public boolean isEliminated(String team) {
        return eliminated[lookupteam(team)];
    }

    public Iterable<String> certificateOfElimination(String team) {
        int tm = lookupteam(team);
        if (!eliminated[tm]) return null;
        return subsetR[tm];

    }  // subset R of teams that eliminates given team; null if not eliminated

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
