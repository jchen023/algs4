import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private int count;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }

    // construct an empty deque
    public Deque() {
        count = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (!isEmpty()) {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        else last = first;
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (!isEmpty()) {
            oldlast.next = last;
            last.prev = oldlast;
        }
        else first = last;
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node oldfirst = first;
        first = first.next;
        count--;
        if (isEmpty()) last = null;
        else first.prev = null;
        return oldfirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node oldlast = last;
        last = oldlast.prev;
        count--;
        if (isEmpty()) first = null;
        else last.next = null;
        return oldlast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        StdOut.println(deque.isEmpty());
        deque.addFirst("Hello");
        StdOut.println(deque.isEmpty());
        deque.addLast("World");
        deque.addFirst("animals");
        deque.addLast("unicorn");
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.size());
        deque.addFirst("cats");
        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
    }
}
