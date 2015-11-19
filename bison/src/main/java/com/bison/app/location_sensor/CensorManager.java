package com.bison.app.location_sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 重力重应器的监听
 * 
 * @author boom
 * 
 */
public class CensorManager implements SensorEventListener {

	// 定义速度值与两次摇晃的时间间

	private int SPEED_THRESHOLD = 3000;// 速度
	private int INTERVAL_TIME =100; // 时间间隔

	// 定义传感器与传感器管理器

	private Sensor sensor;
	private SensorManager mSensorManager;

	// 定义重力感应监听

	private OnShakedListener onShakedListener;

	// 定义应用上下文对
	private Context mContext;

	// 定义手机上一个位置时重力感应坐标
	private float lastx, lasty, lastz;

	private long lastupdatetime;

	public CensorManager(Activity context) {
		this(context, 2000, 100);
		
	}
	public CensorManager(Activity activity, int speed, int time){
		this.mContext = activity;
		this.SPEED_THRESHOLD = speed;
		this.INTERVAL_TIME = time;
		init();
	}

	/**
	 * 初始
	 */
	private void init() {
		mSensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {

			sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}

		if (sensor != null) {

			mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);

		}

	}

	// 停止监测
	public void stop() {

		mSensorManager.unregisterListener(this);
	}

	/**
	 * 重力感应接口
	 * 
	 * @author boom
	 * 
	 */
	public interface OnShakedListener {

		public void shake();
	}

	// 设置重力感应监听
	public void setonShakedListener(OnShakedListener onShakedListener) {

		this.onShakedListener = onShakedListener;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	// 重力感应器感应获取变化数
	@Override
	public void onSensorChanged(SensorEvent event) {

		// 测现在的时间

		long currenttime = System.currentTimeMillis();

		// 判断是否时间间隔达到了最小�??

		long timeInterval = currenttime - lastupdatetime;

		if (timeInterval < INTERVAL_TIME) {

			return;
		}
		// 若达到了，将本次时间改为上次
		lastupdatetime = currenttime;

		// 获取本次手机重力感应坐标
		float curx = event.values[0];
		float cury = event.values[1];
		float curz = event.values[2];

		// 获取前后两次坐标的变化差
		float deltaX = curx - lastx;
		float deltaY = cury - lasty;
		float deltaZ = curz - lastz;

		// 将现在的坐标转换为上次

		lastx = curx;
		lasty = cury;
		lastz = curz;

		// 计算速度，判断是否达到了速度临界
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;

		// 如果达到了临界速度值，执行动作

		if (speed >= SPEED_THRESHOLD) {
			onShakedListener.shake();
		}

	}

}
