package queue;

import java.util.function.Predicate;

/**
 * Model: a[1] .. a[n]
 * Invariant: n >= 0 && for i = 1 .. n: a[i] != null
 *
 * Let immutable(n): for i = 1 .. n: a'[i] = a[i]
 *     immutable(pos1, pos2, n): for i = 1 .. n: a'[pos1 + i] = a[pos2 + i]
 */
public interface Queue {
    /**
     * Pred: true
     * Post: n' = n && immutable(n) && R = n
     */
    int size();

    /**
     * Pred: true
     * Post: R = (n == 0) && n' = n && immutable(n)
     */
    boolean isEmpty();

    /**
     * Pred: n >= 1
     * Post: n' = n - 1 && immutable(0, 1, n') && R = a[1]
     */
    Object dequeue();

    /**
     * Pred: element != null
     * Post: n' = n + 1 && a[n'] = element && immutable(n)
     */
    void enqueue(Object element);

    /**
     * Pred: n >= 1
     * Post: n' = n && immutable(n) && R = a[1]
     */
    Object element();

    /**
     * Pred: true
     * Post: n' = 0
     */
    void clear();

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n если predicate.test(a[i]) == false, тогда a[i] должен принадлежать a' пусть это a'[i'].
     *       Получаем отображение f(i) = i' и f^-1(i') = i.
     *       f^-1(0) < f^-1(1)
     *       Тогда for i = 2 .. n' - 1 : f^-1(i' - 1) < f^-1(i') < f^-1(i' + 1)
     *       f^-1(n' - 1) < f^-1(n')
     *       a' in a
     */
    void removeIf(Predicate<Object> predicate);

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n если predicate.test(a[i]) == true, тогда a[i] должен принадлежать a' пусть это a'[i'].
     *       Получаем отображение f(i) = i' и f^-1(i') = i.
     *       f^-1(0) < f^-1(1)
     *       Тогда for i = 2 .. n' - 1 : f^-1(i' - 1) < f^-1(i') < f^-1(i' + 1)
     *       f^-1(n' - 1) < f^-1(n')
     *       a' in a
     */
    void retainIf(Predicate<Object> predicate);

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n' predicate.test(a[i]) == true и a'[i] == a[i]
     *       n' == n или predicate.test(a[n' + 1]) == false
     */
    void takeWhile(Predicate<Object> predicate);

    /**
     * Pred: predicate != null
     * Post: for i = 1 .. n - n' predicate.test(a[i]) == true
     *       n' == n или predicate.test(a[n' + 1]) == false
     *       for i = (n - n' + 1) .. n : a'[i - (n - n')] == a[i]
     */
    void dropWhile(Predicate<Object> predicate);
}
