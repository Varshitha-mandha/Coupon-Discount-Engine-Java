package Module_4;

import Module_1.Coupon;
import Module_2.*;
import Module_3.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CouponService {
    private final List<Coupon> coupons = new ArrayList<>();
    private final Object lock = new Object();

    public CouponService() {
        coupons.add(new Coupon("WELCOME10", "PERCENTAGE", 10, LocalDate.now().plusDays(30)));
        coupons.add(new Coupon("SAVE50", "FIXED", 50, LocalDate.now().plusDays(10)));
        coupons.add(new Coupon("FREESHIP", "FIXED", 20, LocalDate.now().minusDays(1)));
        StrategyFactory.printStrategyMethods(); // Reflection demo
    }

    public List<Coupon> listAll() { return coupons; }

    public List<String> listCouponCodes() {
        return coupons.stream().map(Coupon::getCode).collect(Collectors.toList());
    }

    public double applyCoupon(String code, double total) {
        synchronized (lock) {
            Coupon c = coupons.stream()
                    .filter(x -> x.getCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCouponException("Coupon not found"));

            if (c.isExpired()) throw new InvalidCouponException("Coupon expired");
            if (c.isUsed()) throw new InvalidCouponException("Coupon already used");

            DiscountStrategy strat = StrategyFactory.get(c.getType());
            double discount = strat.apply(total, c.getAmount());
            c.setUsed(true);

            CouponFileHandler.log("Applied " + code + " | Discount: " + discount);
            Serializer.save(coupons);
            return discount;
        }
    }
}
