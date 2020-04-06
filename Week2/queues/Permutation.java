/* *****************************************************************************
 *  Name:
 *  Date: 03-23-2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {

        int num = Integer.parseInt(args[0]);
        RandomizedQueue<String> s = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) s.enqueue(StdIn.readString());
        Iterator<String> it = s.iterator();
        for (int i = 0; i < num; i++) StdOut.println(it.next());

    }
}
