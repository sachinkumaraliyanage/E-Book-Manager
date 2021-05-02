package com.mad.e_librarymanager.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mad.e_librarymanager.R;

import java.util.ArrayList;
import java.util.List;

public class Admin_View_Books extends AppCompatActivity {

    List<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db;

    CustomBookAdapter adapter;

    ProgressDialog pd;

    FloatingActionButton mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__view__books);

        //initialize the fireStore
        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);

        recyclerView = findViewById(R.id.booklist_recycler);
        //set recycler view Properties
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAddButton = findViewById(R.id.floatingAddButton);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_View_Books.this,Admin_Add_Book.class));
                finish();
            }
        });

        //show data in recycler view
        showData();
    }

    private void showData() {
        //progress dialog
        pd.setTitle("Loading Data");
        pd.show();

        db.collection("Documents").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                bookList.clear();
                pd.dismiss();
                //show data
                for (DocumentSnapshot doc: task.getResult()){
                    Book book = new Book(doc.getString("id"),doc.getString("title"),doc.getString("author"),doc.getString("price"));
                    bookList.add(book);
                }

                //adapter
                adapter = new CustomBookAdapter(Admin_View_Books.this,bookList);
                //set adapter to Recycler view
                recyclerView.setAdapter(adapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Admin_View_Books.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void DeleteData(int index){

        pd.setTitle("Deleting Data");
        pd.show();

        db.collection("Documents").document(bookList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Admin_View_Books.this,"Deleted Successfully",Toast.LENGTH_LONG).show();
                       //update data after delete
                        showData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Admin_View_Books.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void searchData(String query) {

        //progress dialog
        pd.setTitle("Searching ...");
        pd.show();

        db.collection("Documents").whereEqualTo("search", query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //called when search succeedded
                        bookList.clear();
                        pd.dismiss();
                        for (DocumentSnapshot doc: task.getResult()){
                            Book book = new Book(doc.getString("id"),doc.getString("title"),doc.getString("author"),doc.getString("price"));
                            bookList.add(book);
                        }

                        //adapter
                        adapter = new CustomBookAdapter(Admin_View_Books.this,bookList);
                        //set adapter to Recycler view
                        recyclerView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Admin_View_Books.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //infalting book_menu.xml
        getMenuInflater().inflate(R.menu.book_menu,menu);
        //search view
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //caled when we press search button
                searchData(query);// function for search
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // called when we start to type
               // searchData(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle other menu item select
        if (item.getItemId() == R.id.action_settings){
            Toast.makeText(this,"This Feature Will Available Soon",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


}