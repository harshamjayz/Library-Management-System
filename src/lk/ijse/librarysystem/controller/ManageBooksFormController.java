/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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
import lk.ijse.librarysystem.view.tblview.BookTV;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.util.HashNMap;

/**
 * FXML Controller class
 *
 * @author badhr
 */
public class ManageBooksFormController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXTextField txtBookID;
    @FXML
    private JFXTextField txtBookName;
    @FXML
    private JFXTextField txtISBN;
    @FXML
    private JFXTextField txtAuthor;
    @FXML
    private JFXTextField txtPublisher;
    @FXML
    private TableView<BookTV> tblBook;
    @FXML
    private JFXButton btnReport;
    @FXML
    private JFXButton btnReport1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblBook.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookid"));
        tblBook.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblBook.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblBook.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblBook.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("publisher"));
        tblBook.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("availability"));
        
        setTable();
    }    

    private void setTable(){
        ArrayList<BookTV> books = new ArrayList<>();
        String query = "select * from book";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                books.add(new BookTV(rs.getString("id"), rs.getString("name"), rs.getString("isbn"), rs.getString("author"), rs.getString("publisher"), rs.getBoolean("availability")));
                
            }
            tblBook.setItems(FXCollections.observableArrayList(books));
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    private int checkDB(){
        String query ="select count(id) from book where id = '"+txtBookID.getText()+"'";
        int count = -1;
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                count = rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
    private void UpdateBook(){
        String query = "update book set name = ? , isbn = ? , author = ? , publisher = ? where id = ?";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, txtBookName.getText());
            stmt.setObject(2, txtISBN.getText());
            stmt.setObject(3, txtAuthor.getText());
            stmt.setObject(4, txtPublisher.getText());
            stmt.setObject(5, txtBookID.getText());
            int res = stmt.executeUpdate();
            if(res>0){
                new Alert(Alert.AlertType.INFORMATION, "Data updated successfully").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Update failed").show();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void InsertBook(){
        String query = "insert into book values(?,?,?,true,?,?)";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(2, txtBookName.getText());
            stmt.setObject(3, txtISBN.getText());
            stmt.setObject(4, txtAuthor.getText());
            stmt.setObject(5, txtPublisher.getText());
            stmt.setObject(1, txtBookID.getText());
            int res = stmt.executeUpdate();
            if(res>0){
                new Alert(Alert.AlertType.INFORMATION, "Data insertion successfully").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Update failed").show();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void AddClicked(ActionEvent event) {
        int ava = checkDB();
        if(ava == 1){
            UpdateBook();
        }else{
            InsertBook();
        }
        setTable();
    }

    @FXML
    private void ClearClicked(ActionEvent event) {
        txtBookID.setText("");
        txtBookName.setText("");
        txtAuthor.setText("");
        txtPublisher.setText("");
        txtISBN.setText("");
    }

    @FXML
    private void tblClicked(MouseEvent event) {
        BookTV selectedItem = tblBook.getSelectionModel().getSelectedItem();
        txtBookID.setText(selectedItem.getBookid());
        txtBookName.setText(selectedItem.getName());
        txtAuthor.setText(selectedItem.getAuthor());
        txtPublisher.setText(selectedItem.getPublisher());
        txtISBN.setText(selectedItem.getIsbn());
    }

    @FXML
    private void HomeClicked(MouseEvent event) {
        MainFormController.navigateToHome(root, (Stage)root.getScene().getWindow());
    }

    @FXML
    private void ReportClicked(ActionEvent event) {
        InputStream strm = getClass().getResourceAsStream("/lk/ijse/librarysystem/reports/BookReport.jasper");
        HashMap map = new HashMap();
        try {
            JasperPrint fillReport = JasperFillManager.fillReport(strm, map, DBConnection.getDBConnection().getConnection());
          // JasperViewer.viewReport(fillReport,false);
            //JasperPrintManager.printReport(strm, true);
            JasperExportManager.exportReportToPdf(fillReport);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            Logger.getLogger(ManageBooksFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ReportClicked2(ActionEvent event) {
    }
    
}
