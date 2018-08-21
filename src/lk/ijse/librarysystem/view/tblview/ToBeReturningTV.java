/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.view.tblview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author badhr
 */
public class ToBeReturningTV {
    private String borrowid,bookid,memberid;
    private Date returndate,borroweddate;

    public ToBeReturningTV() {
    }

    public ToBeReturningTV(String borrowid, String bookid, String memberid, Date borroweddate) {
        Calendar c = Calendar.getInstance();
        c.setTime(borroweddate);
        c.add(Calendar.DATE, 15);
        this.borrowid = borrowid;
        this.bookid = bookid;
        this.memberid = memberid;
        this.returndate = c.getTime();
        this.borroweddate = borroweddate;
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
     * @return the memberid
     */
    public String getMemberid() {
        return memberid;
    }

    /**
     * @param memberid the memberid to set
     */
    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    /**
     * @return the returndate
     */
    public String getReturndate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(returndate);
    }

    /**
     * @param returndate the returndate to set
     */
    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }

    /**
     * @return the borroweddate
     */
    public Date getBorroweddate() {
        return borroweddate;
    }

    /**
     * @param borroweddate the borroweddate to set
     */
    public void setBorroweddate(Date borroweddate) {
        this.borroweddate = borroweddate;
    }
    
}
