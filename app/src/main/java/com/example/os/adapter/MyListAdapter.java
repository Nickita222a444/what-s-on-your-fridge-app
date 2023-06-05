package com.example.os.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.os.DBHelper;
import com.example.os.R;

import java.util.Objects;

public class MyListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MyListAdapter(Context context, String[] values) {
        super(context, R.layout.shop_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.shop_list_item, parent, false);
        CheckedTextView textView = rowView.findViewById(R.id.text);
        textView.setText(values[position]);

        android.database.sqlite.SQLiteDatabase db = new DBHelper(getContext()).getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery("SELECT bought FROM buy where id = " + (position + 1), null);
        cursor.moveToFirst();
        String bought = cursor.getString(0);
        db.close();

        if(Objects.equals(bought, "1")) {
            textView.setTextColor(Color.parseColor("#606366"));
            textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return rowView;
    }
}