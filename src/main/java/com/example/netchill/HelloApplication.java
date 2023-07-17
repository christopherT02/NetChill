package com.example.netchill;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //delete from the db all ticket from ID = 0
        String query = "DELETE FROM `ticket` WHERE `ID_customer` = 0";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            int rs = stat.executeUpdate(query);

            if(rs>0)
                System.out.println("Line successfully deleted");
            else
                System.out.println("No line deleted");

            con.close();
        } catch (Exception ee) {
            System.out.println("non image " +ee);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("NetChill.fr");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}