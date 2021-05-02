package com.mad.e_librarymanager.user.utill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.e_librarymanager.R;
import com.mad.e_librarymanager.admin.Book;
import com.mad.e_librarymanager.admin.Book_ViewHolder;
import com.mad.e_librarymanager.user.ActivityCart;
import com.mad.e_librarymanager.user.BookCountPiker;
import com.mad.e_librarymanager.user.User_Main;

import java.util.List;

public class CartBookAdapter extends RecyclerView.Adapter<Book_ViewHolder>{
    ActivityCart user_main;
    List<Book> bookList;
    Context context;

    public CartBookAdapter(ActivityCart user_main, List<Book> bookList) {
        this.user_main = user_main;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public Book_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_layout, parent, false);

        Book_ViewHolder viewHolder = new Book_ViewHolder(itemView);
        //handle item click
        viewHolder.setOnClickListener(new Book_ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // this will be called when user click an item
                String title = bookList.get(position).getTitle();
                String author = bookList.get(position).getAuthor();
                Toast.makeText(user_main,title + " by " + author,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                user_main.selectedBook=Cart.cartList.get(position).getBook();


                // Creating Alert Dialog Box
                AlertDialog.Builder builder = new AlertDialog.Builder(user_main);
                //options to display in dialog
                String[] options = {"Remove","Change Count"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0){
                            Cart.getCart().removeCart(bookList.get(position));
                            user_main.showData();
                        }
                        else if (which == 1){
                            BookCountPiker newFragment = new BookCountPiker(bookList.get(position).getTitle(),Cart.cartList.get(position).getCount());
                            newFragment.setValueChangeListener(user_main);
                            newFragment.show(user_main.getSupportFragmentManager(), "time picker");
                        }

                    }
                }).create().show();


            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Book_ViewHolder holder, int position) {
        // bind the data to interace
        holder.book_id.setText(bookList.get(position).getId());
        holder.book_title.setText(bookList.get(position).getTitle());
        holder.book_author.setText(bookList.get(position).getAuthor());
        holder.book_price.setText(bookList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
