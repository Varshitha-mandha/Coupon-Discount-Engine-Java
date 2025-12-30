package Module_2;

public class PercentageStrategy implements DiscountStrategy {
    @Override
    public double apply(double total, double amount) {
        return total * (amount / 100);
    }
}
