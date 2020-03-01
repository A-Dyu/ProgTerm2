package queue;

// Inv: a[0].. a[n - 1]
// n >= 0
// any i: 0 <= i < n => a[i] != null
public class ArrayQueueModule {
    private static int tail;
    private static int head;
    private static Object[] elements = new Object[2];

    // Pre: element != null
    // Post: n' = n + 1
    // a[n]' = element && any i: 0 <= i < n => a[i]' = a[i]
    public static void enqueue(Object element) {
        elements[head] = element;
        head = (head + 1) % elements.length;
        if ((head + 1) % elements.length == tail) {
            resize();
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i + 1]
    // R = a[0]
    public static Object dequeue() {
        Object val = elements[tail];
        elements[tail] = null;
        tail = (tail + 1) % elements.length;
        if (size() < elements.length / 4) {
            resize();
        }
        return val;
    }

    // Pre: element != null
    // Post: n' = n + 1
    // a[0]' = element && any i: 0 < i <= n => a[i]' = a[i - 1]
    public static void push(Object element) {
        tail = (tail == 0 ? elements.length - 1 : tail - 1);
        elements[tail] = element;
        if ((head + 1) % elements.length == tail) {
            resize();
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i]
    // R = a[n - 1]
    public static Object remove() {
        head = (head == 0 ? elements.length - 1 : head - 1);
        Object val = elements[head];
        elements[head] = null;
        if (size() < elements.length / 4) {
            resize();
        }
        return val;
    }

    // Post: n = 0
    public static void clear() {
        elements = new Object[2];
        tail = head = 0;
    }

    // Pre: n > 0
    // Post: R = a[0]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public static Object element() {
        return elements[tail];
    }

    // Pre: 0 <= ind < n
    // Post: R = a[ind]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public static Object get(int ind) {
        return elements[(tail + ind) % elements.length];
    }

    // Pre: 0 <= ind < n
    // element != null
    // Post: a[ind] = element
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public static void set(int ind, Object element) {
        elements[(tail + ind) % elements.length] = element;
    }

    // Pre: n > 0
    // Post: R = a[n - 1]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public static Object peek() {
        return elements[(head == 0 ? elements.length - 1 : head - 1)];
    }

    // Post: R = n
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public static int size() {
        return head >= tail ? head - tail : elements.length - tail + head;
    }

    // Post: R = (n == 0)
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public static boolean isEmpty() {
        return head == tail;
    }

    private static void resize() {
        Object[] nw = new Object[(size() + 1) * 2];
        if (head >= tail) {
            System.arraycopy(elements, tail, nw, 0, size());
        }   else {
            System.arraycopy(elements, tail, nw, 0, elements.length - tail);
            System.arraycopy(elements, 0, nw, elements.length - tail, head);
        }
        head = size();
        tail = 0;
        elements = nw;
    }
}
