package Module_2;

import java.lang.reflect.Method;

public class StrategyFactory {
    public static DiscountStrategy get(String type) {
        return switch (type.toUpperCase()) {
            case "PERCENTAGE" -> new PercentageStrategy();
            default -> new FixedAmountStrategy();
        };
    }

    // Reflection to print available strategy methods
    public static void printStrategyMethods() {
        System.out.println("=== Strategy Methods via Reflection ===");
        for (Class<?> clazz : new Class[]{PercentageStrategy.class, FixedAmountStrategy.class}) {
            System.out.println("\nClass: " + clazz.getSimpleName());
            for (Method m : clazz.getDeclaredMethods()) {
                System.out.println("  " + m.getName());
            }
        }
    }
}
