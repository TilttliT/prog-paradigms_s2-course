package queue;

import java.util.Arrays;
import java.util.Objects;

/**
 * Model: a[1] .. a[n]
 * Invariant: n >= 0 && for i = 1 .. n: a[i] != null
 *
 * Let immutable(n): for i = 1 .. n: a'[i] = a[i]
 *     immutable(pos1, pos2, n): for i = 1 .. n: a'[pos1 + i] = a[pos2 + i]
 */
public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int head;

    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity(size + 1);
        elements[getTail()] = element;
    }

    @Override
    protected Object dequeueImpl() {
        Object result = elements[head];
        elements[head] = null;
        head = increment(head);
        return result;
    }

    @Override
    protected Object elementImpl() {
        return elements[head];
    }

    @Override
    protected void clearImpl() {
        head = 0;
        elements = new Object[2];
    }

    private void ensureCapacity(int size) {
        if (elements.length < size) {
            if (head != 0) {
                Object[] newElements = new Object[4*size];
                System.arraycopy(elements, head, newElements, 0, elements.length - head);
                System.arraycopy(elements, 0, newElements, elements.length - head, head);
                elements = newElements;
                head = 0;
            } else {
                elements = Arrays.copyOf(elements, 2*size);
            }
        }
    }

    private int getTail() {
        int tail = head + size;
        if (tail >= elements.length) {
            tail -= elements.length;
        }
        return tail;
    }

    private int increment(int ind) {
        ind++;
        if (ind >= elements.length) {
            ind -= elements.length;
        }
        return ind;
    }

    /**
     * Pred: element != null
     * Post: n' = n + 1 && a[1] = element && immutable(1, 0, n)
     */
    public void push(final Object element) {
        ensureCapacity(size + 1);
        head = decrement(head);
        elements[head] = element;
        size++;
    }

    /**
     * Pred: n >= 1
     * Post: n' = n && immutable(n) && R = a[n]
     */
    public Object peek() {
        return elements[getTailElement()];
    }

    /**
     * Pred: n >= 1
     * Post: n' = n - 1 && immutable(n') && R = a[n]
     */
    public Object remove() {
        Object result = elements[getTailElement()];
        elements[getTailElement()] = null;
        --size;
        return result;
    }

    /**
     * Pred: element != null
     * Post: n' = n && immutable(n) &&
     *       if |A| != 0 then R = min{A} else R = -1
     *       A = {i из [1, n]: a[i] == element}
     */
    public int indexOf(Object element) {
        int tail = getTail();
        for (int i = head, j = 0; i != tail || j != size; i = increment(i), j++) {
            if (elements[i].equals(element)) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Pred: element != null
     * Post: n' = n && immutable(n) &&
     *       if |A| != 0 then R = max{A} else R = -1
     *       A = {i из [1, n]: a[i] == element}
     */
    public int lastIndexOf(Object element) {
        int preHead = decrement(head);
        for (int i = getTailElement(), j = size - 1; i != preHead || j != -1; i = decrement(i), --j) {
            if (elements[i].equals(element)) {
                return j;
            }
        }
        return -1;
    }

    private int getTailElement() {
        return decrement(getTail());
    }

    private int decrement(int ind) {
        ind--;
        if (ind < 0) {
            ind += elements.length;
        }
        return ind;
    }
}

