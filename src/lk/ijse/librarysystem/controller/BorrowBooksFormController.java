/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.librarysystem.db.DBConnection;
import lk.ijse.librarysystem.view.tblview.BorrowedTV;

/**
 * FXML Controller class
 *
 * @author badhr
 */
public class BorrowBooksFormController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXComboBox<String> cmbMemID;
    @FXML
    private JFXTextField txtMemName;
    @FXML
    private JFXComboBox<String> cmbBookID;
    @FXML
    private JFXTextField txtBookName;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnClear;
    @FXML
    private TableView<BorrowedTV> tblBorrowed;
    @FXML
    private JFXTextField txtBorrowID;
    @FXML
    private JFXButton btnReport;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblBorrowed.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookid"));
        tblBorrowed.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("borrowid"));
        tblBorrowed.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("borrowdate"));
        tblBorrowed.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("returndate"));
        tblBorrowed.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("dayspending"));
        setCmbs();
    }

    private void setCmbs() {
        ArrayList<String> members = new ArrayList<>();
        ArrayList<String> books = new ArrayList<>();
        String query = "select id from member";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                members.add(rs.getString("id"));
            }
            query = "select id from book where availability = true";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                books.add(rs.getString("id"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cmbMemID.setItems(FXCollections.observableArrayList(members));
        cmbBookID.setItems(FXCollections.observableArrayList(books));

    }

    private int getBorrowedBooks() {
        int count = -1;
        String query = "select count(borrowid) from borrow where mid = '" + cmbMemID.getSelectionModel().getSelectedItem() + "' and borrowid not in (select borrowid from returnb)";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    private boolean isAvailable() {
        String query = "select availability from book where id = '" + cmbBookID.getSelectionModel().getSelectedItem() + "'";
        boolean availability = false;
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                availability = rs.getBoolean("availability");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return availability;
    }

    private void InsertBorrow() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String query = "insert into borrow values(?,?,?,?)";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1,txtBorrowID.getText());
            stmt.setObject(2,cmbMemID.getSelectionModel().getSelectedItem());
            stmt.setObject(3, cmbBookID.getSelectionModel().getSelectedItem());
            stmt.setObject(4,df.format(date));
            int res = stmt.executeUpdate();
            if(res>0){
                UpdateBook();
            } else{
                new Alert(Alert.AlertType.ERROR, "Inserting failed").show();
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void UpdateBook(){
        String query = "update book set availability = false where id = '"+cmbBookID.getSelectionModel().getSelectedItem()+"'";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            int res = stmt.executeUpdate(query);
            if(res>0){
                new Alert(Alert.AlertType.INFORMATION, "Borrowing record successful").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Update book failed").show();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void AddClicked(ActionEvent event) {
        int count = getBorrowedBooks();
        boolean availability = isAvailable();
        if (count <= 5) {
            if (availability) {
                InsertBorrow();
            } else{
                new Alert(Alert.AlertType.ERROR, "This book is not available at this moment").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Sorry the user reached his maximum book allowance").show();
        }
        InsertBorrow();
        ClearClicked(null);
    }

    @FXML
    private void ClearClicked(ActionEvent event) {
        this.txtBorrowID.setText("");
        this.txtBookName.setText("");
        this.txtMemName.setText("");
        this.setCmbs();
    }

    @FXML
    private void MemberSelected(ActionEvent event) {
        ArrayList<BorrowedTV> borrows = new ArrayList<>();
        String query = "select  name from member where id = '" + cmbMemID.getSelectionModel().getSelectedItem() + "'";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                txtMemName.setText(rs.getString("name"));
            }
            query = "select * from borrow where borrowid not in (select borrowid from returnb) and mid = '" + cmbMemID.getSelectionModel().getSelectedItem() + "'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                borrows.add(new BorrowedTV(rs.getString("borrowid"), rs.getString("bookid"), rs.getDate("borrowdate")));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblBorrowed.setItems(FXCollections.observableArrayList(borrows));
    }

    @FXML
    private void BookSelected(ActionEvent event) {
        String query = "select name from book where id = '" + cmbBookID.getSelectionModel().getSelectedItem() + "'";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                txtBookName.setText(rs.getString("name"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void HomeClicked(MouseEvent event) {
        MainFormController.navigateToHome(root, (Stage)root.getScene().getWindow());
    }

    @FXML
    private void ReportClicked(ActionEvent event) {
        
    }

}
