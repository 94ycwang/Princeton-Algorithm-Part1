/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private final static int R = 256;

    public static void encode() {
        char[] value = new char[R];
        for (char i = 0; i < R; i++) value[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            char val = BinaryStdIn.readChar();
            char j;
            for (j = 0; j < R; j++) {
                if (value[j] == val) {
                    BinaryStdOut.write(j);
                    break;
                }
            }
            char tmp = value[j];
            for (int k = j; k > 0; k--) value[k] = value[k - 1];
            value[0] = tmp;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] value = new char[R];
        for (char i = 0; i < R; i++) value[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            int posi = BinaryStdIn.readInt(8);
            BinaryStdOut.write(value[posi]);
            char tmp = value[posi];
            for (int k = posi; k > 0; k--) value[k] = value[k - 1];
            value[0] = tmp;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) MoveToFront.encode();
        if (args[0].equals("+")) MoveToFront.decode();
    }

}
