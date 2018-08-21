/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.controller;

import com.jfoenix.controls.JFXButton;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import lk.ijse.librarysystem.db.DBConnection;
import lk.ijse.librarysystem.view.tblview.MemberTV;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author badhr
 */
public class TableController implements Initializable {

    @FXML
    private JFXButton btnAdd;
    @FXML
    private TableView<MemberTV> tblMem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Clicked(ActionEvent event) {
        InputStream strm = getClass().getResourceAsStream("/lk/ijse/librarysystem/reports/Blank_A4.jasper");
        HashMap map = new HashMap();
        map.put("Total", 20);
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(strm, map,DBConnection.getDBConnection().getConnection());
            JasperViewer.viewReport(jasperPrint);
            /*try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(strm, map, new JRBeanCollectionDataSource(tblMem.getItems()));
            
            JasperViewer.viewReport(jasperPrint,false);
            } catch (JRException ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}
