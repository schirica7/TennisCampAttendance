package com.jonesclass.chirica.tenniscampattendance;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * What regular users will see when they log into the app.
 */
public class UserInterfaceActivity extends AppCompatActivity {
    //Fields.
    Button addEntryButton;
    Button logOutButton;
    RecyclerAdapter adapter;
    DatabaseHelper databaseHelper;
    List<Member> entryList = new ArrayList<>();

    @Override
    /**
     * On first generation of activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        //Initializing addEntryButton, and programming it to display NewMemberActivity.
        addEntryButton = findViewById(R.id.button_addEntry);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getResultsIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
                memberActivityResultLauncher.launch(getResultsIntent);
            }
        });

        //Initializing logOutButton, and programming it to dispose of this activity.
        logOutButton = findViewById(R.id.button_logOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    /**
     * Returns member from NewMemberActivity back to this activity.
     * This code runs when NewMemberActivity is opened from UserInterfaceActivity.
     */
    ActivityResultLauncher<Intent> memberActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Member member = (Member) result.getData().getSerializableExtra("com.jonesclass.chirica.roster.NEW_MEMBER");
                        entryList.add(member);
                        boolean successful = databaseHelper.addMember(member);

                        if (successful) {
                            Toast.makeText(UserInterfaceActivity.this, "Entry successfully added!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserInterfaceActivity.this, "Unsuccessful entry!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(UserInterfaceActivity.this, "Action Canceled.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


}