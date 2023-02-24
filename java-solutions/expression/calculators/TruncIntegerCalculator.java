package expression.calculators;

public class TruncIntegerCalculator extends IntegerCalculator implements Calculator<Integer> {
    public TruncIntegerCalculator() {
        super(false);
    }

    @Override
    protected Integer modifyValue(Integer a) {
        return a / 10 * 10;
    }
}