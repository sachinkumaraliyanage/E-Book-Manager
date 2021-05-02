package com.mad.e_librarymanager.user;

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
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mad.e_librarymanager.MainActivity;
import com.mad.e_librarymanager.R;
import com.mad.e_librarymanager.admin.Book;
import com.mad.e_librarymanager.user.utill.Cart;
import com.mad.e_librarymanager.user.utill.UserBookAdapter;

import java.util.ArrayList;
import java.util.List;


public class User_Main extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    List<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db;

    UserBookAdapter adapter;

    ProgressDialog pd;
    MenuItem cart=null;

    public int pickValue=0;
    public Book selectedBook=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__main);

        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);

        recyclerView = findViewById(R.id.user_book_viwe);
        //set recycler view Properties
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



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
                adapter = new UserBookAdapter(User_Main.this,bookList);
                //set adapter to Recycler view
                recyclerView.setAdapter(adapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(User_Main.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if(picker.getValue()>0){
            Cart.getCart().addCart(this.selectedBook,picker.getValue());
        }
        updateCart();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                        adapter = new UserBookAdapter(User_Main.this,bookList);
                        //set adapter to Recycler view
                        recyclerView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(User_Main.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //infalting book_menu.xml
        getMenuInflater().inflate(R.menu.user_menu,menu);
        //search view
        MenuItem item = menu.findItem(R.id.action_search);
        this.cart = menu.findItem(R.id.action_cart);
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
//                searchData(newText);
                return false;
            }
        });
        updateCart();
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            if(Cart.cartList.size()>0){
                Intent intent = new Intent(User_Main.this, ActivityCart.class);
                startActivity(intent);
            }else{
                Toast.makeText(User_Main.this,"Cart Empty",Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    public void updateCart(){
        if(Cart.cartList.size()>0){
            this.cart.setIcon(R.drawable.ic_add_cart);
        }else{
            this.cart.setIcon(R.drawable.ic_cart);
        }
    }
}