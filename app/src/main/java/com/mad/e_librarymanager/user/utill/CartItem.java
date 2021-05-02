package com.mad.e_librarymanager.user.utill;

import com.mad.e_librarymanager.admin.Book;

public class CartItem {
    private Book book;
    private int count;
    private double price;

    public CartItem(Book book, int count,Double price) {
        this.book = book;
        this.count = count;
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
