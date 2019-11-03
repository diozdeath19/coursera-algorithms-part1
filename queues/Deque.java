/* *****************************************************************************
 *  Name: Deque
 *  Date: 8 June 2019, 17:19
 *  Description: Double-ended queue (Deque) implementation
 **************************************************************************** */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        public Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }

        public Item getItem() {
            return item;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
    private Node first;
    private Node last;
    private int n;

    /**
     * Construct an empty deque
     */
    public Deque() {
        first = null;
        last = null;
        n = 0;
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
     * Add the item to the front
     * @throws IllegalArgumentException - item is null
     * @param item - added item
     */
    public void addFirst(Item item)  {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        first = new Node(item, first, null);
        n++;
        if (n == 1) {
           last = first;
        }
    }

    /**
     * Add the item to the end
     * @throws IllegalArgumentException - item is null
     * @param item - added item
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldLast = last;
        Node newLast = new Node(item, null, oldLast);
        last = newLast;
        n++;
        if (oldLast != null) {
            oldLast.setNext(newLast);
        }
        if (n == 1) {
            first = last;
        }
    }

    /**
     * Remove and return the item from the front
     * @throws NoSuchElementException - deque is empty
     * @return removed item
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node newFirst = first.getNext();
        Item oldFirstItem = first.getItem();
        first = newFirst;
        n--;
        if (first != null) {
            first.setPrev(null);
        }
        if (isEmpty()) {
            last = null;
        }
        return oldFirstItem;
    }

    /**
     * Remove and return the item from the end
     * @throws NoSuchElementException if deque is empty
     * @return removed item
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item oldLastItem = last.getItem();
        last = last.getPrev();
        n--;
        if (last != null) {
            last.setNext(null);
        }
        if (isEmpty()) {
            first = null;
        }
        return oldLastItem;
    }

    //

    /**
     * Return an iterator over items in order from front to end
     * @return Iterator
     */
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(first);
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Node current;

        public DequeIterator(Node current) {
            this.current = current;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item oldCurrent = (Item) current.getItem();
            current = current.getNext();
            return oldCurrent;
        }
    }

    /**
     * Test
     * @param args
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(5);
        deque.addLast(6);
        deque.addFirst(4);

        System.out.println(deque.isEmpty());
        System.out.println(deque.size());

        deque.removeLast();
        deque.addLast(8);
        deque.addLast(9);
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.addLast(10);
        deque.removeLast();

        System.out.println(deque.isEmpty());
        System.out.println(deque.size());

        for (Integer val : deque) {
            System.out.println(val);
        }
    }
}
