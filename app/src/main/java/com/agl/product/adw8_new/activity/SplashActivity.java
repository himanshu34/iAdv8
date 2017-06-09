package com.agl.product.adw8_new.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.utils.Session;

public class SplashActivity extends ActivityBase {

    private String TAG = "SplashActivity";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new Session(SplashActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        CountDownTimer countDownTimer  = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startAnimation();
            }

            @Override
            public void onFinish() {
                if (!session.checkLogin()) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent principal = new Intent(SplashActivity.this, HomeActivity.class);
                    principal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    principal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(principal);
                    finish();
                }
            }
        };
        countDownTimer.start();
    }

    private void startAnimation() {
        ImageView image = (ImageView)findViewById(R.id.imageView1);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.move);
        image.startAnimation(animation1);
    }
}
