package search;

public class BinarySearchUni {
    /**Pred: существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                               a[i] < a[i + 1] for i in [j, n-1)
     * Post: min(A)
     *       A = (j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                         a[i] < a[i + 1] for i in [j, n-1))
     */
    static int BinarySearch1 (int[] a) {
        /* существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         */
        int left = -1;
        /* a[-1] = +INF
         * существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         */
        int right = a.length - 1;
        /* a[n] = +INF */

        /* a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right */
        while (right - left > 1) {
            /* a[left] > a[left + 1], a[right - 1] < a[right], left < right
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
            if (a[mid] - a[mid + 1] >= 0) {
                /* a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
                 * left < mid < right, a[mid] - a[mid + 1] >= 0
                 */
                left = mid;
                /* a[left'] - a[left' + 1] >= 0, a[right] - a[right + 1] < 0, left' < right */
            } else {
                /* a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
                 * left < mid < right, a[mid] - a[mid + 1] < 0
                 */
                right = mid;
                /* a[left] - a[left + 1] >= 0, a[right'] - a[right' + 1] < 0, left < right' */
            }
            /* a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
             * (left < (left' or right') < right) => (right - left) строго убывает => цикл закончится
             */
        }
        /* (left < right, right - left <= 1) => left + 1 = right
         * a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0
         * a[left] >= a[left + 1], a[right] < a[right + 1]
         * a[right - 1] >= a[right], a[right] < a[right + 1]:
         *     a[right - 2] > a[right - 1], a[right] < a[right + 1]
         *     a[right - 1] >= a[right], a[right + 1] < a[right + 2]
         *     => right = min(A)
         */

        /* существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         * => a[j - 2] > a[j - 1], a[j] < a[j + 1]
         * => не существует больше двух j
         * size(A) = 1 или 2
         * Если решения 2:
         *     Пусть R = min(A)
         *         a[R - 2] > a[R - 1], a[R] < a[R + 1],
         *         a[R - 1] > a[R], a[R + 1] < a[R + 2]
         */
        return right;
    }

    /**Pred: существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                               a[i] < a[i + 1] for i in [j, n-1)
     * Post: min(A)
     *       A = (j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                         a[i] < a[i + 1] for i in [j, n-1))
     */
    static int BinarySearch2 (int[] a) {
        /* существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         */
        int left = -1;
        /* a[-1] = +INF
         * существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         */
        int right = a.length - 1;
        /* a[n] = +INF */

        /* существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         * a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
         */
        return BinarySearch2(a, left, right);
    }

    /**Pred: существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                               a[i] < a[i + 1] for i in [j, n-1)
     *       a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
     * Post: min(A)
     *       A = (j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                         a[i] < a[i + 1] for i in [j, n-1))
     */
    static int BinarySearch2 (int[] a, int left, int right) {
        /* Выход из рекурсии: */
        if (left + 1 == right) {
            /* (left < right, right - left <= 1) => left + 1 = right
             * a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0
             * Доказательство аналогично
             * => right = min(A)
             */
            return right;
        }
        /* left < (left + right) // 2 < right, доказательство:
         *     Аналогично
         */
        int mid = (left + right) / 2;
        /* left < mid < right */
        int nleft = left, nright = right;
        if (a[mid] - a[mid + 1] >= 0) {
            /* a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
             * left < mid < right, a[mid] - a[mid + 1] >= 0
             */
            nleft = mid;
            /* a[nleft] - a[nleft + 1] >= 0, a[nright] - a[nright + 1] < 0, nleft < nright */
        } else {
            /* a[left] - a[left + 1] >= 0, a[right] - a[right + 1] < 0, left < right
             * left < mid < right, a[mid] - a[mid + 1] < 0
             */
            nright = mid;
            /* a[nleft] - a[nleft + 1] >= 0, a[nright] - a[nright + 1] < 0, nleft < nright */
        }
        /* a[nleft] - a[nleft + 1] >= 0, a[nright] - a[nright + 1] < 0, nleft < nright
         * (left < nleft or nright < right) => (right - left) > (nright - nright)
         * => границы строго сужаются, рекурсия закончится
         * существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
         *                         a[i] < a[i + 1] for i in [j, n-1)
         */
        return BinarySearch2(a, nleft, nright);
    }

    /**Pred: существует j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                               a[i] < a[i + 1] for i in [j, n-1)
     * Post: min(A)
     *       A = (j in [0, n): a[i] > a[i + 1] for i in [0, j-1)
     *                         a[i] < a[i + 1] for i in [j, n-1))
     */
    public static void main(String[] args) {
        int size = args.length;
        int[] a = new int[size];
        for (int i = 0; i < size; ++i) {
            a[i] = Integer.parseInt(args[i]);
        }

        //System.out.println(BinarySearch1(a));
        System.out.println(BinarySearch2(a));
    }
}
