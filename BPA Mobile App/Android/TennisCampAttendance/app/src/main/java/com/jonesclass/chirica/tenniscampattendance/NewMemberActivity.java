package com.jonesclass.chirica.tenniscampattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity that appears when the user wants to add a new member.
 */
public class NewMemberActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Fields.
    private static ArrayList<String> camps = new ArrayList<>(Arrays.asList("Juniors", "Intermediates", "Top Guns", "Elites"));
    EditText firstNameEditText;
    EditText lastNameEditText;
    Switch activeSwitch;
    Button submitButton;
    Button cancelButton;
    Spinner campSpinner;
    DatabaseHelper databaseHelper;

    @Override
    /**
     * What happens when the activity is first generated.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        //Initializing database helper.
        databaseHelper = new DatabaseHelper(getApplicationContext());

        //Initializing screen components that do not require an OnClickListener.
        firstNameEditText = findViewById(R.id.editText_firstName);
        lastNameEditText = findViewById(R.id.editText_lastName);
        activeSwitch = findViewById(R.id.switch_active);

        //Initializing spinner and populating it with options.
        campSpinner = findViewById(R.id.spinner_selectCamp);
        campSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, camps.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campSpinner.setAdapter(adapter);
        //campSpinner

        /*Initializing submit button.
        On click, it will create a new member and add it to the database.
         */
        submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameEditText.getText().toString().length() > 0 &&
                        lastNameEditText.getText().toString().length() > 0) {

                    //TODO: fix roster, find a way to get the current roster
                    Member member = new Member(firstNameEditText.getText().toString(),
                            lastNameEditText.getText().toString(),
                            -1, activeSwitch.isChecked(), campSpinner.getSelectedItem().toString());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("com.jonesclass.chirica.roster.NEW_MEMBER", member);
                    Log.d("NewMemberActivity", member.toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a first and last name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        Initializing cancel button, which will remove this activity from the screen.
         */
        cancelButton = findViewById(R.id.button_cancelSubmit);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

    }

    @Override
    /**
     * Unused method required by the implementation of AdapterView.OnItemSelectedListener.
     */
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    /**
     * Unused method required by the implementation of AdapterView.OnItemSelectedListener.
     */
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}