package io.github.dnivra26.kaito;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login_signup)
public class LoginSignupActivity extends AppCompatActivity {


    @ViewById(R.id.username)
    EditText usernameEditText;

    @ViewById(R.id.password)
    EditText passwordEditText;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    public void setupToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_activity_login_signup));
            setSupportActionBar(toolbar);
        }
    }

    @Click(R.id.login)
    public void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        progressDialog.show();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),
                            "Successfully Logged In",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginSignupActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Log in Error", Toast.LENGTH_LONG)
                            .show();
                }
                progressDialog.dismiss();
            }
        });
    }

    @Click(R.id.signup)
    public void signUp() {
        final String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals("") && password.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {
            ParseUser user = new ParseUser();

            user.setUsername(username);
            user.setPassword(password);

            final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
            progressDialog.show();

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("username", username);
                        installation.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Signed up, please log in.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Sign up Error", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error", Toast.LENGTH_LONG)
                                .show();
                    }
                    progressDialog.dismiss();
                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
