package queue;

class Node {
    private final Object element;
    private Node next;
    private Node prev;

    public Node(Object element) {
        this.element = element;
    }

    public Object getElement() {
        return element;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
