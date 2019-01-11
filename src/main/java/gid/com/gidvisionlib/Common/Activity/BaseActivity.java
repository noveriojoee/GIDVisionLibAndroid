package gid.com.gidvisionlib.Common.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void bindView();
    protected abstract void registerEvent();


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    public void onCreateSetContentView(int layoutViewId) {
        this.setContentView(layoutViewId);
        this.bindView();
        this.registerEvent();
    }

    public abstract void goToNewIntent(int id);
}
