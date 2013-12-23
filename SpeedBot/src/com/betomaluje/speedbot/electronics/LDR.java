package com.betomaluje.speedbot.electronics;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class LDR {

	private IOIO ioio_;

	private AnalogInput ldr;

	private float threshold = 0.0f;

	private int pin = 0;

	public LDR(IOIO ioio, int Pin) throws ConnectionLostException {
		this.ioio_ = ioio;
		this.ldr = ioio_.openAnalogInput(Pin);
	}

	public LDR(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public LDR() {

	}

	public void setIOIO(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public void setPin(int Pin) {
		this.pin = Pin;
	}

	/**
	 * Sets the minimum value to start reading.
	 * 
	 * @param t
	 *            : the threshold.
	 */
	public void setThreshold(float t) {
		this.threshold = t;
	}

	public void initLDR() throws ConnectionLostException {
		if (this.pin != 0 && this.ioio_ != null) {
			this.ldr = ioio_.openAnalogInput(pin);
		} else {
			throw new NullPointerException(
					"Pin must be set and ioio instance must be created! Try using the LDR(IOIO) constructor");
		}
	}

	public void initLDR(int Pin) throws ConnectionLostException {
		if (this.ioio_ != null) {
			this.ldr = ioio_.openAnalogInput(pin);
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the LDR(IOIO) constructor");
		}
	}

	/**
	 * Gets the read value.
	 * 
	 * @return The value returned by the digital input.
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float readValue() throws InterruptedException,
			ConnectionLostException {

		float lecture = this.ldr.read();

		if (lecture <= threshold)
			return this.ldr.read();
		else
			return 0.0f;
	}

	public void close() {
		if (this.ioio_ != null && this.ldr != null) {
			this.ldr.close();
		} else {
			throw new NullPointerException(
					"piezo must be set and ioio instance must be created! Try using the LDR(IOIO) constructor");
		}
	}

}
