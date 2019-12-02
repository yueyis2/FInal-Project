package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<View> itemList = new ArrayList<>();
    private List<RadioButton> doneList = new ArrayList<>();
    private List<Button> deleteList = new ArrayList<>();
    private List<TextView> toDoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button add = findViewById(R.id.add);
        final Button finish = findViewById(R.id.finish);
        add.setOnClickListener(v -> {
            View toAdd = getLayoutInflater().inflate(R.layout.item, null);
            itemList.add(toAdd);
            doneList.add(toAdd.findViewById(R.id.done));
            deleteList.add(toAdd.findViewById(R.id.delete));
            toDoList.add(changeText(toAdd.findViewById(R.id.content)));
            updateDisplay();
        });
        finish.setOnClickListener(v -> {
            boolean allDone = true;
            for (RadioButton done: doneList) {
                if (!done.isChecked()) {
                    allDone = false;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (allDone) {
                builder.setMessage("Good job! You have finished all your to-do today!");
            } else {
                builder.setMessage("Oh no! You have not done yet! Try to click finish after finishing all your to-do.");
            }
            builder.show();
        });

    }
    private TextView changeText(final TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View container = getLayoutInflater().inflate(R.layout.add_message,null, false);
        final EditText setContent = container.findViewById(R.id.setContent);
        if (textView.getText().toString().length() > 0) {
            setContent.setText(textView.getText().toString());
        }
        builder.setView(container).setPositiveButton(R.string.Continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(setContent.getText());
                textView.setTextColor(Color.BLACK);
            }
        });
        builder.show();
        return textView;
    }
    private void updateDisplay() {
        final LinearLayout itemContainer = findViewById(R.id.itemContainer);
        itemContainer.removeAllViews();
        for (View toAdd: itemList) {
            TextView textView = toAdd.findViewById(R.id.content);
            textView.setOnClickListener(v -> {
                changeText(textView);
            });
            toAdd.findViewById(R.id.delete).setOnClickListener(v -> {
                int index = deleteList.indexOf(toAdd.findViewById(R.id.delete));
                if (index >= 0 && index < itemList.size()) {
                    itemList.remove(index);
                    deleteList.remove(index);
                    doneList.remove(index);
                    toDoList.remove(index);
                }
                updateDisplay();
            });
            itemContainer.addView(toAdd);
        }
    }
}
