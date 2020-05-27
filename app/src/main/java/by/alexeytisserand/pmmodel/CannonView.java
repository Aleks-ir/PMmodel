package by.alexeytisserand.pmmodel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import by.alexeytisserand.phmodels.R;


public class CannonView extends View {

    static ArrayList<Integer> arrayTrajectory_X = new ArrayList<Integer>();
    static ArrayList<Integer> arrayTrajectory_Y = new ArrayList<Integer>();
    static ArrayList<Double> array_dX = new ArrayList<Double>();
    static ArrayList<Double> array_dY = new ArrayList<Double>();
    static int current_X, current_Y, cannon_X, cannon_Y, layer_X = 0, layer_Y = 0, bg_X, bg_Y;
    static int startXcb, startYcb, startXcannon, startYcannon, startXbg, startYbg, startXtower, startYtower;
    static int dWidth, dHeight;
    static int groundLevel;
    static int startType = 0, cbFrame = 0, cbType = 0;
    static float countScrollX, startCountScrollX;
    static boolean isRun = false, isStart = true;
    static boolean isEditHeight = true, isEditAngle = true;
    static boolean isDrawTrajectory, isDrawСoordinates;
    static int num = 0;
    static int cannonFrame = 5;
    static MediaPlayer mediaPlayer;
    static SoundPool sSoundPool;

    private Handler handler;
    private Runnable runnable;
    private final int FPS = 30;
    private Bitmap background, backgroundSky, tower;
    private Bitmap[] cannonBall, cannon;
    private Display display;
    private Point point;
    private Paint paint, textPaint;
    private Rect rect;

    private boolean isChangeAngle, isChangeHeight;
    private boolean isScrollCannonFrame = false;
    private boolean isScrollX = true;
    private boolean isScrollY = true;
    private GestureDetector gestureDetector;

    private String TAG = CannonView.class.getCanonicalName();



    protected CannonView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(new GestureListener());
        initSound(context);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        backgroundSky = BitmapFactory.decodeResource(getResources(), R.drawable.background_sky);

