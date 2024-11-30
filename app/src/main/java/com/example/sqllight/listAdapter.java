package com.example.sqllight;

import static androidx.core.app.ActivityCompat.recreate;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.sqllight.Expense;

import java.util.List;

public class listAdapter extends BaseAdapter {
    private Context context;
    private List<Expense> expenses;
    private DBmang dbManager;

    public listAdapter(Context context, List<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
        this.dbManager = new DBmang(context);
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return expenses.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expense_item, parent, false);
        }

        Expense expense = expenses.get(position);

        TextView typeText = convertView.findViewById(R.id.expenseType);
        TextView dateText = convertView.findViewById(R.id.expenseDate);
        TextView cashText = convertView.findViewById(R.id.expenseCash);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        typeText.setText(expense.type);
        dateText.setText(expense.date);
        cashText.setText(String.valueOf(expense.cash));

        deleteButton.setOnClickListener(v -> {
            dbManager.deleteExpense(expense.id);
            expenses.remove(position);
            notifyDataSetChanged();
        });

        typeText.setOnClickListener(v -> {
            Intent intent = new Intent(context, type_expenses.class);
            intent.putExtra("expense_type", expense.type);
            context.startActivity(intent);
        });
        return convertView;
    }

}
