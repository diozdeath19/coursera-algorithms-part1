/* *****************************************************************************
 *  Name: Permutation
 *  Date: 9 June, 21:04
 *  Description: Permutation class implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        if (args.length < 1) {
           throw new IllegalArgumentException();
        }
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        if (k > queue.size()) {
            throw new IllegalArgumentException();
        }

        int i = 0;
        for (String item: queue) {
            System.out.println(item);
            if (++i == k) {
               return;
            }
        }
    }
}
