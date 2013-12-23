package com.betomaluje.speedbot.electronics;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class PushButton {

	private IOIO ioio_;

	private DigitalInput pushButton;

	private int pin = 0;

	public PushButton(IOIO ioio, int Pin) throws ConnectionLostException {
		this.ioio_ = ioio;
		this.pushButton = ioio_.openDigitalInput(Pin);
	}

	public PushButton(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public PushButton() {

	}

	public void setIOIO(IOIO ioio) {
		this.ioio_ = ioio;
	}

	public void setPin(int Pin) {
		this.pin = Pin;
	}

	public void initPushButton() throws ConnectionLostException {
		if (this.pin != 0 && this.ioio_ != null) {
			this.pushButton = ioio_.openDigitalInput(pin);
		} else {
			throw new NullPointerException(
					"Pin must be set and ioio instance must be created! Try using the PushButton(IOIO) constructor");
		}
	}

	public void initPushButton(int Pin) throws ConnectionLostException {
		if (this.ioio_ != null) {
			this.pushButton = ioio_.openDigitalInput(Pin);
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the PushButton(IOIO) constructor");
		}
	}

	/**
	 * Gets the read value.
	 * 
	 * @return The value returned by the digital input.
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public boolean readButton() throws InterruptedException,
			ConnectionLostException {
		return this.pushButton.read();
	}

	public void close() {
		if (this.ioio_ != null && this.pushButton != null) {
			this.pushButton.close();
		} else {
			throw new NullPointerException(
					"piezo must be set and ioio instance must be created! Try using the PushButton(IOIO) constructor");
		}
	}

}
