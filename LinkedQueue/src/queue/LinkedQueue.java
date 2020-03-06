package queue;

import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private Node tail;
    private Node head;

    @Override
    public void enqueue(Object element) {
        if (head == null) {
            head = tail = new Node(element);
        } else {
            Node newHead = new Node(element);
            head.setNext(newHead);
            newHead.setPrev(head);
            head = newHead;
        }
        size++;
    }

    @Override
    public Object dequeue() {
        Object val = tail.getElement();
        delNode(tail);
        return val;
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        Node cur = tail;
        while (cur != null) {
            if (!predicate.test(cur.getElement())) {
                delNode(cur);
            }
            cur = cur.getNext();
        }
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        Node cur = tail;
        int newSize = 0;
        while (cur != null && predicate.test(cur.getElement())) {
            newSize++;
            head = cur;
            cur = cur.getNext();
        }
        if (head != null) {
            head.setNext(null);
        }
        if (newSize == 0) {
            tail = head = null;
        }
        size = newSize;
    }

    @Override
    public void clear() {
        size = 0;
        tail = head = null;
    }

    @Override
    public Object element() {
        return tail.getElement();
    }

    private void delNode(Node cur) {
        if (cur.getPrev() != null) {
            cur.getPrev().setNext(cur.getNext());
        }
        if (cur.getNext() != null) {
            cur.getNext().setPrev(cur.getPrev());
        }
        if (cur == tail) {
            tail = tail.getNext();
        }
        if (cur == head) {
            head = head.getPrev();
        }
        size--;
    }
}
