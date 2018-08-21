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
public class BorrowedTV {
    private String bookid,borrowid;
    private Date borrowdate,returndate;
    private long dayspending;

    public BorrowedTV() {
    }

    public BorrowedTV(String bookid, String borrowid, Date borrowdate ) {
        this.bookid = bookid;
        this.borrowid = borrowid;
        this.borrowdate = borrowdate;
        Calendar c2 = Calendar.getInstance();
        c2.setTime(borrowdate);
        c2.add(Calendar.DATE, 15);
        returndate = c2.getTime();
        Calendar c = Calendar.getInstance();
        this.dayspending = (-c.getTime().getTime() + returndate.getTime())/(60000*60*24);
        
       
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
     * @return the dayspending
     */
    public long getDayspending() {
        return dayspending;
    }

    /**
     * @param dayspending the dayspending to set
     */
    public void setDayspending(int dayspending) {
        this.dayspending = dayspending;
    }
    
    
}
