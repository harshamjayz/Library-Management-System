/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.librarysystem.db.DBConnection;
import lk.ijse.librarysystem.view.tblview.ToBeReturningTV;

/**
 *
 * @author badhr
 */
public class MainFormController implements Initializable{

    @FXML
    private Pane manageMemPane;
    @FXML
    private Pane manageBookPane;
    @FXML
    private Pane borrowingsPanel;
    @FXML
    private Pane returnPane;
    @FXML
    private Pane searchPane;
    @FXML
    private TableView<ToBeReturningTV> tblReturnings;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblReturnings.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookid"));
        tblReturnings.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("memberid"));
        tblReturnings.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("borroweddate"));
        tblReturnings.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("returndate"));
        tblReturnings.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("borrowid"));
        
        setTable();
    }

    @FXML
    private void PaneMouseEntered(MouseEvent event) {
        if(event.getSource() instanceof Pane){
            Pane pane = (Pane) event.getSource();
            ScaleTransition st = new ScaleTransition(Duration.millis(200), pane);
            st.setToY(1.2);
            st.setToX(1.2);
            st.play();
        }
    }

    @FXML
    private void PaneMouseExited(MouseEvent event) {
        if(event.getSource() instanceof Pane){
            Pane pane = (Pane) event.getSource();
            ScaleTransition st = new ScaleTransition(Duration.millis(200), pane);
            st.setToY(1.0);
            st.setToX(1.0);
            st.play();
        }
    }
    private void setTable(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -15);
        ArrayList<ToBeReturningTV> returns = new ArrayList<>();
        String query = "select * from borrow where borrowid not in(select borrowid from returnb) and borrowdate <= '"+df.format(c.getTime())+"'";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                returns.add(new ToBeReturningTV(rs.getString("borrowid"), rs.getString("bookid"), rs.getString("mid"), rs.getDate("borrowdate")));
            }
            tblReturnings.setItems(FXCollections.observableArrayList(returns));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void PaneClicked(MouseEvent event) {
        if(event.getSource() instanceof Pane){
            try {
                Parent root = null;
                Pane pane =(Pane) event.getSource();
                switch(pane.getId()){
                    case "searchPane" :
                        root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/SearchBookForm.fxml"));
                        break;
                    case "manageMemPane":
                        root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/ManageMember.fxml"));
                        break;
                    case "borrowingsPanel" :
                        root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/BorrowBooksForm.fxml"));
                        break;
                    case "returnPane" :
                        root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/ReturnBooksForm.fxml"));
                        break;
                    case "manageBookPane" :
                        root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/ManageBooksForm.fxml"));
                        break;
                }
                if(root != null){
                    Scene scene = new Scene(root);
                    Stage primaryStage =(Stage) this.root.getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("| Library Management System");
                    primaryStage.show();
                    TranslateTransition tt = new TranslateTransition(Duration.millis(2000), scene.getRoot());
                    tt.setToX(0);
                    tt.setFromX(scene.getWidth());
                    tt.play();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static void navigateToHome(Node node, Stage mainStage) {
            TranslateTransition tt = new TranslateTransition(Duration.millis(300), node);
            tt.setFromX(0);
            tt.setToX(-node.getScene().getWidth());
            tt.play();
            
            Platform.runLater(()->{
            
                try {
                    Parent root = FXMLLoader.load(MainFormController.class.getResource("/lk/ijse/librarysystem/view/MainForm.fxml"));
                    Scene mainScene = new Scene(root);
                    mainStage.setScene(mainScene);
                    
                    TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), root.lookup("AnchorPane"));
                    tt1.setToX(0);
                    tt1.setFromX(-mainScene.getWidth());
                    tt1.play();
                    
                    mainStage.centerOnScreen();
                } catch (IOException ex) {
                    Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            });
            
  }
}
