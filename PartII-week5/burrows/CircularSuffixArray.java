/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private int length;
    private Integer[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        length = s.length();
        indexes = new Integer[length];
        for (int i = 0; i < length; i++)
            indexes[i] = i;


        Arrays.sort(indexes, new Comparator<Integer>() {
            public int compare(Integer ind1, Integer ind2) {
                for (int k = 0, i = ind1, j = ind2; k < length; k++, i++, j++) {
                    if (i >= length) i -= length;
                    if (j >= length) j -= length;
                    if (s.charAt(i) != s.charAt(j))
                        return s.charAt(i) - s.charAt(j);
                }
                return 0;
            }
        });
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();
        return indexes[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String input = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(input);
        int n = suffixArray.length();
        for (int i = 0; i < input.length(); i++)
            if (suffixArray.index(i) == 0)
                BinaryStdOut.write(input.charAt(n - 1));
            else
                BinaryStdOut.write(input.charAt(suffixArray.index(i) - 1));
        BinaryStdOut.close();
    }

}
