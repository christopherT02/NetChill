module com.example.netchill {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.datatransfer;
    requires mysql.connector.j;


    opens com.example.netchill to javafx.fxml;
    exports com.example.netchill;
    exports Model;
    opens Model to javafx.fxml;
}