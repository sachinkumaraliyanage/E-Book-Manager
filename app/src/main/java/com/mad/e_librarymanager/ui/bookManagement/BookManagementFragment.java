package com.mad.e_librarymanager.ui.bookManagement;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.e_librarymanager.AdminDashboard;
import com.mad.e_librarymanager.R;
import com.mad.e_librarymanager.ui.gallery.GalleryViewModel;

public class BookManagementFragment extends Fragment {

    Button btn_test;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_book_management, container, false);

        btn_test = root.findViewById(R.id.btn_frag_book_management);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Working",Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

}