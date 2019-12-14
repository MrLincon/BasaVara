package com.example.basavara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basavara.Adapters.MyExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectDivisionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    ExpandableListView expandableListView;
    List<String> division;
    Map<String, List<String>> city;

    ExpandableListAdapter expandableListAdapter;

    private String division_name;
    private String city_name;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Division = "division";
    public static final String Location = "location";

    private String loadlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_division);

        toolbar = findViewById(R.id.toolbar_change_division);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Select Location");


        expandableListView = findViewById(R.id.expandableListView);
        getData();

        expandableListAdapter = new MyExpandableListAdapter(this, division, city);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                division_name = division.get(groupPosition);
                city_name = city.get(division.get(groupPosition)).get(childPosition);

                saveData();
//                loadData();
//                updateData();
                finish();
                return false;
            }
        });



    }

    public void getData() {

        division = new ArrayList<>();
        city = new HashMap<>();

        division.add("Dhaka");
        division.add("Chattogram");
        division.add("Rajshahi");
        division.add("Khulna");
        division.add("Sylhet");
        division.add("Mymenshingh");
        division.add("Rangpur");
        division.add("Barishal");

        List<String> dhaka = new ArrayList<>();
        List<String> chattogram = new ArrayList<>();
        List<String> rajshahi = new ArrayList<>();
        List<String> khulna = new ArrayList<>();
        List<String> sylhet = new ArrayList<>();
        List<String> mymenshingh = new ArrayList<>();
        List<String> rangpur = new ArrayList<>();
        List<String> barishal = new ArrayList<>();

        rajshahi.add("All basa in Rajshahi");
        rajshahi.add("Sadar");
        rajshahi.add("Shaheb Bazar");
        rajshahi.add("New Market");
        rajshahi.add("Uposhahar");
        rajshahi.add("Padma Residential Area");
        rajshahi.add("Sagorpara");
        rajshahi.add("Kazla");
        rajshahi.add("Talaimari");
        rajshahi.add("Court");


        city.put(division.get(0), dhaka);
        city.put(division.get(1), chattogram);
        city.put(division.get(2), rajshahi);
        city.put(division.get(3), khulna);
        city.put(division.get(4), sylhet);
        city.put(division.get(5), mymenshingh);
        city.put(division.get(6), rangpur);
        city.put(division.get(7), barishal);

    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Division, division_name);
        editor.putString(Location, city_name);

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loadlocation = sharedPreferences.getString(Location, "");
    }

    public void updateData() {
//        Toast.makeText(SelectDivisionActivity.this,location, Toast.LENGTH_SHORT).show();
    }
}
