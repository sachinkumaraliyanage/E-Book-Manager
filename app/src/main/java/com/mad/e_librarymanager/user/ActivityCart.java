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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mad.e_librarymanager.R;
import com.mad.e_librarymanager.admin.Admin_Add_Book;
import com.mad.e_librarymanager.admin.Admin_View_Books;
import com.mad.e_librarymanager.admin.Book;
import com.mad.e_librarymanager.user.utill.Cart;
import com.mad.e_librarymanager.user.utill.CartBookAdapter;
import com.mad.e_librarymanager.user.utill.CartItem;
import com.mad.e_librarymanager.user.utill.UserBookAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityCart extends AppCompatActivity  implements NumberPicker.OnValueChangeListener{


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db;

    CartBookAdapter adapter;

    ProgressDialog pd;
    MenuItem cart=null;
    TextView tx_tot;

    public Book selectedBook=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        recyclerView = findViewById(R.id.cart_book_viwe);
        //set recycler view Properties
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tx_tot = findViewById(R.id.tx_tot);



        showData();
    }

    public void showData() {
        //progress dialog

        pd.setTitle("Loading Data");
        pd.show();
        adapter = new CartBookAdapter(ActivityCart.this,Cart.cartBook);
        recyclerView.setAdapter(adapter);
        tx_tot.setText("Total : Rs. "+Cart.getCart().getTotalPrice());
        pd.dismiss();

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Cart.getCart().updateCart(this.selectedBook,picker.getValue());
        showData();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void addCart(View v){
        pd.setTitle("Adding data to FireBase");

        pd.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id",id);
        List <Map>itemlist=new ArrayList<>();
        for (CartItem cartItem : Cart.cartList){
            Map<String, Object> item = new HashMap<>();
            item.put("bookId",cartItem.getBook().getId());
            item.put("count",cartItem.getCount());
            item.put("unitPrice",cartItem.getPrice());
            itemlist.add(item);
        }
        doc.put("order",itemlist);
        doc.put("total",Cart.getCart().getTotalPrice());
        //add data to firebase
        db.collection("Orders").document(id).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(ActivityCart.this,"Oder Placed",Toast.LENGTH_LONG).show();
                Cart.getCart().reset();
                showData();

                startActivity(new Intent(ActivityCart.this, User_Main.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ActivityCart.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


    }


}