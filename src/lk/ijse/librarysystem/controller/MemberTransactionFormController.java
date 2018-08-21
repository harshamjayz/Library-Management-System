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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.librarysystem.db.DBConnection;
import lk.ijse.librarysystem.view.tblview.BorrowedReturnTV;
import lk.ijse.librarysystem.view.tblview.BorrowedTV;

/**
 * FXML Controller class
 *
 * @author badhr
 */
public class MemberTransactionFormController implements Initializable {

    private String memberid;
    @FXML
    private AnchorPane root;
    @FXML
    private Label txtMemID;
    @FXML
    private TableView<BorrowedReturnTV> tblBandR;
    @FXML
    private TableView<BorrowedTV> tblBorrowed;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        memberid = ManageMemberFormController.getmemberid();
        txtMemID.setText(memberid);
        tblBandR.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookid"));
        tblBandR.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("borrowid"));
        tblBandR.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("returnid"));
        tblBandR.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("borrowdate"));
        tblBandR.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("returndate"));
        tblBorrowed.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookid"));
        tblBorrowed.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("borrowid"));
        tblBorrowed.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("borrowdate"));
        tblBorrowed.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("returndate"));
        tblBorrowed.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("dayspending"));
        getTables();
    }   
    private void getTables(){
        ArrayList<BorrowedReturnTV> borrowrets = new ArrayList<>();
        ArrayList<BorrowedTV> borrows = new ArrayList<>();
        String query = "select b.* , r.returndate,r.returnid from borrow b , returnb r where r.borrowid = b.borrowid and b.mid = '"+memberid+"'";
        String query2 = "select * from borrow where borrowid not in (select borrowid from returnb) and mid = '"+memberid+"'";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                borrowrets.add(new BorrowedReturnTV(rs.getString("bookid"), rs.getString("borrowid"), rs.getString("returnid"), rs.getDate("borrowdate"), rs.getDate("returndate")));             
            }
            rs = stmt.executeQuery(query2);
            while(rs.next()){
                borrows.add(new BorrowedTV(rs.getString("bookid"), rs.getString("borrowid"), rs.getDate("borrowdate")));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MemberTransactionFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblBandR.setItems(FXCollections.observableArrayList(borrowrets));
        tblBorrowed.setItems(FXCollections.observableArrayList(borrows));
    }
    private void HomeClicked(MouseEvent event) {
        MainFormController.navigateToHome(root, (Stage)root.getScene().getWindow());
    }

    /**
     * @param memberid the memberid to set
     */
    public void setMemberid(String memberid) {
        this.memberid = memberid;
        
    }

    /**
     * @return the memberid
     */
    public String getMemberid() {
        return memberid;
    }

    @FXML
    private void onBackClicked(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/librarysystem/view/ManageMember.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) this.root.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            TranslateTransition tt = new TranslateTransition(Duration.millis(2000), root);
            tt.setToX(0);
            tt.setFromX(-stage.getWidth());
            tt.play();
        } catch (IOException ex) {
            Logger.getLogger(MemberTransactionFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
