module com.example.netchill {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.datatransfer;


    opens com.example.netchill to javafx.fxml;
    exports com.example.netchill;
}