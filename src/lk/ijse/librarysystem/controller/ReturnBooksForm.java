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
public class ReturnBooksForm implements Initializable {

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
    private JFXTextField txtReturnID;
    @FXML
    private JFXComboBox<String> cmbBorrows;

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
    
    private boolean isReturned(){
        String query = "select availability from book where id ='"+cmbBookID.getSelectionModel().getSelectedItem()+"'";
        boolean available = false;
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                available = rs.getBoolean("availability");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReturnBooksForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return available;
    }
    @FXML
    private void HomeClicked(MouseEvent event) {
        MainFormController.navigateToHome(root, (Stage)root.getScene().getWindow());
    }
    @FXML
    private void AddClicked(ActionEvent event) {
        boolean isReturned = isReturned();
        if(!isReturned){
            InsertReturn();
        } else{
            new Alert(Alert.AlertType.ERROR, "Book already returned").show();
        }
    }
    private void InsertReturn(){
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String query = "insert into returnb values(?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = DBConnection.getDBConnection().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, txtReturnID.getText());
            stmt.setObject(2, cmbMemID.getSelectionModel().getSelectedItem());
            stmt.setObject(3, cmbBookID.getSelectionModel().getSelectedItem());
            stmt.setObject(4, df.format(date));
            stmt.setObject(5, cmbBorrows.getSelectionModel().getSelectedItem());
            int res = stmt.executeUpdate();
            if(res>0){
                UpdateBook();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ReturnBooksForm.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    private void UpdateBook(){
        String query = "update book set availability = true where id = '"+cmbBookID.getSelectionModel().getSelectedItem()+"'";
        Connection conn = null;
        try {
            conn = DBConnection.getDBConnection().getConnection();
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
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ReturnBooksForm.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
    }

    @FXML
    private void ClearClicked(ActionEvent event) {
        txtBookName.setText("");
        txtMemName.setText("");
        txtReturnID.setText("");
    }

    @FXML
    private void TblClicked(MouseEvent event) {
        BorrowedTV borrowedTV = tblBorrowed.getSelectionModel().getSelectedItem();
        cmbBorrows.getSelectionModel().select(borrowedTV.getBorrowid());
    }
    private void setCmbs() {
        ArrayList<String> members = new ArrayList<>();
        ArrayList<String> books = new ArrayList<>();
        ArrayList<String> borrowids = new ArrayList<>();
        String query = "select id from member";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                members.add(rs.getString("id"));
            }
            query = "select id from book where availability = false";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                books.add(rs.getString("id"));
            }
            query = "select borrowid from borrow where borrowid not in(select borrowid from returnb)";
            rs = stmt.executeQuery(query);
            while(rs.next()){
                borrowids.add(rs.getString("borrowid"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BorrowBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cmbMemID.setItems(FXCollections.observableArrayList(members));
        cmbBookID.setItems(FXCollections.observableArrayList(books));
        cmbBorrows.setItems(FXCollections.observableArrayList(borrowids));
    }

    @FXML
    private void BorrowIDSellected(ActionEvent event) {
        String query = "select bookid,mid from borrow where borrowid = '"+cmbBorrows.getSelectionModel().getSelectedItem()+"'";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                cmbBookID.getSelectionModel().select(rs.getString("bookid"));
                cmbMemID.getSelectionModel().select(rs.getString("mid"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReturnBooksForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
