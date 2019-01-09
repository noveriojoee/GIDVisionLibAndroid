package gid.com.gidvisionlib.ViewActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import gid.com.gidvisionlib.Common.Activity.BaseActivity;
import gid.com.gidvisionlib.R;
import gid.com.gidvisionlib.ViewActivity.TextRecognition.OcrCaptureActivity;

public class GIDLibVisionMainActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "GIDLibVisionMainActivity";


    private Button btnPickImage;
    private Button btnRunGoogleVision;
    private ImageView imageResultView;

    @Override
    protected void bindView() {
        this.btnPickImage =  findViewById(R.id.btnPickImage);
        this.btnRunGoogleVision = findViewById(R.id.btnRunGoogleVision);
        this.imageResultView = findViewById(R.id.imgaePreview);
    }

    @Override
    protected void registerEvent() {
        this.btnPickImage.setOnClickListener(this);
        this.btnRunGoogleVision.setOnClickListener(this);
        this.imageResultView.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onCreateSetContentView(R.layout.activity_gidlib_vision_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void goToNewIntent(int id) {

    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.btnPickImage)){
            Snackbar.make(v, "btnPickImage", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else if(v.equals(this.btnRunGoogleVision)){
            Intent i = new Intent(this,OcrCaptureActivity.class);
            this.startActivity(i);
        }else{
            System.out.println("Click action not found");
        }
    }


}
