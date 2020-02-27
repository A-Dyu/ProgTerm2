package queue;

import java.util.Arrays;

// Inv: a[0].. a[n - 1]
// n >= 0
// any i: 0 <= i < n => a[i] != null
public class ArrayQueueADT {
    private int tail;
    private int head;
    private Object[] elements;

    public ArrayQueueADT() {
        elements = new Object[2];
    }

    // Pre: element != null
    // queue != null
    public static void enqueue(ArrayQueueADT queue, Object element) {
        queue.elements[queue.head] = element;
        queue.head = (queue.head + 1) % queue.elements.length;
        if ((queue.head + 1) % queue.elements.length == queue.tail) {
            resize(queue);
        }
    }
    // Post: n' = n + 1
    // a[n]' = element && any i: 0 <= i < n => a[i]' = a[i]

    // Pre: n > 0
    // queue != null
    public static Object dequeue(ArrayQueueADT queue) {
        Object val = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        if (size(queue) < queue.elements.length / 4) {
            resize(queue);
        }
        return val;
    }
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i + 1]
    // R = a[0]

    // Pre: queue != null
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.tail = queue.head = 0;
    }
    // Post: n = 0

    // Pre: queue != null
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.tail];
    }
    // Post: R = a[0]

    // Pre: queue != null
    public static int size(ArrayQueueADT queue) {
        return queue.head >= queue.tail ? queue.head - queue.tail : queue.elements.length - queue.tail + queue.head;
    }
    // Post: R = n

    // Pre: queue != null
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.head == queue.tail;
    }
    // Post: R = (n == 0)

    private static void resize(ArrayQueueADT queue) {
        Object[] nw = new Object[(size(queue) + 1) * 2];
        if (queue.head >= queue.tail) {
            System.arraycopy(queue.elements, queue.tail, nw, 0, size(queue));
        }   else {
            System.arraycopy(queue.elements, queue.tail, nw, 0, queue.elements.length - queue.tail);
            System.arraycopy(queue.elements, 0, nw, queue.elements.length - queue.tail, queue.head);
        }
        queue.head = size(queue);
        queue.tail = 0;
        queue.elements = nw;
    }
}
