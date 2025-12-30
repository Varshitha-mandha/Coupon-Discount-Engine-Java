@echo off
java --enable-native-access=javafx.graphics --module-path lib --add-modules javafx.controls,javafx.fxml -jar MyApp.jar
pause
