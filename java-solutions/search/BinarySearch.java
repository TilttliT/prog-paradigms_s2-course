package search;

public class BinarySearch {
    /**Pred: a[i] >= a[i + 1] for i in [0, n-1)
     * Post: A - пусто: n
     *       Иначе: min(A)
     *       A = (i in [0, n) : a[i] <= x)
     */
    static int BinarySearch1(int[] a, int x) {
        /* a[i] >= a[i + 1] for i in [0, n-1) */
        int left = -1;
        /* a[-1] = +INF
         * a[i] >= a[i + 1] for i in [0, n-1)
         */
        int right = a.length;
        /* a[n] = -INF 
         * a[i] >= a[i + 1] for i in [0, n-1)
         */

        /* a[left] > x >= a[right], left < right */
        while (right - left > 1) {
            /* a[left] > x >= a[right], left < right
             * right - left > 1
             * left < (left + right) // 2 < right, доказательство:
             *     right - left > 1
             *     left < right - 1 => left < right
             *     (left + right)%2 = 0 или 1
             *     left < right - (left + right)%2
             *     left * 2 < (left + right) - (left + right)%2
             *     left < ((left + right) - (left + right)%2) / 2
             *     left < (left + right) // 2
             *     left < mid // 2
             *
             *     right - left > 1
             *     right > left + 1 => right > left
             *     (left + right)%2 = 0 или 1
             *     right > left + (left + right)%2
             *     right * 2 > (left + right) + (left + right)%2
             *     right > ((left + right) - (left + right)%2) / 2
             *     right > (left + right) // 2
             *     right > mid // 2
             */
            int mid = (left + right) / 2;
            /* left < mid < right */
            if (a[mid] <= x) {
                /* left < mid < right && a[mid] <= x */
                right = mid;
                /* left < right' && a[left] > x >= a[right'] */
            } else {
                /* left < mid < right && a[mid] > x */
                left = mid;
                /* left' < right && a[left'] > x >= a[right] */
            }
            /* a[left] > x >= a[right], left < right
             * (left < left' or right' < right) => (right - left) строго убывает => цикл закончится
             */
        }
        /* a[left] > x >= a[right], left < right
         * right - left <= 1
         * => left + 1 = right
         * => right - минимальное
         */
        return right;
    }

    /**Pred: a[i] >= a[i + 1] for i in [0, n-1)
     * Post: A - пусто: n
     *       Иначе: min(A)
     *       A = (i in [0, n) : a[i] <= x)
     */
    static int BinarySearch2(int[] a, int x) {
        /* a[i] >= a[i + 1] for i in [0, n-1) */
        int left = -1;
        /* a[-1] = +INF
         * a[i] >= a[i + 1] for i in [0, n-1)
         */
        int right = a.length;
        /* a[n] = -INF
         * a[i] >= a[i + 1] for i in [0, n-1)
         */

        /* для любого j a[j] >= a[j + 1], a[left] > x >= a[right], left < right */
        return BinarySearch2(a, x, left, right);
    }

    /**Pred: a[i] >= a[i + 1] for i in [0, n-1)
     *       a[left] > x >= a[right], left < right
     * Post: A - пусто: n
     *       Иначе: min(A)
     *       A = (i in [0, n) : a[i] <= x)
     */
    static int BinarySearch2(int[] a, int x, int left, int right) {
        /* a[i] >= a[i + 1] for i in [0, n-1)
         * a[left] > x >= a[right], left < right
         */

        /* Выход из рекурсии: */
        if (left + 1 == right) {
            /* a[left] > x >= a[right], left + 1 = right
             * => right - минимальное
             */
            return right;
        }
        /* (left < right && left + 1 != right) => (right - left > 1)
         * left < (left + right) // 2 < right:
         *     Докозательство аналогично
         */
        int mid = (left + right) / 2;
        /* left < mid < right */
        int nleft = left, nright = right;
        if (a[mid] <= x) {
            /* left < mid < right && a[mid] <= x
             * a[left] > x >= a[mid], left < mid
             */
            nright = mid;
            /* a[nleft] > x >= a[nright], nleft < nright */
        } else {
            /* left < mid < right && a[mid] > x
             * a[mid] > x >= a[right], mid < right
             */
            nleft = mid;
            /* a[nleft] > x >= a[nright], nleft < nright */
        }
        /* left < mid < right
         * => left < nleft or nright < right
         * => (nright - nleft) < (right - left) => границы сужаются, рекурсия закончится
         * a[i] >= a[i + 1] for i in [0, n-1)
         * a[nleft] > x >= a[nright], nleft < nright
         */
        return BinarySearch2(a, x, nleft, nright);
    }

    /**Pred: a[i] >= a[i + 1] for i in [0, n-1)
     * Post: A - пусто: n
     *       Иначе: min(A)
     *       A = (i in [0, n) : a[i] <= x)
     */
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int size = args.length - 1;
        int[] a = new int[size];
        for (int i = 0; i < size; ++i) {
            a[i] = Integer.parseInt(args[i + 1]);
        }

        System.out.println(BinarySearch1(a, x));
        //System.out.println(BinarySearch2(a, x));
    }
}
