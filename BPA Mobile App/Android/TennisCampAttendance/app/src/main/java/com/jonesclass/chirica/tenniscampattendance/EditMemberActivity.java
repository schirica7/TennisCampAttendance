package com.jonesclass.chirica.tenniscampattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
 * Activity that appears when the admin wants to edit the information of a preexisting member.
 */
public class EditMemberActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Fields.
    private static ArrayList<String> camps = new ArrayList<>(Arrays.asList("Juniors", "Intermediates", "Top Guns", "Elites"));
    EditText firstNameEditText;
    EditText lastNameEditText;
    Switch activeSwitch;
    Spinner campSpinner;
    Button updateButton;
    Button deleteButton;
    Button cancelButton;
    DatabaseHelper databaseHelper;
    //ArrayList<String> camps;

    @Override
    /**
     * What happens on first generation of this activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        //camps = MainActivity.getCamps();
        //camps.remove(0);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        firstNameEditText = findViewById(R.id.editText_firstName);
        lastNameEditText = findViewById(R.id.editText_lastName);
        activeSwitch = findViewById(R.id.switch_active);

        //Sending member from AdminInterfaceActivity.
        Intent editMemberIntent = getIntent();
        Member editMember = (Member) editMemberIntent.getSerializableExtra("com.jonesclass.chirica.roster.EDIT_MEMBER");

        firstNameEditText.setText(editMember.getFirstName());
        lastNameEditText.setText(editMember.getLastName());
        activeSwitch.setChecked(editMember.isActive());

        firstNameEditText.requestFocus();
        firstNameEditText.selectAll();

        //Initializing the spinneer and populating it with options.
        campSpinner = findViewById(R.id.spinner_editCamp);
        campSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, camps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campSpinner.setAdapter(adapter);

        //Setting spinner option to the editMember's camp.
        String camp = editMember.getCamp();
        int spinnerPosition = adapter.getPosition(camp);
        campSpinner.setSelection(spinnerPosition);

        //Button that updates the editMember's information.
        updateButton = findViewById(R.id.button_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Makes sure that
                if (firstNameEditText.getText().toString().length() > 0 &&
                        lastNameEditText.getText().toString().length() > 0) {
                    editMember.setFirstName(firstNameEditText.getText().toString());
                    editMember.setLastName(lastNameEditText.getText().toString());
                    editMember.setActive(activeSwitch.isChecked());
                    editMember.setCamp(campSpinner.getSelectedItem().toString());

                    //databaseHelper = new DatabaseHelper(getApplicationContext());
                    boolean successful = databaseHelper.updateMember(editMember);

                    if (successful) {
                        Toast.makeText(getApplicationContext(), "Member Updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed!", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a first and last name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button that deletes member from database.
        deleteButton = findViewById(R.id.button_deleteMember);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If there's any changes, edit them, then delete the member
                if (firstNameEditText.getText().toString().length() > 0 &&
                        lastNameEditText.getText().toString().length() > 0) {
                    editMember.setFirstName(firstNameEditText.getText().toString());
                    editMember.setLastName(lastNameEditText.getText().toString());
                    editMember.setActive(activeSwitch.isChecked());
                    editMember.setCamp(campSpinner.getSelectedItem().toString());

                    //sdatabaseHelper = new DatabaseHelper(getApplicationContext());
                    boolean itemExists = databaseHelper.removeMember(editMember);

                    if (!itemExists) {
                        Toast.makeText(getApplicationContext(), "Member Deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Deletion Failed!", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a first and last name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button that exits out of this activity.
        cancelButton = findViewById(R.id.button_cancelUpdate);
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