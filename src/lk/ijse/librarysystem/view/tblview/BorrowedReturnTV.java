/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.view.tblview;

import java.util.Date;

/**
 *
 * @author badhr
 */
public class BorrowedReturnTV {
    private String bookid, borrowid, returnid;
    private Date borrowdate, returndate;

    public BorrowedReturnTV() {
    }

    public BorrowedReturnTV(String bookid, String borrowid, String returnid, Date borrowdate, Date returndate) {
        this.bookid = bookid;
        this.borrowid = borrowid;
        this.returnid = returnid;
        this.borrowdate = borrowdate;
        this.returndate = returndate;
    }

    /**
     * @return the bookid
     */
    public String getBookid() {
        return bookid;
    }

    /**
     * @param bookid the bookid to set
     */
    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    /**
     * @return the borrowid
     */
    public String getBorrowid() {
        return borrowid;
    }

    /**
     * @param borrowid the borrowid to set
     */
    public void setBorrowid(String borrowid) {
        this.borrowid = borrowid;
    }

    /**
     * @return the returnid
     */
    public String getReturnid() {
        return returnid;
    }

    /**
     * @param returnid the returnid to set
     */
    public void setReturnid(String returnid) {
        this.returnid = returnid;
    }

    /**
     * @return the borrowdate
     */
    public Date getBorrowdate() {
        return borrowdate;
    }

    /**
     * @param borrowdate the borrowdate to set
     */
    public void setBorrowdate(Date borrowdate) {
        this.borrowdate = borrowdate;
    }

    /**
     * @return the returndate
     */
    public Date getReturndate() {
        return returndate;
    }

    /**
     * @param returndate the returndate to set
     */
    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }
    
}
