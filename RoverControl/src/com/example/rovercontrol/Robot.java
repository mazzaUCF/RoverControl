package com.example.rovercontrol;

import ioio.lib.api.IOIO;

import com.example.rovercontrol.control.StateMachine;
import com.example.rovercontrol.io.GrabberPiston;
import com.example.rovercontrol.io.IRSensor;
import com.example.rovercontrol.io.RobotMotion;
import com.example.rovercontrol.io.RobotOrientation;
import com.example.rovercontrol.io.UDPClient;

public class Robot {
	public RobotMotion motion;
	public IRSensor irSensor;
	public GrabberPiston grabber;
	public StateMachine<Robot> stateMachine;
	private long _lastNanoTime;
	private final int PISTON_PIN = 12;
	private final int IR_PIN = 40;
	public RobotOrientation orientation;
	public UDPClient udpClient;
	private final int UDP_PORT = 8888;
	private final String HOST_NAME = "localhost";
	//private final int _TX_PIN = 14;

	
	public Robot(RobotMotion motion_, RobotOrientation orientation_) {
		udpClient = new UDPClient(UDP_PORT, HOST_NAME);
		irSensor = new IRSensor(IR_PIN);
		grabber = new GrabberPiston(PISTON_PIN);
		motion = motion_;
		stateMachine = new StateMachine<Robot>(this);
		orientation = orientation_;
		_looper = new _RobotLooper();
	}
	
	public void resetHardware(IOIO ioio) {
		irSensor.reset(ioio);
	}
	
	public void update() {
		long currentTime = System.nanoTime();
		stateMachine.update(currentTime - _lastNanoTime);
		_lastNanoTime = currentTime;
	}
	
	public void start() {
		new Thread(_looper).start();
	}
	
	private class _RobotLooper implements Runnable {

		@Override
		public void run() {
			while(true) {
				update();
			}
		}
	}
	_RobotLooper _looper;
}
