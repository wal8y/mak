package com.example.sqllight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class inserting extends AppCompatActivity {
    private DBmang dbManager;
    private Spinner typeSpinner;
    private EditText dateInput;
    private EditText cashInput;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserting);

        dbManager = new DBmang(this);
        typeSpinner = findViewById(R.id.typeInput);
        dateInput = findViewById(R.id.dateInput);
        cashInput = findViewById(R.id.cashInput);
        Button saveButton = findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Auto-fill the date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateInput.setText(currentDate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeSpinner.getSelectedItem().toString();
                String date = dateInput.getText().toString();
                int cash = Integer.parseInt(cashInput.getText().toString());

                long id = dbManager.insertExpense(type, date, cash);
                if (id > 0) {
                    Toast.makeText(inserting.this, "Expense Added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(inserting.this, "Error Adding Expense", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View v){
        finish();
    }
}
