package queue;

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
    // Post: queue.n' = queue.n + 1
    // queue.a[n]' = element && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static void enqueue(ArrayQueueADT queue, Object element) {
        queue.elements[queue.head] = element;
        queue.head = (queue.head + 1) % queue.elements.length;
        if ((queue.head + 1) % queue.elements.length == queue.tail) {
            resize(queue);
        }
    }

    // Pre: queue.n > 0
    // queue != null
    // Post: queue.n' = queue.n - 1
    // any i: 0 <= i < queue.n' => queue.a[i]' = queue.a[i + 1]
    // R = queue.a[0]
    public static Object queue(ArrayQueueADT queue) {
        Object val = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        if (size(queue) < queue.elements.length / 4) {
            resize(queue);
        }
        return val;
    }

    // Pre: element != null
    // queue != null
    // Post: n' = n + 1
    // a[0]' = element && any i: 0 < i <= n => a[i]' = a[i - 1]
    public static void push(ArrayQueueADT queue, Object element) {
        queue.tail = (queue.tail == 0 ? queue.elements.length - 1 : queue.tail - 1);
        queue.elements[queue.tail] = element;
        if ((queue.head + 1) % queue.elements.length == queue.tail) {
            resize(queue);
        }
    }

    // Pre: n > 0
    // queue != null
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i]
    // R = a[n - 1]
    public static Object remove(ArrayQueueADT queue) {
        queue.head = (queue.head == 0 ? queue.elements.length - 1 : queue.head - 1);
        Object val = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        if (size(queue) < queue.elements.length / 4) {
            resize(queue);
        }
        return val;
    }

    // Pre: queue != null
    // Post: n = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.tail = queue.head = 0;
    }

    // Pre: queue != null && queue.n > 0
    // Post: R = queue.a[0]
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.tail];
    }

    // Pre: 0 <= ind < n
    // queue != null
    // Post: R = a[ind]
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static Object get(ArrayQueueADT queue, int ind) {
        return queue.elements[(queue.tail + ind) % queue.elements.length];
    }

    // Pre: 0 <= ind < n
    // element != null
    // queue != null
    // Post: a[ind]' = element && any i: i != ind && 0 <= i < n => a[i]' = a[i]
    public static void set(ArrayQueueADT queue, int ind, Object element) {
        queue.elements[(queue.tail + ind) % queue.elements.length] = element;
    }

    // Pre: queue != null && queue.n > 0
    // Post: R = a[n - 1]
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[(queue.head == 0 ? queue.elements.length - 1 : queue.head - 1)];
    }

    // Pre: queue != null
    // Post: R = n
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static int size(ArrayQueueADT queue) {
        return queue.head >= queue.tail ? queue.head - queue.tail : queue.elements.length - queue.tail + queue.head;
    }

    // Pre: queue != null
    // Post: R = (n == 0)
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.head == queue.tail;
    }

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
