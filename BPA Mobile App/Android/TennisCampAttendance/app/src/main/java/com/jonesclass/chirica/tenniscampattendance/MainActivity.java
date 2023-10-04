package com.jonesclass.chirica.tenniscampattendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used as login activity.
 */
public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    private static final ArrayList<String> camps = new ArrayList<>(Arrays.asList("All Camps", "Juniors", "Intermediates", "Top Guns", "Elites"));
    private final String welcomeMessage = "Welcome to the Tennis Camp Attendance app!";
    TextView messageTextView;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    /**
     * What happens on first popup of main activity.
     *
     * There are two hardcoded developer accounts for testing purposes.
     *
     * Admin account: Username is "admin" and password is "password"
     * Member account: Username is "test" and password is "password"
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        messageTextView = findViewById(R.id.textView_message);
        usernameEditText = findViewById(R.id.editText_username);
        passwordEditText = findViewById(R.id.editText_password);

        //This button regulates signing in.
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(getApplicationContext(), "Make sure to enter a username!", Toast.LENGTH_SHORT).show();
                } else {
                    //IF USERNAME AND PASSWORD ARE THAT OF ADMIN:
                    if (username.equals("admin")) {
                        if (password.equals("password")) {
                            messageTextView.setText("Loading...");
                            Intent interfaceIntent = new Intent(getApplicationContext(), AdminInterfaceActivity.class);
                            emptyLoginInfo();
                            startActivity(interfaceIntent);
                            messageTextView.setText(welcomeMessage);

                        } else {
                            Toast.makeText(getApplicationContext(), "Admin password incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //IF USERNAME AND PASSWORD ARE THAT OF DEVELOPER TEST MEMBER:
                        if (username.equals("test")) {
                            if (password.equals("password")) {
                                messageTextView.setText("Loading...");
                                Intent interfaceIntent = new Intent(getApplicationContext(), UserInterfaceActivity.class);
                                emptyLoginInfo();
                                startActivity(interfaceIntent);
                                messageTextView.setText(welcomeMessage);
                            } else {
                                Toast.makeText(getApplicationContext(), "Test account password incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //IF THIS IS A USER CREATED ACCOUNT:
                            Log.d("MainActivity", "USERNAME: " + username);

                            //Usernames and passwords are stored in parallel lists, originating from what the database has.
                            List<String> usernames = databaseHelper.getUsernames();
                            Log.d("MainActivity", "ALL USERNAMES: " + usernames.toString());

                            List<String> passwords = databaseHelper.getPasswords();
                            Log.d("MainActivity", "ALL PASSWORDS: " + passwords.toString());

                            //Valid username sees if the database knows the username.
                            boolean validUsername = false;

                            //Checks to see if the username inputted is stored in the database.
                            if (usernames.size() != 0) {
                                for (int i = 0; i < usernames.size(); i++) {
                                    if (usernames.get(i) != null) {
                                        if (usernames.get(i).equals(username)) {
                                            //The database knows the username.
                                            validUsername = true;

                                            //Usernames and passwords are in parallel lists.
                                            if (passwords.get(i).equals(password)) {
                                                messageTextView.setText("Loading...");
                                                Intent userLoginIntent = new Intent(getApplicationContext(), UserInterfaceActivity.class);
                                                emptyLoginInfo();
                                                startActivity(userLoginIntent);
                                                messageTextView.setText(welcomeMessage);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                                //The database does not know the username.
                                if (!validUsername) {
                                    Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                    emptyLoginInfo();
                                }
                            } else {
                                //The database knows no usernames, but same toast output.
                                Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                                emptyLoginInfo();
                            }
                        }
                    }
                }
            }
        });

        //This button regulates the creation of new accounts.
        Button accountButton = findViewById(R.id.button_newAccount);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pops up an alert dialog before creating new accounts.
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("New Account Creation");
                alertDialog.setMessage("Are you sure you want to create a new account?");

                //What happens if you click yes on the alert dialog..
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Gets text out of both text fields
                                String username = usernameEditText.getText().toString();
                                String password = passwordEditText.getText().toString();

                                //If the app's user put something in both of the text fields:
                                if (!username.equals("") && !password.equals("")) {
                                    //This is so a user cannot take the admin or test accounts.
                                    if (username.equalsIgnoreCase("admin") || username.equalsIgnoreCase("test")) {
                                        Toast.makeText(getApplicationContext(), "This is a developer account only!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                        return;
                                    }

                                    //I have this list to make sure that the user does not try to take a taken username.
                                    List<String> usernames = databaseHelper.getUsernames();

                                    //If there are no registered usernames, you can add one as long as it is one word.
                                    if (usernames.size() == 0) {
                                        if (!username.contains(" ")) {
                                            databaseHelper.addUser(username, password);
                                            Log.d("MainActivity", "USERNAMES: " + databaseHelper.getUsernames().toString());
                                            Toast.makeText(getApplicationContext(), "User successfully added!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Username must be one word!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                    } else {
                                        //This section is for if usernames are already registered.
                                        boolean validUsername = false;

                                        //Loops through the usernames list to see if the input from the text field corresponds to any index in the list.
                                        for (int j = 0; j < usernames.size(); j++) {
                                            if (username.equals(usernames.get(j))) {
                                                Toast.makeText(getApplicationContext(), "Username already taken!", Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                                break;
                                            } else {
                                                validUsername = true;
                                            }
                                        }

                                        //If the username is not taken yet, do this; if the username is taken, it won't even get to this.
                                        if (validUsername) {
                                            boolean success;
                                            if ((username != null) && (password != null)) {
                                                if (!username.contains(" ")) {
                                                    success = databaseHelper.addUser(username, password);
                                                    Log.d("MainActivity", "USERNAMES: " + databaseHelper.getUsernames().toString());

                                                    if (success) {
                                                        Toast.makeText(getApplicationContext(), "User successfully added!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Failure to register user!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Username must be one word!", Toast.LENGTH_SHORT).show();
                                                }
                                                dialogInterface.dismiss();
                                            }
                                        }

                                    }
                                } else {
                                    //If the user left one of the text fields empty:
                                    Toast.makeText(getApplicationContext(), "Make sure you fill in the username and password fields!", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }

                                dialogInterface.dismiss();
                            }
                        }
                );

                //What happens if the user clicks no on the alert dialog.
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

    }

    /**
     * Returns the camps field from the top of this class; used for filling spinners
     * @return constant String ArrayList field called camps.
     */
    public static ArrayList<String> getCamps() {
        return camps;
    }

    /**
     * Empties both text fields.
     */
    public void emptyLoginInfo() {
        usernameEditText.setText("");
        passwordEditText.setText("");
    }
}