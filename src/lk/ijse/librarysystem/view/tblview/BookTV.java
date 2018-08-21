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
public class BookTV {
    private String bookid,name,isbn,author,publisher;
    private boolean availability;

    public BookTV() {
    }

    public BookTV(String bookid, String name, String isbn, String author, String publisher, boolean availability) {
        this.bookid = bookid;
        this.name = name;
        this.isbn = isbn;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
        return (availability)? "Available" : "Not Available";
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
    
}
