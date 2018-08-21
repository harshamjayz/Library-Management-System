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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import lk.ijse.librarysystem.view.tblview.SearchTV;

/**
 * FXML Controller class
 *
 * @author badhr
 */
public class SearchBookFormController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private JFXComboBox<String> cmbSearchBy;
    @FXML
    private JFXButton btnSearch;
    @FXML
    private TableView<SearchTV> tblSearch;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCombo();
        tblSearch.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookid"));
        tblSearch.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("bookname"));
        tblSearch.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblSearch.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("publisher"));
        tblSearch.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("availability"));
        
    }    
    private void setCombo(){
        ArrayList<String> searchbys = new ArrayList<>();
        searchbys.add("Search By Book ID");
        searchbys.add("Search By Book Name");
        searchbys.add("Search By Book Author");
        searchbys.add("Search By Book Publisher");
        cmbSearchBy.setItems(FXCollections.observableArrayList(searchbys));
    }
    @FXML
    private void HomeClicked(MouseEvent event) {
        MainFormController.navigateToHome(root, (Stage)root.getScene().getWindow());
    }

    @FXML
    private void SearchBtnClicked(ActionEvent event) {
        ArrayList<SearchTV> searches = new ArrayList<>();
        String search = txtSearch.getText();
        if(cmbSearchBy.getSelectionModel().getSelectedIndex()>=0){
            String sby = cmbSearchBy.getSelectionModel().getSelectedItem();
            String query = null;
         
            switch(sby){
                case "Search By Book ID":
                    query = "select * from book where id = '"+search+"'";
                    break;
                case "Search By Book Name" :
                    query = "select * from book where name = '"+search+"'";
                    break;
                case "Search By Book Author" :
                    query = "select * from book where author = '"+search+"'";
                    break;
                case "Search By Book Publisher":
                    query = "select * from book where publisher = '"+search+"'";
                    break;
            }
            if(query != null){
                try {
                    Connection conn = DBConnection.getDBConnection().getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                         searches.add(new SearchTV(rs.getString("id"), rs.getString("name"), rs.getString("author"), rs.getString("publisher"), rs.getBoolean("availability")));
                    }
                    tblSearch.setItems(FXCollections.observableArrayList(searches));
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(SearchBookFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                new Alert(Alert.AlertType.ERROR, "Please select that from what you want to search!").show();
            }
        }
    }
    
}
