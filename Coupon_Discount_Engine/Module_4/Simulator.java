package Module_4;

import Module_1.Coupon;
import Module_3.CouponFileHandler;
import Module_3.InvalidCouponException;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple multithreaded simulator for coupon applications.
 * - Creates several customer threads that attempt to apply coupons concurrently.
 * - Prints results to console and also uses CouponFileHandler.log(...) for persistence.
 *
 * Usage:
 *   Run this class (Simulator.main) from your IDE or from the command line.
 *   It will use the existing CouponService (which is synchronized) to demonstrate safe usage.
 */
public class Simulator {

    /**
     * Run a simulation using the provided CouponService instance.
     * @param service the shared CouponService
     * @throws InterruptedException if interrupted while waiting for threads to finish
     */
    public static void runSimulation(CouponService service) throws InterruptedException {
        // Choose a set of customer tasks. We'll intentionally have collisions on codes to show sync.
        List<Runnable> tasks = new ArrayList<>();

        // Using codes defined in Module_1.Coupon.DEFAULT_CODES
        String[] codes = Coupon.DEFAULT_CODES; // e.g. {"WELCOME10","SAVE50","FREESHIP"}

        // Create tasks: several customers try to apply the same coupon(s)
        tasks.add(makeTask(service, codes[0], 200.0, "cust-A")); // many will try WELCOME10
        tasks.add(makeTask(service, codes[0], 150.0, "cust-B"));
        tasks.add(makeTask(service, codes[0], 120.0, "cust-C"));
        tasks.add(makeTask(service, codes[1], 300.0, "cust-D")); // SAVE50
        tasks.add(makeTask(service, codes[1], 80.0, "cust-E"));
        tasks.add(makeTask(service, codes[2], 40.0, "cust-F")); // FREESHIP (maybe expired in seed)
        tasks.add(makeTask(service, "NONEXISTENT", 100.0, "cust-G"));

        // Create and start threads
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Thread t = new Thread(tasks.get(i), "sim-thread-" + (i + 1));
            threads.add(t);
            t.start();

            // Slight stagger so some threads start earlier, but still overlapping
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                // ignore small interruption in spawn
            }
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Simulation finished. Check coupon.log for persisted entries.");
    }

    private static Runnable makeTask(CouponService service, String code, double total, String customerId) {
        return () -> {
            String threadName = Thread.currentThread().getName();
            try {
                double discount = service.applyCoupon(code, total);
                String out = String.format("[%s] Customer=%s applied=%s orderTotal=%.2f discount=%.2f",
                        threadName, customerId, code, total, discount);
                System.out.println(out);
                CouponFileHandler.log(out);
            } catch (InvalidCouponException ice) {
                String out = String.format("[%s] Customer=%s failed to apply=%s -> %s",
                        threadName, customerId, code, ice.getMessage());
                System.out.println(out);
                CouponFileHandler.log(out);
            } catch (Exception ex) {
                String out = String.format("[%s] Customer=%s encountered error applying=%s -> %s",
                        threadName, customerId, code, ex.toString());
                System.out.println(out);
                CouponFileHandler.log(out);
            }
        };
    }

    /**
     * Shortcut main to run the simulator standalone.
     */
    public static void main(String[] args) throws InterruptedException {
        CouponService service = new CouponService();
        System.out.println("Starting simple multithreaded simulation...");
        runSimulation(service);
    }
}
