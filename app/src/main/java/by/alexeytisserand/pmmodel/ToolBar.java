package by.alexeytisserand.pmmodel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.Locale;

import by.alexeytisserand.phmodels.R;

public class ToolBar extends AppCompatActivity {

    static boolean isRunningTime = false, isResetTime = false, isStop = false;
    private boolean isShowTime;
    private int seconds = 0;
    private TextView tvStopwatch;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected static MenuItem pauseItem;

    private void runTimer()
    {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d",  minutes, secs);
                if(isShowTime){
                    tvStopwatch.setText(time);
                }else{
                    tvStopwatch.setText("");
                }

                if (isRunningTime) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);

        pauseItem = menu.findItem(R.id.pauseStop);

        MenuItem timerItem = menu.findItem(R.id.stopwatch);
        tvStopwatch = (TextView) MenuItemCompat.getActionView(timerItem);

        runTimer();

        if (sharedPreferences.getBoolean(AppConstants.KEY_COORDINATES, false)) {
            menu.findItem(R.id.coordinates).setChecked(true);
            CannonView.isDrawСoordinates = true;
        }
        if (sharedPreferences.getBoolean(AppConstants.KEY_TIME, false)) {
            menu.findItem(R.id.time_fly).setChecked(true);
            isShowTime = true;
        }
        if (sharedPreferences.getBoolean(AppConstants.KEY_TRAJECTORY, false)) {
            menu.findItem(R.id.trajectory).setChecked(true);
            CannonView.isDrawTrajectory = true;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.start:
                CannonView.runCannonBall();
                isRunningTime = true;
                if(isStop){
                    isStop = false;
                    pauseItem.setIcon(R.drawable.ic_pause);
                }
                if(isResetTime){
                    seconds = 0;
                    isResetTime = false;
                }

                break;
            case R.id.pauseStop:
                if(isStop) {
                    CannonView.backToStart();
                    CannonView.clearArrayTrajectory();
                    CannonView.stopCannonBall();
                    CannonView.isEditHeight = true;
                    CannonView.isEditAngle = true;
                    seconds = 0;
                }else{
                    CannonView.pauseCannonBall();
                    isStop = true;
                    isRunningTime = false;
                    pauseItem.setIcon(R.drawable.ic_stop);
                }
                break;
            case R.id.coordinates:
                if(CannonView.isDrawСoordinates){
                    item.setChecked(false);
                    CannonView.isDrawСoordinates = false;
                    editor.putBoolean(AppConstants.KEY_COORDINATES, false);
                }else{
                    item.setChecked(true);
                    CannonView.isDrawСoordinates = true;
                    editor.putBoolean(AppConstants.KEY_COORDINATES, true);
                }
                break;
            case R.id.time_fly:
                if (isShowTime) {
                    item.setChecked(false);
                    isShowTime = false;
                    tvStopwatch.setPadding(0, 0, 0, 0);
                    editor.putBoolean(AppConstants.KEY_TIME, false);
                } else {
                    item.setChecked(true);
                    isShowTime = true;
                    tvStopwatch.setPadding(20, 0, 20, 0);
                    editor.putBoolean(AppConstants.KEY_TIME, true);
                }
                break;
            case R.id.trajectory:
                if(CannonView.isDrawTrajectory){
                    item.setChecked(false);
                    CannonView.isDrawTrajectory = false;
                    editor.putBoolean(AppConstants.KEY_TRAJECTORY, false);
                }else{
                    item.setChecked(true);
                    CannonView.isDrawTrajectory = true;
                    editor.putBoolean(AppConstants.KEY_TRAJECTORY, true);
                }
                break;
        }
        editor.commit();
        return super.onOptionsItemSelected(item);
    }
}
