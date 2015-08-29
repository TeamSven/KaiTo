package io.github.dnivra26.kaito;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.github.dnivra26.kaito.models.KadaiListAdapter;
import io.github.dnivra26.kaito.models.Vandi;

@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.kadai_list)
    ListView kadaiList;

    @ViewById(R.id.create_new_kadai_fab)
    FloatingActionButton floatingActionButton;

    @AfterViews
    public void setupToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_activity_home));
            setSupportActionBar(toolbar);
        }

        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        final KadaiListAdapter kadaiListAdapter = new KadaiListAdapter(this);
        kadaiListAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Vandi>() {
            @Override
            public void onLoading() {
                progressDialog.show();
            }

            @Override
            public void onLoaded(List<Vandi> list, Exception e) {
                if (list.size() > 0) {
                    //noActiveIssuesLabel.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }
        });
        kadaiList.setAdapter(kadaiListAdapter);
        kadaiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, VandiDetailActivity_.class);
                intent.putExtra("vandi_id", kadaiListAdapter.getItem(position).getObjectId());
                startActivity(intent);
            }
        });
        this.getSupportActionBar().setTitle("Active Tasks");
    }

    @Click(R.id.create_new_kadai_fab)
    public void createNewKadai() {
        startActivity(new Intent(HomeActivity.this, NewKadaiActivity_.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
            progressDialog.show();
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(),
                                "Successfully Logged Out",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this, LoginSignupActivity_.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Log Out Error", Toast.LENGTH_LONG)
                                .show();
                    }
                    progressDialog.dismiss();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
