package Module_3;

import java.io.*;
import java.util.List;

public class Serializer {
    private static final String FILE = "applied_coupons.ser";

    public static void save(List<?> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
