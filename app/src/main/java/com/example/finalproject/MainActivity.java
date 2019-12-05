package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<View> itemList = new ArrayList<>();
    private List<RadioButton> doneList = new ArrayList<>();
    private List<Button> deleteList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button add = findViewById(R.id.add);
        final Button finish = findViewById(R.id.finish);
        Calendar calendar = Calendar.getInstance();
        final String mMonth = month(calendar.get(Calendar.MONTH));
        String nDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (nDay.length() < 2) {
            nDay = "0" + nDay;
        }
        final String mDay = nDay;
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        String toShow = mMonth + "/" + mDay + ", ";
        if ("1".equals(mWay)) {
            toShow += "Sunday";
        } else if ("2".equals(mWay)) {
            toShow += "Monday";
        } else if ("3".equals(mWay)) {
            toShow += "Tueday";
        } else if ("4".equals(mWay)) {
            toShow += "Wednesday";
        } else if ("5".equals(mWay)) {
            toShow += "Thursday";
        } else if ("6".equals(mWay)) {
            toShow += "Friday";
        } else if ("7".equals(mWay)) {
            toShow += "Saturday";
        }
        ((TextView) findViewById(R.id.dateBar)).setText(toShow);
        ((TextView) findViewById(R.id.dateBar)).setTextColor(Color.BLACK);
        add.setOnClickListener(v -> {
            View toAdd = getLayoutInflater().inflate(R.layout.item, null);
            itemList.add(toAdd);
            doneList.add(toAdd.findViewById(R.id.done));
            deleteList.add(toAdd.findViewById(R.id.delete));
            ((TextView) toAdd.findViewById(R.id.day)).setText(mDay);
            ((TextView) toAdd.findViewById(R.id.month)).setText(mMonth);
            changeText(toAdd.findViewById(R.id.content), toAdd.findViewById(R.id.month), toAdd.findViewById(R.id.day), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1);
            updateDisplay();
        });
        finish.setOnClickListener(v -> {
            boolean allDone = true;
            for (RadioButton done: doneList) {
                if (!done.isChecked()) {
                    allDone = false;
                }
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (allDone) {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "http://yerkee.com/api/fortune",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    builder.setMessage("Good job! You have finished all your to-do today!\nThis is your fortune today:\n" + response.getString("fortune"));
                                    builder.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                builder.setMessage("Error");
                                builder.show();
                            }
                        });
                requestQueue.add(objectRequest);
            } else {
                builder.setMessage("Oh no! You have not done yet! Try to click finish after finishing all your to-do.");
                builder.show();
            }
        });
    }
    /*private String retrieveFortune() {
        String url = "http://yerkee.com/api/fortune";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String toReturn = "";
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                return response.toString();
            }
        })
    }*/
    private static String month(int m) {
        if (m == 0) {
            return "Jan";
        } else if (m == 1) {
            return "Feb";
        } else if (m == 2) {
            return "Mar";
        } else if (m == 3) {
            return "Apr";
        } else if (m == 4) {
            return "May";
        } else if (m == 5) {
            return "Jun";
        } else if (m == 6) {
            return "Jul";
        } else if (m == 7) {
            return "Aug";
        } else if (m == 8) {
            return "Sep";
        } else if (m == 9) {
            return "Oct";
        } else if (m == 10) {
            return "Nov";
        } else {
            return "Dec";
        }
    }
    private static int reverseMonth(String m) {
        if (m.equals("Jan")) {
            return 0;
        } else if (m.equals("Feb")) {
            return 1;
        } else if (m.equals("Mar")) {
            return 2;
        } else if (m.equals("Apr")) {
            return 3;
        } else if (m.equals("May")) {
            return 4;
        } else if (m.equals("Jun")) {
            return 5;
        } else if (m.equals("Jul")) {
            return 6;
        } else if (m.equals("Aug")) {
            return 7;
        } else if (m.equals("Sep")) {
            return 8;
        } else if (m.equals("Oct")) {
            return 9;
        } else if (m.equals("Nov")) {
            return 10;
        } else {
            return 11;
        }
    }
    private void adjustDay(int month, View container) {
        final Spinner setDay = container.findViewById(R.id.setDay);
        int i = month;
        int selectedDay = setDay.getSelectedItemPosition();
        if (i == 1) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.twenty_eight_day_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setDay.setAdapter(adapter);
            if (selectedDay >= 28) {
                setDay.setSelection(27);
            } else {
                setDay.setSelection(selectedDay);
            }
        } else if (i == 3 || i == 5 || i == 8 || i == 10) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.thirty_day_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setDay.setAdapter(adapter);
            if (selectedDay >= 30) {
                setDay.setSelection(29);
            } else {
                setDay.setSelection(selectedDay);
            }
        } else {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.thirty_one_day_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setDay.setAdapter(adapter);
            if (selectedDay >= 31) {
                setDay.setSelection(30);
            } else {
                setDay.setSelection(selectedDay);
            }
        }
    }
    private TextView changeText(final TextView textView, final TextView month, final TextView day, int m, int d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View container = getLayoutInflater().inflate(R.layout.add_message,null, false);
        final EditText setContent = container.findViewById(R.id.setContent);
        final Spinner setMonth = container.findViewById(R.id.setMonth);
        final Spinner setDay = container.findViewById(R.id.setDay);
        setMonth.setSelection(m);
        setDay.setSelection(d);
        if (textView.getText().toString().length() > 0) {
            setContent.setText(textView.getText().toString());
        }
        setMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adjustDay(setMonth.getSelectedItemPosition(), container);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setView(container).setPositiveButton(R.string.Continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(setContent.getText());
                textView.setTextColor(Color.BLACK);
                String month1 = month(setMonth.getSelectedItemPosition());
                String day1 = "" + (setDay.getSelectedItemPosition() + 1);
                if(day1.length() < 2) {
                    day1 = "0" + day1;
                }
                month.setText(month1);
                day.setText(day1);
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
            toAdd.findViewById(R.id.contentContainer).setOnClickListener(v -> {
                changeText(textView, toAdd.findViewById(R.id.month), toAdd.findViewById(R.id.day), reverseMonth(((TextView) toAdd.findViewById(R.id.month)).getText().toString()), Integer.parseInt(((TextView) toAdd.findViewById(R.id.day)).getText().toString()) - 1);
            });
            toAdd.findViewById(R.id.delete).setOnClickListener(v -> {
                int index = deleteList.indexOf(toAdd.findViewById(R.id.delete));
                if (index >= 0 && index < itemList.size()) {
                    itemList.remove(index);
                    deleteList.remove(index);
                    doneList.remove(index);
                }
                updateDisplay();
            });
            itemContainer.addView(toAdd);
        }
    }
}
