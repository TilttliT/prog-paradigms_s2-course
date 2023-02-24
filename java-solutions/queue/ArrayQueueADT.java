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
public class ArrayQueueADT {
    private Object[] elements;
    private int head, size;

    public ArrayQueueADT() {
        elements = new Object[2];
    }

    public static ArrayQueueADT create() {
        final ArrayQueueADT queue = new ArrayQueueADT();
        queue.elements = new Object[2];
        return queue;
    }

    /**
     * Pred: element != null && queue != null
     * Post: n' = n + 1 && a[n'] = element && immutable(n)
     */
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);
        queue.elements[ArrayQueueADT.getTail(queue)] = element;
        queue.size++;
    }

    /**
     * Pred: n >= 1 && queue != null
     * Post: n' = n && immutable(n) && R = a[1]
     */
    public static Object element(final ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    /**
     * Pred: n >= 1
     * Post: n' = n - 1 && immutable(0, 1, n') && R = a[1]
     */
    public static Object dequeue(final ArrayQueueADT queue) {
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        --queue.size;
        queue.head = increment(queue, queue.head);
        return result;
    }

    /**
     * Pred: true && queue != null
     * Post: n' = n && immutable(n) && R = n
     */
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * Pred: true && queue != null
     * Post: R = (n == 0) && n' = n && immutable(n)
     */
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /**
     * Pred: true && queue != null
     * Post: n' = 0
     */
    public static void clear(final ArrayQueueADT queue) {
        queue.size = 0;
        queue.head = 0;
        queue.elements = new Object[2];
    }

    /**
     * Pred: element != null && queue != null
     * Post: n' = n + 1 && a[1] = element && immutable(1, 0, n)
     */
    public static void push(final ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);
        queue.head = decrement(queue, queue.head);
        queue.elements[queue.head] = element;
        queue.size++;
    }

    /**
     * Pred: n >= 1 && queue != null
     * Post: n' = n && immutable(n) && R = a[n]
     */
    public static Object peek(final ArrayQueueADT queue) {
        return queue.elements[getTailElement(queue)];
    }

    /**
     * Pred: n >= 1 && queue != null
     * Post: n' = n - 1 && immutable(n') && R = a[n]
     */
    public static Object remove(final ArrayQueueADT queue) {
        Object result = queue.elements[getTailElement(queue)];
        queue.elements[getTailElement(queue)] = null;
        --queue.size;
        return result;
    }

    /**
     * Pred: element != null && queue != null
     * Post: n' = n && immutable(n) &&
     *       if |A| != 0 then R = min{A} else R = -1
     *       A = {i из [1, n]: a[i] == element}
     */
    public static int indexOf(final ArrayQueueADT queue, Object element) {
        int tail = getTail(queue);
        for (int i = queue.head, j = 0; i != tail || j != queue.size; i = increment(queue, i), j++) {
            if (queue.elements[i].equals(element)) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Pred: element != null && queue != null
     * Post: n' = n && immutable(n) &&
     *       if |A| != 0 then R = max{A} else R = -1
     *       A = {i из [1, n]: a[i] == element}
     */
    public static int lastIndexOf(final ArrayQueueADT queue, Object element) {
        int preHead = decrement(queue, queue.head);
        for (int i = getTailElement(queue), j = queue.size - 1; i != preHead || j != -1;
             i = decrement(queue, i), --j) {

            if (queue.elements[i].equals(element)) {
                return j;
            }
        }
        return -1;
    }

    private static void ensureCapacity(final ArrayQueueADT queue, int size) {
        if (queue.elements.length < size) {
            if (queue.head != 0) {
                Object[] newElements = new Object[4*size];
                System.arraycopy(queue.elements, queue.head, newElements, 0, queue.elements.length - queue.head);
                System.arraycopy(queue.elements, 0, newElements, queue.elements.length - queue.head, queue.head);
                queue.elements = newElements;
                queue.head = 0;
            } else {
                queue.elements = Arrays.copyOf(queue.elements, 2*size);
            }
        }
    }

    private static int getTailElement(final ArrayQueueADT queue) {
        return decrement(queue, getTail(queue));
    }

    private static int getTail(final ArrayQueueADT queue) {
        int tail = queue.head + queue.size;
        if (tail >= queue.elements.length) {
            tail -= queue.elements.length;
        }
        return tail;
    }

    private static int increment(final ArrayQueueADT queue, int ind) {
        ind++;
        if (ind >= queue.elements.length) {
            ind -= queue.elements.length;
        }
        return ind;
    }

    private static int decrement(final ArrayQueueADT queue, int ind) {
        ind--;
        if (ind < 0) {
            ind += queue.elements.length;
        }
        return ind;
    }
}
