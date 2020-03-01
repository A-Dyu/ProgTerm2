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
    public static void enqueue(ArrayQueueADT deque, Object element) {
        deque.elements[deque.head] = element;
        deque.head = (deque.head + 1) % deque.elements.length;
        if ((deque.head + 1) % deque.elements.length == deque.tail) {
            resize(deque);
        }
    }
    // Post: queue.n' = queue.n + 1
    // queue.a[n]' = element && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]

    // Pre: queue.n > 0
    // queue != null
    public static Object dequeue(ArrayQueueADT deque) {
        Object val = deque.elements[deque.tail];
        deque.elements[deque.tail] = null;
        deque.tail = (deque.tail + 1) % deque.elements.length;
        if (size(deque) < deque.elements.length / 4) {
            resize(deque);
        }
        return val;
    }
    // Post: queue.n' = queue.n - 1
    // any i: 0 <= i < queue.n' => queue.a[i]' = queue.a[i + 1]
    // R = queue.a[0]

    // Pre: element != null
    // queue != null
    public static void push(ArrayQueueADT queue, Object element) {
        queue.tail = (queue.tail == 0 ? queue.elements.length - 1 : queue.tail - 1);
        queue.elements[queue.tail] = element;
        if ((queue.head + 1) % queue.elements.length == queue.tail) {
            resize(queue);
        }
    }
    // Post: n' = n + 1
    // a[0]' = element && any i: 0 < i <= n => a[i]' = a[i - 1]

    // Pre: n > 0
    // deque != null
    public static Object remove(ArrayQueueADT deque) {
        deque.head = (deque.head == 0 ? deque.elements.length - 1 : deque.head - 1);
        Object val = deque.elements[deque.head];
        deque.elements[deque.head] = null;
        if (size(deque) < deque.elements.length / 4) {
            resize(deque);
        }
        return val;
    }
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i]
    // R = a[n - 1]

    // Pre: deque != null
    public static void clear(ArrayQueueADT deque) {
        deque.elements = new Object[2];
        deque.tail = deque.head = 0;
    }
    // Post: n = 0

    // Pre: deque != null
    public static Object element(ArrayQueueADT deque) {
        return deque.elements[deque.tail];
    }
    // Post: R = deque.a[0]

    // Pre: 0 <= ind < n
    // deque != null
    public static Object get(ArrayQueueADT deque, int ind) {
        return deque.elements[(deque.tail + ind) % deque.elements.length];
    }
    //Post: R = a[ind]

    // Pre: 0 <= ind < n
    // element != null
    // deque != null
    public static void set(ArrayQueueADT deque, int ind, Object element) {
        deque.elements[(deque.tail + ind) % deque.elements.length] = element;
    }
    // Post: a[ind] = element

    // Pre: deque != null
    public static Object peek(ArrayQueueADT deque) {
        return deque.elements[(deque.head == 0 ? deque.elements.length - 1 : deque.head - 1)];
    }
    // Post: R = a[n - 1]

    // Pre: deque != null
    public static int size(ArrayQueueADT deque) {
        return deque.head >= deque.tail ? deque.head - deque.tail : deque.elements.length - deque.tail + deque.head;
    }
    // Post: R = n

    // Pre: deque != null
    public static boolean isEmpty(ArrayQueueADT deque) {
        return deque.head == deque.tail;
    }
    // Post: R = (n == 0)

    private static void resize(ArrayQueueADT deque) {
        Object[] nw = new Object[(size(deque) + 1) * 2];
        if (deque.head >= deque.tail) {
            System.arraycopy(deque.elements, deque.tail, nw, 0, size(deque));
        }   else {
            System.arraycopy(deque.elements, deque.tail, nw, 0, deque.elements.length - deque.tail);
            System.arraycopy(deque.elements, 0, nw, deque.elements.length - deque.tail, deque.head);
        }
        deque.head = size(deque);
        deque.tail = 0;
        deque.elements = nw;
    }
}
