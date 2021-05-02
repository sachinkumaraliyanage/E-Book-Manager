package com.mad.e_librarymanager.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.e_librarymanager.R;

import java.util.List;

public class CustomBookAdapter extends RecyclerView.Adapter<Book_ViewHolder> {

    Admin_View_Books admin_view_books;
    List<Book> bookList;
    Context context;

    public CustomBookAdapter(Admin_View_Books admin_view_books, List<Book> bookList) {
        this.admin_view_books = admin_view_books;
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
                Toast.makeText(admin_view_books,title + " by " + author,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                // Creating Alert Dialog Box
                AlertDialog.Builder builder = new AlertDialog.Builder(admin_view_books);
                //options to display in dialog
                String[] options = {"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0){
                            //update is called
                            //get data
                            String id = bookList.get(position).getId();
                            String title = bookList.get(position).getTitle();
                            String author = bookList.get(position).getAuthor();
                            String price = bookList.get(position).getPrice();

                            Intent intent = new Intent(admin_view_books,Admin_Update_Book.class);
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pAuthor", author);
                            intent.putExtra("pPrice", price);

                            //start activity
                            admin_view_books.startActivity(intent);
                        }
                        if (which == 1){
                            admin_view_books.DeleteData(position);
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
