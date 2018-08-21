/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lk.ijse.librarysystem.db.DBConnection;
import lk.ijse.librarysystem.view.tblview.MemberTV;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author badhr
 */
public class ManageMemberFormController implements Initializable {

    private static String callmem;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField txtMemberID;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtAdr;
    @FXML
    private JFXTextField txtNIC;
    @FXML
    private JFXTextField txtContact;
    @FXML
    private TableView<MemberTV> tblMember;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnReport;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("memberid"));
        tblMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("adr"));
        tblMember.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contact"));
        setTable();

    }

    @FXML
    private void SaveClicked(ActionEvent event) {
        String query = "select count(*) from member where id = '" + txtMemberID.getText() + "'";
        int count = -1;
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageMemberFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (count) {
            case 0:
                insertMember();
                break;
            case 1:
                updateMember();
                break;
        }
        setTable();
    }

    private void insertMember() {
        String query = "insert into member values(?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, txtMemberID.getText());
            stmt.setObject(2, txtNIC.getText());
            stmt.setObject(3, txtName.getText());
            stmt.setObject(4, txtAdr.getText());
            stmt.setObject(5, txtContact.getText());
            int res = stmt.executeUpdate();
            if (res > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Member added successfully!").show();
                ClearClicked(null);
            } else {
                new Alert(Alert.AlertType.ERROR, "Member Adding failed!").show();
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageMemberFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateMember() {
        String query = "update member set name = ? , address = ? , nic = ? , contact_no = ? where id = ?";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(5, txtMemberID.getText());
            stmt.setObject(3, txtNIC.getText());
            stmt.setObject(1, txtName.getText());
            stmt.setObject(2, txtAdr.getText());
            stmt.setObject(4, txtContact.getText());
            int res = stmt.executeUpdate();
            if (res > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Member updated successfully!").show();
                ClearClicked(null);
            } else {
                new Alert(Alert.AlertType.ERROR, "Member updating failed!").show();
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageMemberFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTable() {
        ArrayList<MemberTV> members = new ArrayList<>();
        String query = "select * from member";
        try {
            Connection conn = DBConnection.getDBConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                members.add(new MemberTV(rs.getString("id"), rs.getString("name"), rs.getString("nic"), rs.getString("address"), rs.getString("contact_no")));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ManageMemberFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblMember.setItems(FXCollections.observableArrayList(members));
    }

    @FXML
    private void ClearClicked(ActionEvent event) {
        txtName.setText("");
        txtNIC.setText("");
        txtAdr.setText("");
        txtContact.setText("");
        txtMemberID.setText("");
    }

    @FXML
    private void tblClicked(MouseEvent event) {
        switch (event.getClickCount()) {
            case 1:
                setDetails();
                break;
            case 2:
                TransactionOpener();
                break;
        }
    }

    private void setDetails() {
        MemberTV member = tblMember.getSelectionModel().getSelectedItem();
        txtName.setText(member.getName());
        txtMemberID.setText(member.getMemberid());
        txtAdr.setText(member.getAdr());
        txtNIC.setText(member.getNic());
        txtContact.setText(member.getContact());
    }

    private void TransactionOpener(){
        callmem = tblMember.getSelectionModel().getSelectedItem().getMemberid();
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/lk/ijse/librarysystem/view/MemberTransactionsForm.fxml"));
        Parent root = null;
        try {
            root = fXMLLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(ManageMemberFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public static String getmemberid(){
        return callmem;
    }
    @FXML
    private void HomeClicked(MouseEvent event) {
        MainFormController.navigateToHome(root, (Stage)root.getScene().getWindow());
    }

    @FXML
    private void ReportClicked(ActionEvent event) {
        InputStream strm = getClass().getResourceAsStream("/lk/ijse/librarysystem/reports/JasperTest.jasper");
        HashMap map = new HashMap();
        map.put("Name", "2000.00");
        try {
            JasperPrint fillReport = JasperFillManager.fillReport(strm, map , DBConnection.getDBConnection().getConnection());
            JasperViewer.viewReport(fillReport);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            Logger.getLogger(ManageMemberFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
