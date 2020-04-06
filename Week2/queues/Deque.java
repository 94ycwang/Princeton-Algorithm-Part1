/* *****************************************************************************
 *  Name:
 *  Date: 03-22-2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private int N;
    private Node head;
    private Node tail;

    private class Node {
        Item item;
        Node pre = null;
        Node next = null;
    }

    // construct an empty deque
    public Deque() {
        N = 0;
        head = null;
        tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Null argument");

        Node headold = head;
        head = new Node();
        head.item = item;
        head.next = headold;

        if (isEmpty()) tail = head;
        else headold.pre = head;

        N++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Null argument");

        Node tailold = tail;
        tail = new Node();
        tail.item = item;
        tail.pre = tailold;

        if (isEmpty()) head = tail;
        else tailold.next = tail;
        N++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Deque is empty");

        Item item = head.item;
        head = head.next;
        N--;
        if (isEmpty()) tail = null;
        else head.pre = null;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Deque is empty");

        Item item = tail.item;
        tail = tail.pre;
        N--;
        if (isEmpty()) head = null;
        else tail.next = null;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new dqiterator(head);
    }

    private class dqiterator implements Iterator<Item> {
        private Node node;

        public dqiterator(Node head) {
            node = head;
        }

        public boolean hasNext() {
            return node != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No more items to return");

            Item item = node.item;
            node = node.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Unsupported operation");
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println("Is deque empty? " + deque.isEmpty());
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3);
        deque.addLast(4);
        StdOut.println("Remove first:" + deque.removeFirst());
        StdOut.println("Remove last:" + deque.removeLast());
        StdOut.println("Deque size = " + deque.size());
        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) StdOut.println(it.next());


    }

}
