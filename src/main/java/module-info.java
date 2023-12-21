module com.example.finalecommerce {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.finaltodo to javafx.fxml;
    exports com.example.finaltodo;
    opens com.example.finaltodo.Login to javafx.fxml;
    exports com.example.finaltodo.Login;
    opens com.example.finaltodo.Home to javafx.fxml;
    exports com.example.finaltodo.Home;
    opens com.example.finaltodo.SignUp to javafx.fxml;
    exports com.example.finaltodo.SignUp;
    opens com.example.finaltodo.Database to javafx.fxml;
    exports com.example.finaltodo.Database;
    opens com.example.finaltodo.ToDo to javafx.fxml;
    exports com.example.finaltodo.ToDo;
}