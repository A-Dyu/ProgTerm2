package queue;

import static queue.ArrayQueueADT.*;

public class ArrayQueueADTTest {
    private static void print(ArrayQueueADT queue) {
        System.out.println("---");
        System.out.println("Is empty: " + isEmpty(queue));
        if (!isEmpty(queue)) {
            System.out.println("Element: " + element(queue));
        }
        System.out.println("Size: " + size(queue));
        System.out.println("---");
    }
    public static void main(String[] args) {
        final int N = 3;
        System.out.println("Enqueue test:");
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 1; i <= N; i++) {
            System.out.println("Added: " + i);
            enqueue(queue, i);
            print(queue);
        }
        System.out.println("Dequeue test: ");
        for (int i = 1; i <= N; i++) {
            System.out.println("Erased: " + dequeue(queue));
            print(queue);
        }
        System.out.println("Clear test: ");
        clear(queue);
        print(queue);
    }
}
