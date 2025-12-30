package Module_5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import Module_4.CouponService;
import Module_3.InvalidCouponException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp extends Application {
    private final CouponService service = new CouponService();
    private final List<String> history = new ArrayList<>();
    private double totalSavings = 0;

    private FlowPane couponButtons;
    private TextField searchField;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        // === Title ===
        Label title = new Label("💸 Coupon & Discount Engine");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill: linear-gradient(to right, #6a1b9a, #283593);");

        // === Input Fields ===
        TextField totalField = new TextField("200");
        totalField.setStyle("-fx-border-color:#7e57c2; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 10;");

        TextField codeField = new TextField();
        codeField.setStyle("-fx-border-color:#7e57c2; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 10;");

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size:14px; -fx-text-fill:#283593;");

        Label savingsLabel = new Label("Total Savings: ₹0");
        savingsLabel.setStyle("-fx-font-weight:bold; -fx-text-fill:#283593;");

        // === Apply Button ===
        Button applyBtn = new Button("Apply Coupon");
        applyBtn.setStyle(buttonStyle());
        applyBtn.setOnMouseEntered(e -> applyBtn.setStyle(buttonHoverStyle()));
        applyBtn.setOnMouseExited(e -> applyBtn.setStyle(buttonStyle()));

        applyBtn.setOnAction(event -> {
            try {
                double total = Double.parseDouble(totalField.getText());
                double discount = service.applyCoupon(codeField.getText(), total);
                double finalAmount = total - discount;
                totalSavings += discount;
                resultLabel.setText("✅ Discount: ₹" + discount + " | Final: ₹" + finalAmount);
                savingsLabel.setText("Total Savings: ₹" + totalSavings);
                history.add(codeField.getText() + " → saved ₹" + discount);
            } catch (InvalidCouponException ex) {
                resultLabel.setText("❌ " + ex.getMessage());
            } catch (Exception ex) {
                resultLabel.setText("❌ Invalid input!");
            }
        });

        // === Search Bar ===
        searchField = new TextField();
        searchField.setPromptText("🔎 Search coupon...");
        searchField.setStyle("-fx-border-color:#7e57c2; -fx-border-radius:6; -fx-background-radius:6; -fx-padding:6 10;");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshCouponButtons(newVal.trim(), codeField));

        // === Coupon Buttons ===
        Label availableLabel = new Label("Available Coupons:");
        availableLabel.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:#5e35b1;");

        couponButtons = new FlowPane(10, 10);
        couponButtons.setPadding(new Insets(10));
        couponButtons.setStyle("-fx-background-color:rgba(255,255,255,0.7); -fx-border-color:#b39ddb; -fx-border-radius:8; -fx-background-radius:8; -fx-padding:10;");
        refreshCouponButtons("", codeField);

        // === Feature Buttons ===
        

        Button exportBtn = new Button("💾 Export History");
        exportBtn.setStyle(featureButtonStyle());
        exportBtn.setOnMouseEntered(e -> exportBtn.setStyle(featureButtonHover()));
        exportBtn.setOnMouseExited(e -> exportBtn.setStyle(featureButtonStyle()));
        exportBtn.setOnAction(e -> exportHistory(stage));

        HBox featureRow = new HBox(10,  exportBtn);

        // === Layout ===
        VBox layout = new VBox(15,
                title,
                new Label("Total Amount:"), totalField,
                new Label("Coupon Code:"), codeField,
                applyBtn,
                new Separator(),
                searchField,
                availableLabel,
                couponButtons,
                new Separator(),
                savingsLabel,
                featureRow,
                resultLabel
        );
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #f3e5f5); -fx-font-family:'Poppins';");

        // === Scene ===
        scene = new Scene(layout, 520, 600);
        stage.setTitle("Coupon Engine");
        stage.setScene(scene);
        stage.show();
    }

    /** Refresh coupon buttons */
    private void refreshCouponButtons(String filter, TextField codeField) {
        couponButtons.getChildren().clear();
      List<String> allCoupons = service.listCouponCodes();


        List<String> filtered = allCoupons.stream()
                .filter(c -> c.toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

        for (int i = 0; i < filtered.size(); i++) {
            String code = filtered.get(i);
            Button btn = new Button(code);
            String style = switch (i % 4) {
                case 0 -> couponStyle("#8e24aa", "#6a1b9a");
                case 1 -> couponStyle("#1e88e5", "#1565c0");
                case 2 -> couponStyle("#ec407a", "#d81b60");
                default -> couponStyle("#26c6da", "#0097a7");
            };
            btn.setStyle(style);
            btn.setOnMouseEntered(e -> btn.setStyle(style + "-fx-opacity:0.85; -fx-translate-y:-2;"));
            btn.setOnMouseExited(e -> btn.setStyle(style));
            btn.setTooltip(new Tooltip("Click to apply " + code));
            btn.setOnAction(e -> codeField.setText(code));
            couponButtons.getChildren().add(btn);
        }

        if (filtered.isEmpty()) {
            couponButtons.getChildren().add(new Label("❌ No coupons found"));
        }
    }

    /** Generate random coupon */
    private void addRandomCoupon() {
        String randomCode = "NEW" + (100 + new Random().nextInt(900));
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "🎉 New Coupon Generated: " + randomCode);
        alert.show();
    }

    /** Export discount history */
    private void exportHistory(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Discount History");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        var file = chooser.showSaveDialog(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                for (String h : history) writer.write(h + "\n");
                new Alert(Alert.AlertType.INFORMATION, "✅ History saved to " + file.getName()).show();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error saving history!").show();
            }
        }
    }

    /** === CSS helper methods === */
    private String buttonStyle() {
        return "-fx-background-color: linear-gradient(to right, #7e57c2, #3949ab);"
             + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius:8; -fx-padding:8 16;";
    }

    private String buttonHoverStyle() {
        return "-fx-background-color: linear-gradient(to right, #5e35b1, #283593);"
             + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius:8; -fx-padding:8 16; -fx-opacity:0.9; -fx-translate-y:-2;";
    }

    private String featureButtonStyle() {
        return "-fx-background-color: linear-gradient(to right, #ba68c8, #9575cd);"
             + "-fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:8 14;";
    }

    private String featureButtonHover() {
        return "-fx-background-color: linear-gradient(to right, #8e24aa, #5e35b1);"
             + "-fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:8 14; -fx-opacity:0.9; -fx-translate-y:-2;";
    }

    private String couponStyle(String color1, String color2) {
        return "-fx-background-color: linear-gradient(to right, " + color1 + ", " + color2 + ");"
             + "-fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:8 14;";
    }

    public static void main(String[] args) {
        launch();
    }
}
