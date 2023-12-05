module com.example.ep1cliente {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ep1cliente to javafx.fxml;
    exports com.example.ep1cliente;
}