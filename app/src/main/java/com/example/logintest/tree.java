package com.example.logintest;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;



public class tree extends AppCompatActivity implements DroidListener {
    private DroidNet mDroidNet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        DroidNet.init(tree.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(tree.this);
        YoYo.with(Techniques.Shake)
                .duration(650)
                .repeat(1)
                .playOn(findViewById(R.id.treescroll));
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            netIsOn();
        } else {
            netIsOff();
        }
    }


    private void netIsOn(){
    }

    private void netIsOff(){
        Toast toast = Toast.makeText(tree.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(tree.this);
    }
}
