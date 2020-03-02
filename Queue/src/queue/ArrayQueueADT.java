package queue;

// Inv: a[0].. a[n - 1]
// n >= 0
// any i: 0 <= i < n => a[i] != null
public class ArrayQueueADT {
    private int tail;
    private int size;
    private Object[] elements;

    public ArrayQueueADT() {
        elements = new Object[2];
    }

    // Pre: element != null
    // queue != null
    // Post: queue.n' = queue.n + 1
    // queue.a[n]' = element && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static void enqueue(ArrayQueueADT queue, Object element) {
        queue.elements[(queue.tail + queue.size) % queue.elements.length] = element;
        queue.size++;
        if (queue.size == queue.elements.length) {
            resize(queue);
        }
    }

    // Pre: queue.n > 0
    // queue != null
    // Post: queue.n' = queue.n - 1
    // any i: 0 <= i < queue.n' => queue.a[i]' = queue.a[i + 1]
    // R = queue.a[0]
    public static Object dequeue(ArrayQueueADT queue) {
        Object val = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        queue.size--;
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
        queue.size++;
        if (queue.size == queue.elements.length) {
            resize(queue);
        }
    }

    // Pre: n > 0
    // queue != null
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i]
    // R = a[n - 1]
    public static Object remove(ArrayQueueADT queue) {
        queue.size--;
        Object val = queue.elements[(queue.tail + queue.size) % queue.elements.length];
        queue.elements[(queue.tail + queue.size) % queue.elements.length] = null;
        if (queue.size < queue.elements.length / 4) {
            resize(queue);
        }
        return val;
    }

    // Pre: queue != null
    // Post: n = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.tail = queue.size = 0;
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
        return queue.elements[(queue.tail + queue.size - 1) % queue.elements.length];
    }

    // Pre: queue != null
    // Post: R = n
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: queue != null
    // Post: R = (n == 0)
    // queue.n' = queue.n && any i: 0 <= i < queue.n => queue.a[i]' = queue.a[i]
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    private static void resize(ArrayQueueADT queue) {
        Object[] nw = new Object[(size(queue) + 1) * 2];
        if (queue.tail + queue.size < queue.elements.length) {
            System.arraycopy(queue.elements, queue.tail, nw, 0, size(queue));
        }   else {
            System.arraycopy(queue.elements, queue.tail, nw, 0, queue.elements.length - queue.tail);
            System.arraycopy(queue.elements, 0,
                    nw, queue.elements.length - queue.tail,
                    (queue.tail + queue.size) % queue.elements.length);
        }
        queue.tail = 0;
        queue.elements = nw;
    }
}
