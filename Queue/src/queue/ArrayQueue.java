package queue;

// Inv: a[0].. a[n - 1]
// n >= 0
// any i: 0 <= i < n => a[i] != null
public class ArrayQueue {
    private int tail;
    private int head;
    private Object[] elements;

    public ArrayQueue() {
        elements = new Object[2];
        tail = head = 0;
    }

    // Pre: element != null
    public void enqueue(Object element) {
        elements[head] = element;
        head = (head + 1) % elements.length;
        if ((head + 1) % elements.length == tail) {
            resize();
        }
    }
    // Post: n' = n + 1
    // a[n]' = element && any i: 0 <= i < n => a[i]' = a[i]

    // Pre: n > 0
    public Object dequeue() {
        Object val = elements[tail];
        elements[tail] = null;
        tail = (tail + 1) % elements.length;
        if (size() < elements.length / 4) {
            resize();
        }
        return val;
    }
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i + 1]
    // R = a[0]

    public void clear() {
        elements = new Object[2];
        tail = head = 0;
    }
    // Post: n = 0

    public Object element() {
        return elements[tail];
    }
    // Post: R = a[0]

    public int size() {
        return head >= tail ? head - tail : elements.length - tail + head;
    }
    // Post: R = n

    public boolean isEmpty() {
        return head == tail;
    }
    // Post: R = (n == 0)

    private void resize() {
        Object[] nw = new Object[(size() + 1) * 2];
        for (int i = 0; i < size(); i++) {
            nw[i] = elements[(tail + i) % elements.length];
        }
        head = size();
        tail = 0;
        elements = nw;
    }
}
