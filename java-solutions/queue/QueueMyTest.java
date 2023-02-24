package queue;

import java.util.Objects;

class QueueMyTest {
    public static void main(String[] args) {
        Queue arrayQueue = new ArrayQueue();
        System.out.println("ArrayQueue tests:");
        test(arrayQueue);
        Queue linkedQueue = new LinkedQueue();
        System.out.println("LinkedQueue tests:");
        test(linkedQueue);
    }

    private static void test(Queue q) {
        /* e0 e1 e2 e3 e4
         * 4 enqueue_1_0
         * 3 enqueue_1_1
         * 2 enqueue_1_2
         * 1 enqueue_1_3
         * 0 enqueue_1_4
         */
        for (int i = 0; i < 5; ++i) {
            q.enqueue("enqueue_1_" + i);
        }
        System.out.println("Queue test N1: ");
        while (!q.isEmpty()) {
            Object value = q.dequeue();
            Objects.requireNonNull(value);
            System.out.println(q.size() + " " + value);
        }
        System.out.println();

        /* e0 e1 e2 e3 e4
         * 5 enqueue_2_0
         * 4 enqueue_2_1
         * 3 enqueue_2_2
         * 2 enqueue_2_3
         * 1 enqueue_2_4
         */
        for (int i = 0; i < 5; ++i) {
            q.enqueue("enqueue_2_" + i);
        }
        System.out.println("Queue test N2: ");
        while (!q.isEmpty()) {
            Object value = q.element();
            Objects.requireNonNull(value);
            System.out.println(q.size() + " " + value);
            q.dequeue();
        }
        System.out.println();

        /* e5 e6 e7 e8 e9
         * 5 enqueue_3_5
         * 4 enqueue_3_6
         * 3 enqueue_3_7
         * 2 enqueue_3_8
         * 1 enqueue_3_9
         */
        for (int i = 0; i < 5; ++i) {
            q.enqueue("push_3_" + i);
        }
        q.clear();
        for (int i = 5; i < 10; ++i) {
            q.enqueue("enqueue_3_" + i);
        }
        System.out.println("Clear test:");
        while (!q.isEmpty()) {
            Object value = q.element();
            Objects.requireNonNull(value);
            System.out.println(q.size() + " " + value);
            q.dequeue();
        }
        System.out.println();
    }
}