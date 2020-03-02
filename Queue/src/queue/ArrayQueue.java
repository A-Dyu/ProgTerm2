package queue;

// Inv: a[0].. a[n - 1]
// n >= 0
// any i: 0 <= i < n => a[i] != null
public class ArrayQueue {
    private int tail;
    private int size;
    private Object[] elements;

    public ArrayQueue() {
        elements = new Object[2];
    }

    // Pre: element != null
    // Post: n' = n + 1
    // a[n]' = element && any i: 0 <= i < n => a[i]' = a[i]
    public void enqueue(Object element) {
        elements[(tail + size) % elements.length] = element;
        size++;
        if (size == elements.length) {
            resize();
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i + 1]
    // R = a[0]
    public Object dequeue() {
        size--;
        Object val = elements[tail];
        elements[tail] = null;
        tail = (tail + 1) % elements.length;
        if (size < elements.length / 4) {
            resize();
        }
        return val;
    }

    // Pre: element != null
    // Post: n' = n + 1
    // a[0]' = element && any i: 0 < i <= n => a[i]' = a[i - 1]
    public void push(Object element) {
        size++;
        tail = (tail == 0 ? elements.length - 1 : tail - 1);
        elements[tail] = element;
        if (size == elements.length) {
            resize();
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i]
    // R = a[n - 1]
    public Object remove() {
        size--;
        Object val = elements[(tail + size) % elements.length];
        elements[(tail + size) % elements.length] = null;
        if (size < elements.length / 4) {
            resize();
        }
        return val;
    }

    // Post: n = 0
    public void clear() {
        elements = new Object[2];
        tail = size = 0;
    }

    // Pre: n > 0
    // Post: R = a[0]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public Object element() {
        return elements[tail];
    }

    // Pre: 0 <= ind < n
    //Post: R = a[ind]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public Object get(int ind) {
        return elements[(tail + ind) % elements.length];
    }

    // Pre: 0 <= ind < n
    // element != null
    // Post: a[ind] = element
    // n' = n && any i: 0 <= i < n && i != ind => a[i]' = a[i]
    public void set(int ind, Object element) {
        elements[(tail + ind) % elements.length] = element;
    }

    // Pre: n > 0
    // Post: R = a[n - 1]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public Object peek() {
        return elements[(tail + size - 1) % elements.length];
    }

    // Post: R = n
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public int size() {
        return size;
    }

    // Post: R = (n == 0)
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        Object[] nw = new Object[(size() + 1) * 2];
        if (tail + size < elements.length) {
            System.arraycopy(elements, tail, nw, 0, size());
        }   else {
            System.arraycopy(elements, tail, nw, 0, elements.length - tail);
            System.arraycopy(elements, 0, nw, elements.length - tail, (tail + size) % elements.length);
        }
        tail = 0;
        elements = nw;
    }
}
