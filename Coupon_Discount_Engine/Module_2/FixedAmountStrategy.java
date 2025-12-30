package Module_2;

public class FixedAmountStrategy implements DiscountStrategy {
    @Override
    public double apply(double total, double amount) {
        return Math.min(amount, total);
    }
}
