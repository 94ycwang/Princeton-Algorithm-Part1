/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {


    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(input);
        int n = suffixArray.length();


        for (int i = 0; i < input.length(); i++)
            if (suffixArray.index(i) == 0)
                BinaryStdOut.write(i, 32);

        for (int i = 0; i < input.length(); i++)
            if (suffixArray.index(i) == 0)
                BinaryStdOut.write(input.charAt(n - 1));
            else BinaryStdOut.write(input.charAt(suffixArray.index(i) - 1));

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt(32);
        String t = BinaryStdIn.readString();

        int r = 256;
        int[] count = new int[r + 1];
        for (int i = 0; i < t.length(); i++) count[t.charAt(i) + 1] += 1;
        for (int i = 1; i < r; i++) count[i] += count[i - 1];

        char[] initial = new char[t.length()];
        int[] next = new int[t.length()];
        for (int i = 0; i < t.length(); i++) {
            int p = count[t.charAt(i)]++;
            initial[p] = t.charAt(i);
            next[p] = i;
        }

        for (int i = 0; i < t.length(); i++) {
            BinaryStdOut.write(initial[first]);
            first = next[first];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) BurrowsWheeler.transform();
        if (args[0].equals("+")) BurrowsWheeler.inverseTransform();
    }

}
