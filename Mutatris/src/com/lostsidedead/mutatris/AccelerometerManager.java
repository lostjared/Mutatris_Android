package com.lostsidedead.mutatris;

/* I found this on: http://blog.androgames.net/85/android-accelerometer-tutorial/
 * just temporary 
 */

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
 
/**
 * Android Accelerometer Sensor Manager Archetype
 * @author antoine vianey
 * under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 */


public class AccelerometerManager {
 
    private static Sensor sensor;
    private static SensorManager sensorManager;
    private static AccelerometerListener listener;
 
    private static Boolean supported;
    private static boolean running = false;
 
    public static boolean isListening() {
        return running;
    }
 
    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {}
    }
 
    public static boolean isSupported() {
        if (supported == null) {
            if (Mutatris4DroidActivity.getContext() != null) {
                sensorManager = (SensorManager) Mutatris4DroidActivity.getContext().
                        getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = sensorManager.getSensorList(
                        Sensor.TYPE_ACCELEROMETER);
                supported = new Boolean(sensors.size() > 0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }
 
  
    public static void startListening(
            AccelerometerListener accelerometerListener) {
        sensorManager = (SensorManager) Mutatris4DroidActivity.getContext().
                getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sensor = sensors.get(0);
            running = sensorManager.registerListener(
                    sensorEventListener, sensor, 
                    SensorManager.SENSOR_DELAY_GAME);
            listener = accelerometerListener;
        }
    }
 
 
    private static SensorEventListener sensorEventListener = 
        new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            listener.onAccelerationChanged(event.values[0], event.values[1], event.values[2], event);
        }
    };
 
}