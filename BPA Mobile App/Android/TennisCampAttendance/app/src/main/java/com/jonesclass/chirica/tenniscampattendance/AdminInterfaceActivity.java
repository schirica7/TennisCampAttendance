package com.jonesclass.chirica.tenniscampattendance;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * What a user sees when logging in with the admin account.
 */
public class AdminInterfaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Fields here:
    private static ArrayList<String> camps = new ArrayList<>(Arrays.asList("Juniors", "Intermediates", "Top Guns", "Elites"));
    Button addMemberButton;
    Button logOutButton;
    Button clearButton;
    RecyclerView membersRV;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    DatabaseHelper databaseHelper;
    Spinner campSpinner;
    ArrayAdapter<String> arrayAdapter;
    String currentCampName = "Default Camp";
    List<Member> memberList = new ArrayList<>();
    List<Member> currentCampMemberList = new ArrayList<>();

    @Override
    /**
     * What happens when activity is first generated.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_interface);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        membersRV = findViewById(R.id.recyclerView_members);

        Log.d("InterfaceActivity", "MEMBERS: " + databaseHelper.getMembers().toString());

        //Initializing addMember button; on click, send user to NewMemberActivity.
        addMemberButton = findViewById(R.id.button_addMember);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getResultsIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
                memberActivityResultLauncher.launch(getResultsIntent);
            }
        });

        //Initializing clearButton; on click, clear database and RecyclerView of whatever members are in the camp indicated by the spinner.
        clearButton = findViewById(R.id.button_clearCamp);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentCamp = campSpinner.getSelectedItem().toString();
                memberList = databaseHelper.getMembers();
                AlertDialog alertDialog = new AlertDialog.Builder(AdminInterfaceActivity.this).create();
                alertDialog.setTitle("Data Deletion Warning!");

                //Validating what the spinner says:
                if (currentCamp.equals("All Camps")) {
                    alertDialog.setMessage("Are you sure you want to delete ALL members?");
                } else {
                    alertDialog.setMessage("Are you sure you want to delete the members in camp " + currentCamp + "?");
                }

                //What happens when user clicks yes on alert dialog:
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Find current camp name
                                if (campSpinner.getSelectedItem().toString().equals("All Camps")) {
                                    if (memberList.size() > 0) {
                                        //Remove members from database as well as fields in this class.
                                        databaseHelper.removeAllMembers();
                                        memberList.clear();
                                        currentCampMemberList.clear();
                                        adapter.notifyDataSetChanged();
                                        dialogInterface.dismiss();
                                        setup();
                                    } else {
                                        Toast.makeText(AdminInterfaceActivity.this, "No members to clear!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                } else {
                                    currentCampMemberList.clear();

                                    //See if any member's camp is the option indicated by the spinner.
                                    for (int j = 0; j < memberList.size(); j++) {
                                        if (memberList.get(j).getCamp().equals(currentCampName)) {
                                            currentCampMemberList.add(memberList.get(j));
                                        }
                                    }

                                    //If there are members to remove:
                                    if (currentCampMemberList.size() > 0) {
                                        //admcurrentCampMemberList.clear();

                                        for (int j = 0; j < currentCampMemberList.size(); j++) {
                                            databaseHelper.removeMember(currentCampMemberList.get(j));
                                        }
                                        adapter.notifyDataSetChanged();
                                        dialogInterface.dismiss();
                                        currentCampMemberList.clear();
                                        setup();
                                    } else {
                                        Toast.makeText(AdminInterfaceActivity.this, "No members in camp " + currentCamp + " to clear!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                }
                                //memberList.clear();

                            }
                        }
                );

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }
                );


                alertDialog.show();
            }
        });

        //Initializing campSpinner and feeding it options.
        campSpinner = findViewById(R.id.spinner_camp);
        campSpinner.setOnItemSelectedListener(this);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.getCamps());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campSpinner.setAdapter(arrayAdapter);

        //Sets default spinner selection to all camps.
        String camp = "All Camps";
        int spinnerPosition = arrayAdapter.getPosition(camp);
        campSpinner.setSelection(spinnerPosition);
        setup();

        //Takes user back to main activity.
        logOutButton = findViewById(R.id.button_logOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    /**
     * What happens every additional time this activity comes into view
     */
    protected void onStart() {
        super.onStart();
        setup();
    }


    /**
     * Updates screen.
     */
    public void setup() {
        currentCampName = campSpinner.getSelectedItem().toString();
        //camps = databaseHelper.getCamps();
        memberList = databaseHelper.getMembers();
        Log.d("InterfaceActivity", "MEMBER LIST: " + memberList.toString());


        if (currentCampName.equals("All Camps")) {
            //system.out.println
            //currentCampMemberList.equals(memberList);
            adapter = new RecyclerAdapter(memberList, databaseHelper, AdminInterfaceActivity.this);
            layoutManager = new LinearLayoutManager(AdminInterfaceActivity.this);
            membersRV.setLayoutManager(layoutManager);
            membersRV.setItemAnimator(new DefaultItemAnimator());
            membersRV.setAdapter(adapter);
            Log.d("InterfaceActivity", "ALL MEMBERS: " + currentCampMemberList.toString());
            //return;
        } else {
            currentCampMemberList.clear();

            for (int i = 0; i < memberList.size(); i++) {
                if (memberList.get(i).getCamp().equals(currentCampName)) {
                    currentCampMemberList.add(memberList.get(i));
                }
            }

            adapter = new RecyclerAdapter(currentCampMemberList, databaseHelper, AdminInterfaceActivity.this);
            layoutManager = new LinearLayoutManager(AdminInterfaceActivity.this);
            membersRV.setLayoutManager(layoutManager);
            membersRV.setItemAnimator(new DefaultItemAnimator());
            membersRV.setAdapter(adapter);

        }
    }

    @Override
    /**
     * What happens every time an option is selected in the spinner
     */
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: textview.setText(array[i]);
        setup();
    }

    @Override
    /**
     * What happens every time nothing is selected in the spinner; in this case, nothing needs to happen.
     */
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Returning data back from new member activity.
     */
    ActivityResultLauncher<Intent> memberActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Member member = (Member) result.getData().getSerializableExtra("com.jonesclass.chirica.roster.NEW_MEMBER");
                        memberList.add(member);

                        if (member.getCamp().equals(campSpinner.getSelectedItem().toString())) {
                            currentCampMemberList.add(member);
                        }
                        // rosterSpinner.select
                        boolean successful = databaseHelper.addMember(member);

                        if (successful) {
                            Toast.makeText(AdminInterfaceActivity.this, "Entry successfully added!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminInterfaceActivity.this, "Unsuccessful entry!", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyItemInserted(memberList.size());
                        setup();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(AdminInterfaceActivity.this, "Action Canceled.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}
