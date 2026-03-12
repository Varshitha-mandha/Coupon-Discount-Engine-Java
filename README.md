Coupon & Discount Engine

A Java-based Coupon and Discount Engine that applies promotional discounts dynamically using coupon codes.
The system calculates the final payable amount after applying valid coupons and tracks savings.

Features

Apply coupon codes to calculate discounts

Supports different discount strategies (percentage, fixed amount, free shipping)

Displays final price after discount

Search and view available coupons

Tracks total savings

Export coupon usage history

Simple GUI interface for applying coupons

Available Coupons

WELCOME10 – 10% discount for new users

SAVE50 – Flat ₹50 discount

FREESHIP – Free shipping on eligible orders

Project Structure
Coupon_Discount_Engine
│
├── .vscode
│
├── lib
│
├── Module_1
│   ├── Coupon.java
│   └── Coupon.class
│
├── Module_2
│   ├── FixedAmountStrategy.java
│   ├── FixedAmountStrategy.class
│   ├── PercentageStrategy.java
│   ├── PercentageStrategy.class
│   ├── StrategyFactory.java
│   └── StrategyFactory.class
│
├── Module_3
│   ├── CouponFileHandler.java
│   ├── CouponFileHandler.class
│   ├── InvalidCouponException.java
│   ├── InvalidCouponException.class
│   ├── Serializer.java
│   └── Serializer.class
│
├── Module_4
│   ├── CouponService.java
│   ├── CouponService.class
│   ├── Simulator.java
│   └── Simulator.class
│
├── Module_5
│   ├── MainApp.java
│   └── MainApp.class
│
├── style.css
├── applied_coupons.ser
├── coupon.log
├── MyApp.jar
├── run.bat
└── run_javafx.bat


How It Works

Enter the total order amount.

Enter a coupon code.

Click Apply Coupon.

The engine calculates the discount and final amount.

The system displays total savings and logs coupon usage.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/6abe006a-424f-49aa-bd19-ebacaf192357" />
