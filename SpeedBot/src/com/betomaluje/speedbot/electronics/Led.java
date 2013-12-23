package com.betomaluje.speedbot.electronics;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class Led {
	private IOIO ioio_;

	private DigitalOutput led;

	private int aPin = 0;

	private boolean isON = false;

	public Led(IOIO ioio, int aPin) throws ConnectionLostException {
		this.ioio_ = ioio;
		this.led = ioio_.openDigitalOutput(aPin);
	}

	public Led(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public Led() {

	}

	public void setIOIO(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public void setaPin(int aPin) {
		this.aPin = aPin;
	}

	public void initLed() throws ConnectionLostException {
		if (this.aPin != 0 && this.ioio_ != null) {
			this.led = ioio_.openDigitalOutput(aPin);
		} else {
			throw new NullPointerException(
					"aPin must be set and ioio instance must be created! Try using the Led(IOIO) constructor");
		}
	}

	public void initLed(int aPin) throws ConnectionLostException {
		if (this.ioio_ != null) {
			this.led = ioio_.openDigitalOutput(aPin);
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the Led(IOIO) constructor");
		}
	}

	public void toggle() throws ConnectionLostException {
		this.led.write(isON);
	}

	public void close() {
		if (this.ioio_ != null && this.led != null) {
			this.led.close();
		} else {
			throw new NullPointerException(
					"piezo must be set and ioio instance must be created! Try using the Led(IOIO) constructor");
		}
	}

	public boolean isON() {
		return this.isON;
	}

	public void setIsON(boolean on) {
		this.isON = on;
	}
}
