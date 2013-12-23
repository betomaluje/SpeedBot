package com.betomaluje.speedbot.interfaces;

public interface LDRListener {
	/**
	 * Gets the ldr/photo-resistor value.
	 * 
	 * @param id
	 *            : the identifier for that ldr.
	 * @param value
	 *            : the current value read. 1.0 is it's maximum value, 0.0 it's
	 *            minimum.
	 */
	public void onRead(String id, float value);
}
