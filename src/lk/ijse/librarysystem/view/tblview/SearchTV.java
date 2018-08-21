/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.view.tblview;

/**
 *
 * @author badhr
 */
public class SearchTV {
    private String bookid,bookname,author,publisher;
    private boolean availability;

    public SearchTV() {
    }

    public SearchTV(String bookid, String bookname, String author, String publisher, boolean availability) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.author = author;
        this.publisher = publisher;
        this.availability = availability;
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
     * @return the bookname
     */
    public String getBookname() {
        return bookname;
    }

    /**
     * @param bookname the bookname to set
     */
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the availability
     */
    public String isAvailability() {
        return (availability)? "Available":"Not available";
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
    
}
