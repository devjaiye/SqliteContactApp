package com.dev_app.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SqliteDatabase mDatabase;
    private ArrayList<Contacts> allContacts=new ArrayList<>();
    private ContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout fLayout =  findViewById(R.id.activity_to_do);

        RecyclerView contactView = findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactView.setLayoutManager(linearLayoutManager);
        contactView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allContacts = mDatabase.listContacts();

        if(allContacts.size() > 0){
            contactView.setVisibility(View.VISIBLE);
            mAdapter = new ContactAdapter(this, allContacts);
            contactView.setAdapter(mAdapter);

        }else {
            contactView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });
    }

    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_contact_layout, null);

        final EditText nameField = subView.findViewById(R.id.enter_name);
        final EditText noField = subView.findViewById(R.id.enter_phno);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new CONTACT");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String ph_no = noField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    Contacts newContact = new Contacts(name, ph_no);
                    mDatabase.addContacts(newContact);

                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter!=null)
                    mAdapter.getFilter().filter(newText);
                return true;
            }
        });


    }
}
