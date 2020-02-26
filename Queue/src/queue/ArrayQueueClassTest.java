package queue;

import static queue.ArrayQueueModule.*;

public class ArrayQueueClassTest {
    private static void print() {
        System.out.println("---");
        System.out.println("Is empty: " + isEmpty());
        if (!isEmpty()) {
            System.out.println("Element: " + element());
        }
        System.out.println("Size: " + size());
        System.out.println("---");
    }
    public static void main(String[] args) {
        final int N = 3;
        System.out.println("Enqueue test:");
        for (int i = 1; i <= N; i++) {
            System.out.println("Added: " + i);
            enqueue(i);
            print();
        }
        System.out.println("Dequeue test: ");
        for (int i = 1; i <= N; i++) {
            System.out.println("Erased: " + dequeue());
            print();
        }
        System.out.println("Clear test: ");
        clear();
        print();
    }
}
