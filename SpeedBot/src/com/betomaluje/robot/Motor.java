package com.betomaluje.robot;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

public class Motor {

	private IOIO ioio_;

	private PwmOutput pwm;
	private DigitalOutput in1, in2;
	private int pwmPin = 0, in1Pin = 0, in2Pin = 0;
	private Boolean move = false, forward = true;

	private int pwmFrequency = 100000;

	public Motor(IOIO ioio, int pwmPin, int in1Pin, int in2Pin)
			throws ConnectionLostException {
		this.ioio_ = ioio;
		this.pwm = ioio_.openPwmOutput(pwmPin, pwmFrequency);

		this.in1 = ioio_.openDigitalOutput(in1Pin, false);
		this.in2 = ioio_.openDigitalOutput(in2Pin, false);
		
		brake();
	}

	public Motor(IOIO ioio) throws ConnectionLostException {
		ioio_ = ioio;
	}
	
	public Motor(){
		
	}

	public void setIOIO(IOIO ioio){
		this.ioio_ = ioio;
	}
	
	public void setPwmPin(int pwmPin) {
		this.pwmPin = pwmPin;
	}

	public void setDigitalOutputs(int in1Pin, int in2Pin) {
		this.in1Pin = in1Pin;
		this.in2Pin = in2Pin;
	}

	public void initMotor() throws ConnectionLostException {
		if (this.pwmPin != 0 && this.ioio_ != null && this.in1Pin != 0
				&& this.in2Pin != 0) {
			this.pwm = ioio_.openPwmOutput(pwmPin, pwmFrequency);
			this.in1 = ioio_.openDigitalOutput(in1Pin, false);
			this.in2 = ioio_.openDigitalOutput(in2Pin, false);
			
			brake();
		} else {
			throw new NullPointerException(
					"pwmPin, in1Pin and in2Pin must be set and ioio instance must be created! Try using the Motor(IOIO) constructor");
		}
	}

	public void initMotor(int pwmPin, int in1Pin, int in2Pin)
			throws ConnectionLostException {
		if (this.ioio_ != null) {
			this.pwm = ioio_.openPwmOutput(pwmPin, pwmFrequency);
			this.in1 = ioio_.openDigitalOutput(in1Pin, false);
			this.in2 = ioio_.openDigitalOutput(in2Pin, false);
			brake();
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the Motor(IOIO) constructor");
		}
	}

	public void setFrequency(int freq) {
		this.pwmFrequency = freq;
	}

	public void move(float speed) throws ConnectionLostException {		
		if (speed > 0) {			
			moveForward(speed);
		} else if (speed < 0) {
			moveBackward(-1 * speed);
		} else {
			brake();
		}
	}	

	public void moveForward(float speed) throws ConnectionLostException {
		move = true;
		forward = true;
		pwm.setDutyCycle(constrainSpeed(speed));
		in1.write(true);
		in2.write(false);
	}

	public void moveBackward(float speed) throws ConnectionLostException {
		move = true;
		forward = false;
		pwm.setDutyCycle(constrainSpeed(speed));
		in1.write(false);
		in2.write(true);
	}

	public void brake() throws ConnectionLostException {
		move = false;
		pwm.setDutyCycle(0);
		in1.write(false);
		in2.write(false);
	}
	
	public Boolean getMove() {
		return move;
	}

	public void setMove(Boolean move) {
		this.move = move;
	}
	
	public Boolean getForward() {
		return forward;
	}

	public void setForward(Boolean forward) {
		this.forward = forward;
	}

	private float constrainSpeed(float speed) {
		if (speed < 0) {
			return (float) 0.0;
		} else if (speed > 1.0) {
			return (float) 1.0;
		} else {
			return (float) Math.abs(speed);
		}
	}

	public void close() {
		if (this.pwm != null && this.ioio_ != null && this.in1 != null
				&& this.in2 != null) {
			this.pwm.close();
			this.in1.close();
			this.in2.close();
		} else {
			throw new NullPointerException(
					"pwm, in1 and in2 must be set and ioio instance must be created! Try using the Motor(IOIO) constructor");
		}
	}
	
	public Boolean isNull(){
		if(this == null){
			return true;
		} else {
			return false;
		}
	}		
}
