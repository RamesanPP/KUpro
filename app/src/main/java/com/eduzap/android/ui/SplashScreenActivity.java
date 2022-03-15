package com.eduzap.android.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eduzap.android.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;


    //Variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView slogan, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.splash_photo);
        slogan = findViewById(R.id.textView1);
        version = findViewById(R.id.textView2);

        try {
            String vName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String versionName = "Version: "+ vName;
            version.setText(versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Set animation to elements
        image.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);
        version.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Call next screen
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                // Attach all the elements those you want to animate in design
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(slogan, "logo_text");

                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                } else {
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_SCREEN);
    }
}
