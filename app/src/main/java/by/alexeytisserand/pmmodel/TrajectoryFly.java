package by.alexeytisserand.pmmodel;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

public class TrajectoryFly extends Activity {

    private final double g = 9.81;
    private final double windage = 0.4;

    static double bodySize, bodyDensity, startSpeed, shotAngleGr, height;
    private double weight, shotAngle;
    private ArrayList<Double> arrayX = new ArrayList<Double>();
    private ArrayList<Double> arrayY = new ArrayList<Double>();
    private String TAG = CannonView.class.getCanonicalName();

    public TrajectoryFly(){}

    private double X(double time) {
        return ((startSpeed * weight * Math.cos(shotAngle)) / windage) * (1 - Math.exp(-(windage * time) / weight));
    }
    private double Y(double time) {
        return ((weight * (startSpeed * Math.sin(shotAngle) * windage + weight * g)) /
                Math.pow(windage, 2)) * (1 - Math.exp((-windage * time) / weight)) - ((weight * g * time) / windage);
    }

    private double transferRad(double angleGr){
        return angleGr * Math.PI / 180;
    }

    private double weightСalculation(double size, double density){
        return (4 / 3) * density * Math.PI * Math.pow(size, 3);
    }

    public void Calculation() {
        shotAngle = transferRad(shotAngleGr);
        weight = weightСalculation(bodySize, bodyDensity);

        double time = 1;
        double maxHeight = 0;
        double maxDistance = 0;
        double nX = X(time / 22);
        double nY = Y(time / 22);
        while (nY >= -1000) {
            if(nY >= 0){
                maxDistance = nX;
            }
            if(nY > maxHeight){
                maxHeight = nY;
            }
            time++;
            arrayX.add((X(time / 22) - nX) * 2);
            arrayY.add((Y(time / 22) - nY) * 2);
            nX = X(time / 22);
            nY = Y(time / 22);
        }
        Log.i(TAG, String.valueOf(maxHeight));
        Log.i(TAG, String.valueOf(maxDistance));
        CannonView.array_dX = arrayX;
        CannonView.array_dY = arrayY;
    }
}
