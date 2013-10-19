package com.betomaluje.robot;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class Piezo {
	
	private IOIO ioio_;
	
	private AnalogInput piezo;
	private int aPin = 0;
	
	public Piezo(IOIO ioio, int aPin) throws ConnectionLostException{
		this.ioio_ = ioio;
		this.piezo = ioio_.openAnalogInput(aPin);
	}
	
	public Piezo(IOIO ioio){
		this.ioio_ = ioio;
	}
	
	public Piezo(){
		
	}
	
	public void setIOIO(IOIO ioio){
		this.ioio_ = ioio;
	}

	public void setaPin(int aPin) {
		this.aPin = aPin;
	}
	
	public void initPiezo() throws ConnectionLostException{
		if (this.aPin != 0 && this.ioio_ != null) {
			this.piezo = ioio_.openAnalogInput(aPin);
		} else {
			throw new NullPointerException(
					"aPin must be set and ioio instance must be created! Try using the Piezo(IOIO) constructor"); 
		}
	}
	
	public void initPiezo(int aPin) throws ConnectionLostException{
		if (this.ioio_ != null) {
			this.piezo = ioio_.openAnalogInput(aPin);
		} else {
			throw new NullPointerException(
					"ioio instance must be created! Try using the Piezo(IOIO) constructor"); 
		}
	}
	
	public float readPiezo() throws InterruptedException, ConnectionLostException{		
		return this.piezo.read();
	}
	
	public void close(){
		if (this.ioio_ != null && this.piezo != null) {
			this.piezo.close();
		} else {
			throw new NullPointerException(
					"piezo must be set and ioio instance must be created! Try using the Piezo(IOIO) constructor"); 
		}		
	}
}