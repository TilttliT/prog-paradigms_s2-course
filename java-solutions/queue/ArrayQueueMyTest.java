package queue;

import java.util.Objects;

class ArrayQueueMyTest {
    public static void main(String[] args) {
        /** p4 p3 p2 p1 p0 e0 e1 e2 e3 e4
         * 9 push_1_4
         * 8 push_1_3
         * 7 push_1_2
         * 6 push_1_1
         * 5 push_1_0
         * 4 enqueue_1_0
         * 3 enqueue_1_1
         * 2 enqueue_1_4
         * 1 enqueue_1_3
         * 0 enqueue_1_2
         */
        ArrayQueue q1 = new ArrayQueue();
        for (int i = 0; i < 5; ++i) {
            q1.enqueue("enqueue_1_" + i);
        }
        for (int i = 0; i < 5; ++i) {
            q1.push("push_1_" + i);
        }
        System.out.println("Dequeue test N1: ");
        for (int i = 0; i < 7; ++i) {
            Object value = q1.dequeue();
            Objects.requireNonNull(value);
            System.out.println(q1.size() + " " + value);
        }
        while (!q1.isEmpty()) {
            Object value = q1.remove();
            Objects.requireNonNull(value);
            System.out.println(q1.size() + " " + value);
        }
        System.out.println();


        /** p4 p3 p2 p1 p0 e0 e1 e2 e3 e4
         * 10 enqueue_2_4
         * 9 enqueue_2_3
         * 8 enqueue_2_2
         * 7 push_2_4
         * 6 push_2_3
         * 5 push_2_2
         * 4 push_2_1
         * 3 push_2_0
         * 2 enqueue_2_0
         * 1 enqueue_2_1
         */
        ArrayQueue q2 = new ArrayQueue();
        for (int i = 0; i < 5; ++i) {
            q2.push("push_2_" + i);
        }
        for (int i = 0; i < 5; ++i) {
            q2.enqueue("enqueue_2_" + i);
        }
        System.out.println("Dequeue test N2: ");
        for (int i = 0; i < 3; ++i) {
            Object value = q2.peek();
            Objects.requireNonNull(value);
            System.out.println(q2.size() + " " + value);
            q2.remove();
        }
        while (!q2.isEmpty()) {
            Object value = q2.element();
            Objects.requireNonNull(value);
            System.out.println(q2.size() + " " + value);
            q2.dequeue();
        }
        System.out.println();

        /** e0 e1 e2 e3 e4
         * 5 enqueue_3_0
         * 4 enqueue_3_1
         * 3 enqueue_3_2
         * 2 enqueue_3_3
         * 1 enqueue_3_4
         */
        ArrayQueue q3 = new ArrayQueue();
        for (int i = 0; i < 5; ++i) {
            q3.push("push_3_" + i);
        }
        q3.clear();
        for (int i = 0; i < 5; ++i) {
            q3.enqueue("enqueue_3_" + i);
        }
        System.out.println("Clear test:");
        while (!q3.isEmpty()) {
            Object value = q3.element();
            Objects.requireNonNull(value);
            System.out.println(q3.size() + " " + value);
            q3.dequeue();
        }
        System.out.println();

        /** 1 2 3 4 1 2 3 5
         * -1
         *  0
         *  1
         *  2
         *  3
         *  7
         *  -1
         *  -1
         *  4
         *  5
         *  6
         *  3
         *  7
         *  -1
         */
        ArrayQueue q4 = new ArrayQueue();
        for (int i = 1; i <= 4; ++i) {
            q4.enqueue(i);
        }
        for (int i = 1; i <= 3; ++i) {
            q4.enqueue(i);
        }
        q4.enqueue(5);
        System.out.println("IndexOf test:");
        for (int i = 0; i <= 6; ++i) {
            System.out.println(q4.indexOf(i));
        }
        for (int i = 0; i <= 6; ++i) {
            System.out.println(q4.lastIndexOf(i));
        }
        System.out.println();
    }
}