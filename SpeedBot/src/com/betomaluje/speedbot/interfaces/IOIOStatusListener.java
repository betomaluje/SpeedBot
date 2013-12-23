package com.betomaluje.speedbot.interfaces;

/**
 * Status listener to notify when the IOIO Board changes its status.
 * 
 * @version 1
 * @author Alberto Maluje
 * 
 */
public interface IOIOStatusListener {

	public enum Status {
		LOADING, LOOPING, DISCONNECTED
	}

	/**
	 * Triggered when the IOIO Board changes its status.
	 * 
	 * @param status
	 *            : {@link Status} type. Possible statuses are: <b>LOADING</b>
	 *            when the board is on it's "setup" state, <b>LOOPING</b> when
	 *            the board is on it's "loop" state or <b>DISCONNECTED</b> when
	 *            the board is on it's "disconnected" state.
	 */
	public void onStatusChanged(Status status);

}
