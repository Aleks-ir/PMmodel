package by.alexeytisserand.pmmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import by.alexeytisserand.phmodels.R;


public class MainActivity extends AppCompatActivity {
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setBackgroundImage();
        buttonEffect();
    }

    public void startCannot(View view) {
        Intent intent = new Intent(this, Cannon.class);
        startActivity(intent);
        finish();
    }


    private void setBackgroundImage() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.mainlayout);
        layout.setBackgroundResource(R.drawable.background_main);
    }
    private void buttonEffect(){
        imageButton = findViewById(R.id.btn_play);
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        imageButton.setImageResource(R.drawable.ic_play_2);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        imageButton.setImageResource(R.drawable.ic_play_1);
                        break;
                    }
                }
                return false;
            }
        });
    }
}