        cannonBall = new Bitmap[8];
        cannon = new Bitmap[7];
        cannonBall[0] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_castiron_0);
        cannonBall[1] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_castiron_0);
        cannonBall[2] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_lead_0);
        cannonBall[3] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_lead_1);
        cannonBall[4] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_stone_0);
        cannonBall[5] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_stone_1);
        cannonBall[6] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_unknown_0);
        cannonBall[7] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_unknown_1);

        cannon[0] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_0);
        cannon[1] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_1);
        cannon[2] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_2);
        cannon[3] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_3);
        cannon[4] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_4);
        cannon[5] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_5);
        cannon[6] = BitmapFactory.decodeResource(getResources(), R.drawable.spr_cannon_6);

        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;

        rect = new Rect(0,0, dWidth, dHeight);
        startYbg = (int) (dHeight - background.getHeight() * 1.2);

        groundLevel = (int) (dHeight * 0.80);
        startXcb = 0;
        startYcb = groundLevel;
        startXcannon = 0;
        startYcannon = (int) (dHeight * 0.71);
        startCountScrollX = -dWidth/4;


        countScrollX = startCountScrollX;
        bg_X = startXbg;
        bg_Y = startYbg;
        current_X = startXcb;
        current_Y = startYcb;
        cannon_X = startXcannon;
        cannon_Y = startYcannon;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(30.0f);
        textPaint.setStrokeWidth(2.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundSky, null, rect, null);
        canvas.drawBitmap(background, bg_X, bg_Y, null);
        canvas.drawBitmap(cannonBall[cbFrame], current_X, current_Y, null);
        canvas.drawBitmap(cannon[cannonFrame], cannon_X, cannon_Y, null);


        if(isScrollCannonFrame) {
            cannonFrame = changeCannonFram();
        }

        if(isRun){
            if(cbFrame % 2 == 0){
                cbFrame = cbType * 2 + 1;
            }else{
                cbFrame = cbType * 2;
            }

            if (num >= array_dX.size() - 1){
                isRun = false;
                isStart = true;
            }


            if(groundLevel < current_Y && cannon_Y == startYcannon){
                stopCannonBall();
                playSound(AppConstants.KEY_SOUND_ID_FALL);
            }else if(current_X > dWidth){
                current_X -= dWidth;
                cannon_X -= dWidth;
                if(bg_X - dWidth*2 < -background.getWidth()){
                    bg_X = 0;
                }else{
                    bg_X -= dWidth;
                }
                layer_X++;
                clearArrayTrajectory();
            }
            else if(current_Y < 0 ){
                current_Y += dHeight;
                cannon_Y += dHeight;
                layer_Y++;
                bg_Y += dHeight;
                clearArrayTrajectory();
            }else if(current_Y > dHeight){
                current_Y -= dHeight;
                cannon_Y -= dHeight;
                layer_Y--;
                bg_Y -= dHeight;
                clearArrayTrajectory();
            }else{
                current_X += array_dX.get(num);
                current_Y -= array_dY.get(num);
                addArrayTrajectory();
                num++;
            }
        }

        if(isDrawTrajectory){
            drawTrajectory(canvas);
        }
        if(isDrawСoordinates){
            drawCoordinates(canvas);
        }
        handler.postDelayed(runnable,FPS);

    }

    private void initSound(Context context){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        sSoundPool.load(context, R.raw.shot, AppConstants.KEY_SOUND_ID_SHOT);
        sSoundPool.load(context, R.raw.fall,  AppConstants.KEY_SOUND_ID_FALL);
    }
    public static void playSound(int id) {
        sSoundPool.play(id, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    private void drawCoordinates(Canvas canvas){
        for(int i = 1; i <= 6; i++) {
            canvas.drawText(String.valueOf(i * 100 + layer_X * 600), dWidth / 6 * i, (float) (dHeight*0.86), textPaint);
        }
        for(int i = 0; i <= 5; i++) {
            canvas.drawText(String.valueOf(i * 50 + layer_Y * 350), 0, (float) (dHeight / 6 * (6 - i) - dHeight*0.18), textPaint);
        }
    }
    private void addArrayTrajectory(){
        if(num % 3 == 0) {
            arrayTrajectory_X.add(current_X);
            arrayTrajectory_Y.add(current_Y);
        }
    }

    public static void clearArrayTrajectory(){
        arrayTrajectory_X.clear();
        arrayTrajectory_Y.clear();
    }

    private void drawTrajectory(Canvas canvas){
        for(int i = 0; i < arrayTrajectory_X.size(); i++){
            canvas.drawCircle(arrayTrajectory_X.get(i), arrayTrajectory_Y.get(i), 3, paint);
        }
    }

    public static void stopCannonBall(){
        isRun = false;
        isStart = true;
        ToolBar.isRunningTime = false;
        ToolBar.isResetTime = true;
        ToolBar.isStop = true;
        ToolBar.pauseItem.setIcon(R.drawable.ic_stop);
        num = 0;
    }

    public static void runCannonBall(){
        if(isStart){
            new TrajectoryFly().Calculation();
            startCannonBall();
        }else{
            isRun = true;
        }
    }
    public static void startCannonBall(){
        backToStart();
        isRun = true;
        isStart = false;
        isEditHeight = false;
        isEditAngle = false;
        clearArrayTrajectory();
        playSound(AppConstants.KEY_SOUND_ID_SHOT);
    }

    public static void backToStart(){
        current_X = startXcb;
        current_Y = startYcb;
        cannon_X = startXcannon;
        cannon_Y = startYcannon;
        layer_X = 0;
        layer_Y = 0;
        bg_X = startXbg;
        bg_Y = startYbg;
    }

    public static void pauseCannonBall(){
        if(isRun) {
            isStart = false;
        }
        isRun = false;
    }
    public static void getAndSetCannonFrame(){
        double angle = TrajectoryFly.shotAngleGr;
        int frame = 0;
        double count = 80;
        while (count > angle){
            count -= 15;
            frame++;
        }
        cannonFrame = frame;
        countScrollX = dWidth/2;
        countScrollX -= dWidth/7 * frame;
    }
    private int changeCannonFram(){
        int frame = 0;
        float count = dWidth/3;
        while (count >= countScrollX){
            count -= dWidth/7;
            frame++;
        }
        return frame;
    }
    public static void getAndSetHeightTower(){
        double height = TrajectoryFly.height;
        double pm = (groundLevel - dHeight*0.3)/150;
        int velosity = current_Y - (groundLevel - (int)(height * pm));
        current_Y -= velosity;
        cannon_Y -= velosity;
        startYcb = current_Y;
        startYcannon = cannon_Y;
    }
    private void changeHeightTower(float velosityY){
        if(current_Y - velosityY <= groundLevel && current_Y - velosityY >= dHeight*0.3){
            cannon_Y -= velosityY;
            current_Y -= velosityY;
            startYcannon = cannon_Y;
            startYcb = current_Y;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP){
            if(isChangeAngle){
                saveAngle(cannonFrame);
                isChangeAngle = false;
                isScrollCannonFrame = false;
            }
            if(isChangeHeight){
                saveHeight();
                isChangeHeight = false;
            }
            isScrollX = true;
            isScrollY = true;
        }
        return true;
    }
    private void saveAngle(int frame){
        switch (frame){
            case 0:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_90;
                break;
            case 1:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_75;
                break;
            case 2:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_60;
                break;
            case 3:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_45;
                break;
            case 4:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_30;
                break;
            case 5:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_15;
                break;
            case 6:
                TrajectoryFly.shotAngleGr = AppConstants.ANGLE_0;
                break;
        }
    }
    private void saveHeight(){
        double pm = (groundLevel - dHeight*0.3)/150;
        int height = (int)(Math.abs(current_Y - groundLevel) / pm);
        TrajectoryFly.height = height;
        Log.i(TAG, String.valueOf(height));
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e1) {
            cbType = 3; //easter egg
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > AppConstants.SWIPE_MIN_DISTANCE && isScrollX && isEditAngle) {
                changeCountScrollX(velocityX);
                isChangeAngle = true;
                isScrollCannonFrame = true;
                isScrollY = false;
                return false;
            }  else if (e2.getX() - e1.getX() > AppConstants.SWIPE_MIN_DISTANCE && isScrollX && isEditAngle) {
                changeCountScrollX(velocityX);
                isChangeAngle = true;
                isScrollCannonFrame = true;
                isScrollY = false;
                return false;
            }

            if(e1.getY() - e2.getY() > AppConstants.SWIPE_MIN_DISTANCE && isScrollY && isEditHeight) {
                changeHeightTower(velocityY);
                isChangeHeight = true;
                isScrollX = false;
                return false;
            }  else if (e2.getY() - e1.getY() > AppConstants.SWIPE_MIN_DISTANCE && isScrollY && isEditHeight) {
                changeHeightTower(velocityY);
                isChangeHeight = true;
                isScrollX = false;
                return false;
            }
            return false;
        }

        private void changeCountScrollX(float velocityX){
            countScrollX += velocityX;
            if(countScrollX > dWidth/2){
                countScrollX = dWidth/2;
            }else if(countScrollX < -dWidth/2) {
                countScrollX = -dWidth/2;
            }
        }
    }
}
