package Module_1;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Coupon implements Serializable {
    private static final AtomicInteger counter = new AtomicInteger(0);
    public static final String[] DEFAULT_CODES = {"WELCOME10", "SAVE50", "FREESHIP"};

    private int id;
    private String code;
    private String type;
    private double amount;
    private LocalDate expiry;
    private boolean used;

    public Coupon(String code, String type, double amount, LocalDate expiry) {
        this.id = counter.incrementAndGet();
        this.code = code;
        this.type = type;
        this.amount = amount;
        this.expiry = expiry;
        this.used = false;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDate getExpiry() { return expiry; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public boolean isExpired() {
        return expiry.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return code + " (" + type + ", " + amount + ", Exp: " + expiry + ")";
    }
}
