package com.mad.e_librarymanager.admin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mad.e_librarymanager.R;

public class Book_ViewHolder extends RecyclerView.ViewHolder {

    public TextView book_id, book_title, book_author, book_price;
    public View mView;


    public Book_ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });

        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });

        //initialize view with model layout
        book_id = itemView.findViewById(R.id.view_book_id);
        book_title = itemView.findViewById(R.id.view_book_title);
        book_author = itemView.findViewById(R.id.view_book_author);
        book_price = itemView.findViewById(R.id.view_book_price);



    }

    private Book_ViewHolder.ClickListener mClickListener;

    //interface for click listner
    public interface ClickListener{

        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);

    }
    public void setOnClickListener(Book_ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
