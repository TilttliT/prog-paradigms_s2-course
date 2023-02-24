package queue;

/**
 * Model: a[1] .. a[n]
 * Invariant: n >= 0 && for i = 1 .. n: a[i] != null
 *
 * Let immutable(n): for i = 1 .. n: a'[i] = a[i]
 *     immutable(pos1, pos2, n): for i = 1 .. n: a'[pos1 + i] = a[pos2 + i]
 */
public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    private static class Node {
        private final Object element;
        private Node prev;

        public Node(Object element, Node prev) {
            this.element = element;
            this.prev = prev;
        }
    }

    @Override
    protected void enqueueImpl(Object element) {
        Node node = new Node(element, null);
        if (size == 0) {
            head = node;
        } else {
            tail.prev = node;
        }
        tail = node;
    }

    @Override
    protected Object dequeueImpl() {
        final Object result  = head.element;
        head = head.prev;
        return result;
    }

    @Override
    protected Object elementImpl() {
        return head.element;
    }

    @Override
    protected void clearImpl() {
        head = null;
        tail = null;
    }
}
