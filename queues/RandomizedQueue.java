/* *****************************************************************************
 *  Name: RandomizedQueue
 *  Date: 9 June 2019, 09:07
 *  Description: Randomized queue implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] nodes;
    private int n;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        n = 0;
        nodes = (Item[]) new Object[1];
    }

    /**
     * Is Deque empty?
     * @return boolean
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Return the number of items on the deque
     * @return size
     */
    public int size() {
        return n;
    }

    /**
     * Enqueue item
     * @throws IllegalArgumentException - item is null
     * @param item - item
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        nodes[n++] = item;
        if (n == nodes.length) {
            resize(n*2);
        }
    }

    private void resize(int capacity) {
        Item[] resizedNodes = (Item[]) new Object[capacity];
        int end = capacity > nodes.length ? nodes.length : capacity;
        for (int i = 0; i < end; i++) {
            resizedNodes[i] = nodes[i];
        }
        nodes = resizedNodes;
    }

    /**
     * Enqueue item
     * @throws NoSuchElementException - queue is mepty
     * @return item - deleted item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(0, n);
        Item item = nodes[index];
        nodes[index] = nodes[n-1];
        nodes[n-1] = null;
        n--;
        if (n > 0 && n == nodes.length / 4) {
            resize(nodes.length / 2);
        }
        return item;
    }

    /**
     * Return a random item (but do not remove it)
     * @throws NoSuchElementException - queue is empty
     * @return sample item
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(0, n);
        return nodes[index];
    }

    /**
     * Iterator through randomized queue
     * @return iterator
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<Item>();
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private int curIndex;
        private Item[] shuffledNodes;

        public RandomizedQueueIterator() {
            shuffledNodes = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                shuffledNodes[i] = (Item) nodes[i];
            }
            StdRandom.shuffle(shuffledNodes);
            this.curIndex = 0;
        }

        public boolean hasNext() {
            return curIndex != n;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return shuffledNodes[curIndex++];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7);
        queue.enqueue(8);
        for (Integer item: queue) {
            System.out.println(item);
        }
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
    }
}
