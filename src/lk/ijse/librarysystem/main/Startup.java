/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author badhr
 */
public class Startup extends Application{

    @Override
    public void start(Stage primaryStage){
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/MainForm.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/Table.fxml"));
            Scene mainScene = new Scene(root);
            primaryStage.setTitle("Library Management System Main");
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
