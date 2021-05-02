package com.mad.e_librarymanager.user.utill;

import android.widget.Toast;

import com.mad.e_librarymanager.admin.Book;
import com.mad.e_librarymanager.user.User_Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cart {
    private static Cart cart =null;
    public static List<CartItem> cartList= new ArrayList<>();
    public static List<Book> cartBook= new ArrayList<>();


    private Cart(){

    }

    public static Cart getCart(){
        if(cart==null){
            cart=new Cart();
        }
        return cart;
    }

    public void addCart(Book book, int count){

        CartItem befor=null;
        CartItem after=null;
        for (CartItem cartItem : cartList) {
            if (cartItem.getBook().getId() == book.getId()) {
                befor =cartItem;

                cartItem.setCount(count + cartItem.getCount());
                after=cartItem;

            }
        }
        if(befor!=null){
            cartList.remove(befor);
            cartBook.remove(befor.getBook());
        }
        if(after==null){
            after=new CartItem(book,count,getPrice(book));

        }
        cartList.add(after);
        book =createBook(book,after.getCount(),after.getPrice());
        cartBook.add(book);



    }



    private Book createBook(Book book,int count,double price){
        String titalarray[] = book.getTitle().split("\\(");
        String tital="";
        if(titalarray.length==1){
            tital=titalarray[0];
        }else{
            for(int i=0;i<titalarray.length-1;i++){
                tital+=titalarray[i];
            }
        }
        book.setTitle(tital+" ( "+count+" )");
        String PriceArray[] = book.getPrice().split("\\(");
        book.setPrice(PriceArray[0]+" ( X "+count+" = Rs "+(price*count)+" )");
        return book;
    }

    public void updateCart(Book book,int count){
        if(count ==0){
            removeCart(book);
        }else{
            CartItem befor=null;
            CartItem after=null;
            for (CartItem cartItem : cartList) {
                if (cartItem.getBook().getId() == book.getId()) {
                    befor =cartItem;
                    cartItem.setCount(count);
                    after=cartItem;

                }
            }
            if(befor!=null){
                cartList.remove(befor);
                cartBook.remove(befor.getBook());
            }
            if(after==null){
                after=new CartItem(book,count,getPrice(book));
            }
            cartList.add(after);
            book =createBook(book,after.getCount(),after.getPrice());
            cartBook.add(book);
        }


    }

    public Double getPrice(Book book){
        String priceS[]=book.getPrice().replaceAll(" ","").toLowerCase().split("rs");
        Double price;
        try{
            price = Double.valueOf(priceS[priceS.length-1]);
        }catch (NumberFormatException e){
            e.printStackTrace();
            price=0.00;
        }
        return price;
    }

    public Double getTotalPrice(){
        Double tot=0.0;
        for (CartItem cartItem: cartList){
            tot=tot+(cartItem.getCount()*cartItem.getPrice());
        }
        return tot;
    }

    public void removeCart(Book book){
        CartItem befor=null;
        for (CartItem cartItem : cartList) {
            if (cartItem.getBook().getId() == book.getId()) {
                befor =cartItem;

            }
        }
        if(befor!=null){
            cartList.remove(befor);
            cartBook.remove(befor.getBook());
        }

    }

    public void reset(){
        cartList= new ArrayList<>();
        cartBook= new ArrayList<>();
    }
}
