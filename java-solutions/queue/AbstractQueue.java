package queue;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Model: a[1] .. a[n]
 * Invariant: n >= 0 && for i = 1 .. n: a[i] != null
 *
 * Let immutable(n): for i = 1 .. n: a'[i] = a[i]
 *     immutable(pos1, pos2, n): for i = 1 .. n: a'[pos1 + i] = a[pos2 + i]
 */
public abstract class AbstractQueue implements Queue {
    protected int size;

    public AbstractQueue() {
        clear();
    }

    /**
     * Pred: true
     * Post: n' = n && immutable(n) && R = n
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Pred: true
     * Post: R = (n == 0) && n' = n && immutable(n)
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Pred: n >= 1
     * Post: n' = n - 1 && immutable(0, 1, n') && R = a[1]
     */
    @Override
    public Object dequeue() {
        --size;

        return dequeueImpl();
    }

    /**
     * Pred: element != null
     * Post: n' = n + 1 && a[n'] = element && immutable(n)
     */
    @Override
    public void enqueue(final Object element) {
        enqueueImpl(element);
        size++;
    }

    /**
     * Pred: n >= 1
     * Post: n' = n && immutable(n) && R = a[1]
     */
    @Override
    public final Object element() {
        return elementImpl();
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n если predicate.test(a[i]) == false, тогда a[i] должен принадлежать a' пусть это a'[i'].
     *       Получаем отображение f(i) = i' и f^-1(i') = i.
     *       Тогда for i = 1 .. n' : f^-1(i' - 1) < f^-1(i') < f^-1(i' + 1)
     *       a' in a
     */
    @Override
    public void removeIf(Predicate<Object> predicate) {
        ifWhileFunction((obj, flag) -> !predicate.test(obj), true);
    }

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n если predicate.test(a[i]) == true, тогда a[i] должен принадлежать a' пусть это a'[i'].
     *       Получаем отображение f(i) = i' и f^-1(i') = i.
     *       f^-1(0) < f^-1(1)
     *       Тогда for i = 2 .. n' - 1 : f^-1(i' - 1) < f^-1(i') < f^-1(i' + 1)
     *       f^-1(n' - 1) < f^-1(n')
     *       a' in a
     */
    @Override
    public void retainIf(Predicate<Object> predicate) {
        ifWhileFunction((obj, flag) -> predicate.test(obj), true);
    }

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n' predicate.test(a[i]) == true и a'[i] == a[i]
     *       n' == n или predicate.test(a[n' + 1]) == false
     */
    @Override
    public void takeWhile(Predicate<Object> predicate) {
        ifWhileFunction((obj, flag) -> flag && predicate.test(obj), true);
    }

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n - n' predicate.test(a[i]) == true
     *       n' == n или predicate.test(a[n' + 1]) == false
     *       for i = (n - n' + 1) .. n : a'[i - (n - n')] == a[i]
     */
    @Override
    public void dropWhile(Predicate<Object> predicate) {
        ifWhileFunction((obj, flag) -> flag || !predicate.test(obj), false);
    }

    @FunctionalInterface
    private interface Condition {
        boolean get(Object element, boolean flag);
    }

    private void ifWhileFunction(Condition condition, boolean flag) {
        int tempSize = size;
        for (int i = 0; i < tempSize; ++i) {
            Object curElement = element();
            flag = condition.get(curElement, flag);
            if (flag) {
                enqueue(curElement);
            }
            dequeue();
        }
    }

    protected abstract void enqueueImpl(final Object element);
    protected abstract Object dequeueImpl();
    protected abstract Object elementImpl();
    protected abstract void clearImpl();
}
