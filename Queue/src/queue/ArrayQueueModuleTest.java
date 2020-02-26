package queue;

public class ArrayQueueModuleTest {
    private static void print(ArrayQueue queue) {
        System.out.println("---");
        System.out.println("Is empty: " + queue.isEmpty());
        if (!queue.isEmpty()) {
            System.out.println("Element: " + queue.element());
        }
        System.out.println("Size: " + queue.size());
        System.out.println("---");
    }
    public static void main(String[] args) {
        final int N = 3;
        System.out.println("Enqueue test:");
        ArrayQueue queue = new ArrayQueue();
        for (int i = 1; i <= N; i++) {
            System.out.println("Added: " + i);
            queue.enqueue(i);
            print(queue);
        }
        System.out.println("Dequeue test: ");
        for (int i = 1; i <= N; i++) {
            System.out.println("Erased: " + queue.dequeue());
            print(queue);
        }
        System.out.println("Clear test: ");
        queue.clear();
        print(queue);
    }
}
