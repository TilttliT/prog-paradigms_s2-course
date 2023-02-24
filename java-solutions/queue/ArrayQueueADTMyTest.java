package queue;

import java.util.Objects;

import static queue.ArrayQueueADT.*;

class ArrayQueueADTMyTest {
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
        ArrayQueueADT q1 = create();
        for (int i = 0; i < 5; ++i) {
            enqueue(q1, "enqueue_1_" + i);
        }
        for (int i = 0; i < 5; ++i) {
            push(q1, "push_1_" + i);
        }
        System.out.println("Dequeue test N1: ");
        for (int i = 0; i < 7; ++i) {
            Object value = dequeue(q1);
            Objects.requireNonNull(value);
            System.out.println(size(q1) + " " + value);
        }
        while (!isEmpty(q1)) {
            Object value = remove(q1);
            Objects.requireNonNull(value);
            System.out.println(size(q1) + " " + value);
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
        ArrayQueueADT q2 = create();
        for (int i = 0; i < 5; ++i) {
            push(q2, "push_2_" + i);
        }
        for (int i = 0; i < 5; ++i) {
            enqueue(q2, "enqueue_2_" + i);
        }
        System.out.println("Dequeue test N2: ");
        for (int i = 0; i < 3; ++i) {
            Object value = peek(q2);
            Objects.requireNonNull(value);
            System.out.println(size(q2) + " " + value);
            remove(q2);
        }
        while (!isEmpty(q2)) {
            Object value = element(q2);
            Objects.requireNonNull(value);
            System.out.println(size(q2) + " " + value);
            dequeue(q2);
        }
        System.out.println();

        /** e0 e1 e2 e3 e4
         * 5 enqueue_3_0
         * 4 enqueue_3_1
         * 3 enqueue_3_2
         * 2 enqueue_3_3
         * 1 enqueue_3_4
         */
        ArrayQueueADT q3 = create();
        for (int i = 0; i < 5; ++i) {
            push(q3, "push_3_" + i);
        }
        clear(q3);
        for (int i = 0; i < 5; ++i) {
            enqueue(q3, "enqueue_3_" + i);
        }
        System.out.println("Clear test:");
        while (!isEmpty(q3)) {
            Object value = element(q3);
            Objects.requireNonNull(value);
            System.out.println(size(q3) + " " + value);
            dequeue(q3);
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
        ArrayQueueADT q4 = create();
        for (int i = 1; i <= 4; ++i) {
            enqueue(q4, i);
        }
        for (int i = 1; i <= 3; ++i) {
            enqueue(q4, i);
        }
        enqueue(q4, 5);
        System.out.println("IndexOf test:");
        for (int i = 0; i <= 6; ++i) {
            System.out.println(indexOf(q4, i));
        }
        for (int i = 0; i <= 6; ++i) {
            System.out.println(lastIndexOf(q4, i));
        }
        System.out.println();
    }
}