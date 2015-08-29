package io.github.dnivra26.kaito;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.parse.ParseFile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

@EActivity(R.layout.activity_new_kadai)
public class NewKadaiActivity extends AppCompatActivity {

    public static final int IMAGE_REQUEST_CODE = 111;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.kadai_image)
    ImageView kadaiImage;

    @ViewById(R.id.kadai_title)
    EditText kadaiTitle;

    @ViewById(R.id.kadai_menu)
    EditText kadaiMenu;

    @ViewById(R.id.user_review)
    EditText userReview;

    @ViewById(R.id.kadai_location)
    EditText kadaiLocation;

    @ViewById(R.id.spice_rating)
    RatingBar spiceRating;

    @ViewById(R.id.kadai_rating)
    RatingBar kadaiRating;


    @AfterViews
    public void setupToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_activity_new_kadai));
            setSupportActionBar(toolbar);
        }
    }

    @Click(R.id.kadai_image)
    public void takePicture() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_kadai, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && data.getExtras() != null) {

            Bitmap bp = (Bitmap) data.getExtras().get("data");
            kadaiImage.setImageBitmap(bp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            ParseFile parseFile = new ParseFile("kadai_image.jpg", byteArray);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
