package com.betomaluje.speedbot.electronics;

import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

public class RGBLed {

	private IOIO ioio_;

	private final int PWM_FREQ = 100;

	private int redPin = 0;
	private int bluePin = 0;
	private int greenPin = 0;

	private int red = 0;
	private int blue = 0;
	private int green = 0;

	private PwmOutput pwm_red, pwm_green, pwm_blue;

	public RGBLed(IOIO ioio, int rpin, int gpin, int bpin)
			throws ConnectionLostException {
		this.ioio_ = ioio;
		this.redPin = rpin;
		this.bluePin = bpin;
		this.greenPin = gpin;

		pwm_red = ioio_.openPwmOutput(redPin, PWM_FREQ);
		pwm_green = ioio_.openPwmOutput(greenPin, PWM_FREQ);
		pwm_blue = ioio_.openPwmOutput(bluePin, PWM_FREQ);
	}

	public RGBLed(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public RGBLed() {

	}

	public void setIOIO(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public void setPins(int rpin, int gpin, int bpin) {
		this.redPin = rpin;
		this.bluePin = bpin;
		this.greenPin = gpin;
	}

	public void initRGBLed() throws ConnectionLostException {
		if (this.redPin != 0 && this.greenPin != 0 && this.bluePin != 0
				&& this.ioio_ != null) {
			pwm_red = ioio_.openPwmOutput(redPin, PWM_FREQ);
			pwm_green = ioio_.openPwmOutput(greenPin, PWM_FREQ);
			pwm_blue = ioio_.openPwmOutput(bluePin, PWM_FREQ);
		} else {
			throw new NullPointerException(
					"Pin must be set and ioio instance must be created! Try using the RGBLed(IOIO) constructor");
		}
	}

	public void initRGBLed(int rpin, int gpin, int bpin)
			throws ConnectionLostException {
		if (rpin != 0 && gpin != 0 && bpin != 0 && this.ioio_ != null) {

			this.redPin = rpin;
			this.bluePin = bpin;
			this.greenPin = gpin;

			pwm_red = ioio_.openPwmOutput(redPin, PWM_FREQ);
			pwm_green = ioio_.openPwmOutput(greenPin, PWM_FREQ);
			pwm_blue = ioio_.openPwmOutput(bluePin, PWM_FREQ);
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the RGBLed(IOIO) constructor");
		}
	}

	public void setColors(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void changeColor() throws ConnectionLostException {
		pwm_red.setPulseWidth(red);
		pwm_green.setPulseWidth(green);
		pwm_blue.setPulseWidth(blue);
	}

	public void close() {
		if (this.ioio_ != null && this.pwm_red != null
				&& this.pwm_green != null && this.pwm_blue != null) {
			this.pwm_red.close();
			this.pwm_green.close();
			this.pwm_blue.close();
		} else {
			throw new NullPointerException(
					"piezo must be set and ioio instance must be created! Try using the RGBLed(IOIO) constructor");
		}
	}

}
