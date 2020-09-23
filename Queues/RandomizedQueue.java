import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int count;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        count = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (count == queue.length) resize(2 * queue.length);
        queue[count++] = item;
    }

    private void resize(int size) {
        Item[] temp = (Item[]) new Object[size];
        for (int i = 0; i < count; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int i = StdRandom.uniform(count);
        Item swap = queue[i];
        queue[i] = queue[--count];
        queue[count] = null;
        if (count <= queue.length / 4 && queue.length >= 2) resize(queue.length / 2);
        return swap;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int i = StdRandom.uniform(count);
        return queue[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        Item[] it = (Item[]) new Object[count];
        private int pointer = 0;

        public ArrayIterator() {
            for (int i = 0; i < count; i++) {
                it[i] = queue[i];
            }
            StdRandom.shuffle(it);
        }

        public boolean hasNext() {
            return pointer < count;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return it[pointer++];
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        StdOut.println(rq.isEmpty());
        rq.enqueue("Hello");
        StdOut.println(rq.isEmpty());
        rq.enqueue("World");
        rq.enqueue("animals");
        rq.enqueue("unicorn");
        StdOut.println("Sample - " + rq.sample());
        StdOut.println("Dequeue - " + rq.dequeue());
        StdOut.println(rq.size());
        rq.enqueue("cats");
        StdOut.println("Dequeue - " + rq.dequeue());
        StdOut.println("Dequeue - " + rq.dequeue());
        StdOut.println("Sample - " + rq.sample());
        StdOut.println("Dequeue - " + rq.dequeue());
        StdOut.println(rq.isEmpty());
        StdOut.println("Dequeue - " + rq.dequeue());
        StdOut.println(rq.size());
        StdOut.println(rq.isEmpty());
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
