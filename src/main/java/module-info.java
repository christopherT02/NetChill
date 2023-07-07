module com.example.netchill {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.netchill to javafx.fxml;
    exports com.example.netchill;
}