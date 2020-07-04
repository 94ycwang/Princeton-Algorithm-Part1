/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int N;
    private Item[] array;

    // construct an empty randomized queue
    public RandomizedQueue() {
        N = 0;
        array = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        array[N] = item;
        N++;
        if (N == array.length) resize(2 * array.length);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int index = StdRandom.uniform(N);
        Item item = array[index];
        array[index] = array[N - 1];
        array[N - 1] = null;
        N--;
        if (N > 0 && N <= array.length / 4) resize(array.length / 2);
        return item;
    }

    // resize arra;y when necessary
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = array[i];
        }
        array = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int index = StdRandom.uniform(N);
        return array[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new randq_iterator(array, N);
    }

    private class randq_iterator implements Iterator<Item> {
        private Item[] s;
        private int n;


        public randq_iterator(Item[] array, int N) {
            s = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) s[i] = array[i];
            n = N;
        }

        public boolean hasNext() {
            return n != 0;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            int index = StdRandom.uniform(n);
            Item item = s[index];
            s[index] = s[n - 1];
            n--;

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randque = new RandomizedQueue<>();
        StdOut.println("Is deque empty? " + randque.isEmpty());
        randque.enqueue(1);
        randque.enqueue(2);
        randque.enqueue(3);
        randque.enqueue(4);
        StdOut.println("Remove:" + randque.dequeue());
        StdOut.println("Deque size = " + randque.size());

        Iterator<Integer> it = randque.iterator();
        while (it.hasNext()) StdOut.println(it.next());

        StdOut.println("***");

        Iterator<Integer> it2 = randque.iterator();
        while (it2.hasNext()) StdOut.println(it2.next());

    }

}
