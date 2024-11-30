package com.example.sqllight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

public class type_expenses extends AppCompatActivity {
    private DBmang dbManager;
    private ListView expensesListView;
    TextView tv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_expenses);
        tv = findViewById(R.id.title2);
        dbManager = new DBmang(this);
        expensesListView = findViewById(R.id.expensesListView);
        String type = getIntent().getStringExtra("expense_type");
        List<Expense> expenses = dbManager.getExpensesByType(type);
        tv.setText(type);
        listAdapter adapter = new listAdapter(this, expenses);
        expensesListView.setAdapter(adapter);
    }

    public void back(View v){
        finish();
    }
}