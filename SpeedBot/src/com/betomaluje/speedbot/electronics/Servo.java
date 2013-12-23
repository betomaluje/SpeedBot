package com.betomaluje.speedbot.electronics;

import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

public class Servo {

	private IOIO ioio_;
	private PwmOutput pwm;
	private int pwmFrequency = 50, pwmPin = 0, pulsewidth = 2000, return_start = 1000;
	private Boolean move = false;	

	public Servo(IOIO ioio, int pwmPin) throws ConnectionLostException {
		this.ioio_ = ioio;
		this.pwm = ioio_.openPwmOutput(pwmPin, pwmFrequency);
	}

	public Servo(IOIO ioio) {
		this.ioio_ = ioio;
	}
	
	public Servo() {
	
	}
	
	public void setIOIO(IOIO ioio){
		this.ioio_ = ioio;
	}

	public void setPwmPin(int pwmPin) {
		this.pwmPin = pwmPin;
	}

	public void initServo() throws ConnectionLostException {
		if (this.pwmPin != 0 && this.ioio_ != null) {
			this.pwm = ioio_.openPwmOutput(pwmPin, pwmFrequency);
		} else {
			throw new NullPointerException(
					"pwmPin must be set and ioio instance must be created! Try using the Servo(IOIO) constructor");
		}
	}

	public void initServo(int pwmPin) throws ConnectionLostException {
		if (this.ioio_ != null) {
			this.pwm = ioio_.openPwmOutput(pwmPin, pwmFrequency);
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the Servo(IOIO) constructor");
		}
	}

	public void setFrequency(int freq) {
		this.pwmFrequency = freq;
	}	
	
	public Boolean getMove() {
		return move;
	}

	public void setMove(Boolean move) {
		this.move = move;
	}

	public int getPulsewidth() {
		return pulsewidth;
	}

	public void setPulsewidth(int pulsewidth) {
		this.pulsewidth = pulsewidth;
	}

	public int getReturnStart() {
		return return_start;
	}

	public void setReturnStart(int return_start) {
		this.return_start = return_start;
	}

	public void moveWithProgress(int progress) throws ConnectionLostException{		
		this.pwm.setPulseWidth(progress);
	}
	
	public void move() throws ConnectionLostException {
		if(move){			
			move = !move;
		}
		
		this.pwm.setPulseWidth(pulsewidth);
	}
	
	public void returnStart() throws ConnectionLostException{
		this.pwm.setPulseWidth(return_start);
	}

	public void close() {
		if (this.pwm != null && this.ioio_ != null) {
			this.pwm.close();
		} else {
			throw new NullPointerException(
					"pwm must be set and ioio instance must be created! Try using the Servo(IOIO) constructor");
		}
	}

}
