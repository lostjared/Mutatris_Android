package com.lostsidedead.mutatris;
import android.hardware.SensorEvent;

public interface AccelerometerListener {
	/* added SesnorEvent e */
	public void onAccelerationChanged(float x, float y, float z, SensorEvent e);
	public void onShake(float force);
}
