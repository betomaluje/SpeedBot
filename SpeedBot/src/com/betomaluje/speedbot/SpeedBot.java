package com.betomaluje.speedbot;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.IOIOLooperProvider;
import ioio.lib.util.android.IOIOAndroidApplicationHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Application;
import android.util.Log;

import com.betomaluje.speedbot.electronics.LDR;
import com.betomaluje.speedbot.electronics.Led;
import com.betomaluje.speedbot.electronics.Motor;
import com.betomaluje.speedbot.electronics.Piezo;
import com.betomaluje.speedbot.electronics.PushButton;
import com.betomaluje.speedbot.electronics.RGBLed;
import com.betomaluje.speedbot.electronics.Servo;
import com.betomaluje.speedbot.interfaces.IOIOStatusListener;
import com.betomaluje.speedbot.interfaces.IOIOStatusListener.Status;
import com.betomaluje.speedbot.interfaces.LDRListener;
import com.betomaluje.speedbot.interfaces.LedListener;
import com.betomaluje.speedbot.interfaces.PiezoListener;
import com.betomaluje.speedbot.interfaces.PushButtonListener;

public class SpeedBot extends Application implements IOIOLooperProvider {

	private final IOIOAndroidApplicationHelper helper_ = new IOIOAndroidApplicationHelper(
			this, this);

	private int GAME_TEMPO = 1000;
	private int servo_progress = 0;

	private final String TAG = "SpeedBot";

	// ----- Listeners -----
	// for the IOIO Board status
	private IOIOStatusListener statusListener = null;

	// for the Electric Piezos
	private PiezoListener piezoListener = null;

	// for the Push Buttons
	private PushButtonListener pushButtonListener = null;

	// for the LED
	private LedListener ledListener = null;

	// for the LDR
	private LDRListener ldrListener = null;

	// the Singleton instance of the IOIOLooper class
	private IOIOLooper sharedInstance = null;

	// the Singleton instance of SpeedBot class
	public static SpeedBot speedBotInstance = null;

	private HashMap<String, Motor> _motors = null;
	private HashMap<String, Servo> _servos = null;
	private HashMap<String, Piezo> _piezos = null;
	private HashMap<String, PushButton> _pushButtons = null;
	private HashMap<String, Led> _leds = null;
	private HashMap<String, RGBLed> _rgbLeds = null;
	private HashMap<String, LDR> _ldrs = null;

	public static SpeedBot getInstance() {
		return speedBotInstance == null ? new SpeedBot() : speedBotInstance;
	}

	/**
	 * Sets the IOIOStatusListener to listen to the IOIO Board status when
	 * changed.
	 * 
	 * @param l
	 */
	public void setIOIOStatusListener(IOIOStatusListener l) {
		this.statusListener = l;
	}

	/**
	 * Creates the SpeedBot instance to start communicating with the IOIO Board.
	 * Call this method after adding all components
	 */
	public void create() {
		helper_.create();
		Log.d(TAG, "create!");
	}

	/**
	 * Destroys all communication with the IOIO Board
	 */
	public void destroy() {
		helper_.destroy();
		Log.d(TAG, "destroy!");
	}

	/**
	 * Establishes the communication with the physical IOIO Board. Call this
	 * method after calling {@link #create()}
	 */
	public void start() {
		helper_.start();
		Log.d(TAG, "start!");
	}

	/**
	 * Stops communication with the IOIO Board
	 */
	public void stop() {
		helper_.stop();
		Log.d(TAG, "stop!");
	}

	/**
	 * Restarts the communication with the IOIO Board
	 */
	public void restart() {
		helper_.restart();
		Log.d(TAG, "restart!");
	}

	// ----- MOTORS -----

	/**
	 * Adds a motor to the SpeedBot logic for future use.
	 * 
	 * @param id
	 *            : the motor's unique identifier
	 * @param pwmPin
	 *            : the PWM Pin to use
	 * @param in1Pin
	 *            : the first IN Pin to use
	 * @param in2Pin
	 *            : the second IN Pin to use
	 * @return an Motor instance of the created motor.
	 */
	public Motor addMotor(String id, int pwmPin, int in1Pin, int in2Pin) {
		if (_motors == null) {
			_motors = new HashMap<String, Motor>();
		} else {
			if (_motors.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		Motor m = new Motor();
		m.setPwmPin(pwmPin);
		m.setDigitalOutputs(in1Pin, in2Pin);

		_motors.put(id, m);

		return m;
	}

	/**
	 * Adds a motor to the SpeedBot logic for future use.
	 * 
	 * @param id
	 *            : the motor's unique identifier
	 * @param pwmPin
	 *            : the PWM Pin to use
	 * @param in1Pin
	 *            : the first IN Pin to use
	 * @param in2Pin
	 *            : the second IN Pin to use
	 * @param speed
	 *            : the speed for that motor
	 * @return an Motor instance of the created motor.
	 */
	public Motor addMotor(String id, int pwmPin, int in1Pin, int in2Pin,
			float speed) {
		if (_motors == null) {
			_motors = new HashMap<String, Motor>();
		} else {
			if (_motors.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		Motor m = new Motor();
		m.setPwmPin(pwmPin);
		m.setDigitalOutputs(in1Pin, in2Pin);
		m.setSpeed(speed);

		_motors.put(id, m);

		return m;
	}

	/**
	 * Sets the speed for a given Motor
	 * 
	 * @param id
	 *            : the motor's unique identifier
	 * @param speed
	 *            : the desired speed for that motor
	 */
	public void setMotorSpeed(String id, float speed) {
		Motor m = _motors.get(id);
		if (m != null)
			m.setSpeed(speed);
		else
			throw new NullPointerException(
					"The key: "
							+ id
							+ " isn't set. Use addMotor methods to add a motor with that id.");
	}

	public void moveMotor(String id, Motor.Direction direction) {
		Motor m = _motors.get(id);
		if (_motors != null && m != null) {
			m.setMove(true);
			if (direction == Motor.Direction.FORWARD) {
				m.setForward(true);
			} else if (direction == Motor.Direction.BACKWARD) {
				m.setForward(false);
			} else {
				m.setMove(false);
			}
		} else {
			throw new NullPointerException("You must add motor id: " + id
					+ " using addMotor first.");
		}
	}

	public void stopMotor(String id) {
		Motor m = _motors.get(id);
		if (_motors != null && m != null) {
			m.setMove(false);
		} else {
			throw new NullPointerException("You must add motor id: " + id
					+ " using addMotor first.");
		}
	}

	public void stopAllMotors() {
		for (Motor m : _motors.values()) {
			m.setMove(false);
		}
	}

	// ----- SERVOS -----

	/**
	 * Adds a servo to the SpeedBot logic for future use.
	 * 
	 * @param id
	 *            : the servo's unique identifier
	 * @param pwmPin
	 *            : the PWM Pin to use
	 * @return the Servo instance of the created servo
	 */
	public Servo addServo(String id, int pwmPin) {
		if (_servos == null) {
			_servos = new HashMap<String, Servo>();
		} else {
			if (_servos.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		Servo s = new Servo();
		s.setPwmPin(pwmPin);

		_servos.put(id, s);

		return s;
	}

	/**
	 * Moves a given servo by the given progress
	 * 
	 * @param id
	 *            : the identifier for that servo
	 * @param progress
	 *            : the progress to move
	 */
	public void moveServo(String id, int progress) {
		Servo s = _servos.get(id);
		if (_servos != null && s != null) {
			s.setMove(true);
			servo_progress = progress;
		} else {
			throw new NullPointerException("You must add servo id: " + id
					+ " using addServo first.");
		}
	}

	/**
	 * Moves a given servo by it's default progress
	 * 
	 * @param id
	 *            : the identifier for that servo
	 */
	public void moveServo(String id) {
		Servo s = _servos.get(id);
		if (_servos != null && s != null) {
			s.setMove(true);
		} else {
			throw new NullPointerException("You must add servo id: " + id
					+ " using addServo first.");
		}
	}

	// ----- ELECTRIC PIEZOS -----
	public void setPiezoListener(PiezoListener l) {
		this.piezoListener = l;
	}

	/**
	 * Adds a electric piezo to the SpeedBot logic for future use.
	 * 
	 * @param id
	 *            : the piezo's unique identifier
	 * @param aPin
	 *            : the Analog Pin to use
	 * @return the Piezo instance created
	 */
	public Piezo addPiezo(String id, int aPin) {
		if (_piezos == null) {
			_piezos = new HashMap<String, Piezo>();
		} else {
			if (_piezos.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		Piezo p = new Piezo();
		p.setaPin(aPin);

		_piezos.put(id, p);

		return p;
	}

	// ----- PUSH BUTTONS -----
	public void setPushButtonListener(PushButtonListener l) {
		this.pushButtonListener = l;
	}

	/**
	 * Adds a PushButton instance.
	 * 
	 * @param id
	 *            : the unique identifier for that push button.
	 * @param aPin
	 *            : the pin on the physical IOIO Board.
	 * @return A PushButton instance.
	 */
	public PushButton addPushButton(String id, int aPin) {
		if (_pushButtons == null) {
			_pushButtons = new HashMap<String, PushButton>();
		} else {
			if (_pushButtons.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		PushButton p = new PushButton();
		p.setPin(aPin);

		Log.v(TAG, "adding " + id + " to the _pushButtons array");

		_pushButtons.put(id, p);

		return p;
	}

	// ----- LED -----
	public void setLedListener(LedListener l) {
		this.ledListener = l;
	}

	/**
	 * Adds a Led instance.
	 * 
	 * @param id
	 *            : the unique identifier.
	 * @param aPin
	 *            : the pin on the physical IOIO Board.
	 * @return A Led instance.
	 */
	public Led addLed(String id, int aPin) {
		if (_leds == null) {
			_leds = new HashMap<String, Led>();
		} else {
			if (_leds.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		Led m = new Led();
		m.setaPin(aPin);

		_leds.put(id, m);

		return m;
	}

	/**
	 * Turns the given led status.
	 * 
	 * @param id
	 *            : the identifier for the led.
	 * @param on
	 *            : <b>true</b> to turn the led on, <b>false</b> otherwise
	 */
	public void turnLed(String id, boolean on) {
		Led led = _leds.get(id);
		if (_leds != null && led != null) {
			led.setIsON(on);
		} else {
			throw new NullPointerException("You must add led id: " + id
					+ " using addLed first.");
		}
	}

	// ----- RGB LED -----
	/**
	 * Adds a RGBLed instance.
	 * 
	 * @param id
	 *            : the unique identifier
	 * @param redPin
	 *            : the pin on the physical IOIO Board for the red value.
	 * @param greenPin
	 *            : the pin on the physical IOIO Board for the green value.
	 * @param bluePin
	 *            : the pin on the physical IOIO Board for the blue value.
	 * @return A RGLed instance.
	 */
	public RGBLed addRGBLed(String id, int redPin, int greenPin, int bluePin) {
		if (_rgbLeds == null) {
			_rgbLeds = new HashMap<String, RGBLed>();
		} else {
			if (_rgbLeds.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		RGBLed m = new RGBLed();
		m.setPins(redPin, greenPin, bluePin);

		_rgbLeds.put(id, m);

		return m;
	}

	/**
	 * Changes the RGB LED color. Values go between 0 and 255
	 * 
	 * @param id
	 *            : the identifier for that led
	 * @param red
	 *            : the amount of "red" color.
	 * @param green
	 *            : the amount of "green" color.
	 * @param blue
	 *            : the amount of "blue" color.
	 * @throws ConnectionLostException
	 */
	public void setRGBLedColors(String id, int red, int green, int blue) {
		RGBLed led = _rgbLeds.get(id);
		if (_rgbLeds != null && led != null) {
			led.setColors(red, green, blue);
		} else {
			throw new NullPointerException("You must add led id: " + id
					+ " using addRGBLed first.");
		}
	}

	// ----- LDR -----
	public void setLDRListener(LDRListener l) {
		this.ldrListener = l;
	}

	/**
	 * Adds a LDR instance.
	 * 
	 * @param id
	 *            : the unique identifier.
	 * @param pin
	 *            : the pin on the physical IOIO Board.
	 * @return A LDR instance.
	 */
	public LDR addLDR(String id, int pin) {
		if (_ldrs == null) {
			_ldrs = new HashMap<String, LDR>();
		} else {
			if (_ldrs.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		LDR m = new LDR();
		m.setPin(pin);

		_ldrs.put(id, m);

		return m;
	}

	public LDR addLDR(String id, int pin, float threshold) {
		if (_ldrs == null) {
			_ldrs = new HashMap<String, LDR>();
		} else {
			if (_ldrs.containsKey(id))
				throw new IllegalArgumentException("The key: " + id
						+ " is already in use");
		}

		LDR m = new LDR();
		m.setPin(pin);
		m.setThreshold(threshold);

		_ldrs.put(id, m);

		return m;
	}

	public void setLDRThreshold(String id, float threshold) {
		LDR ldr = _ldrs.get(id);
		if (_ldrs != null && ldr != null) {
			ldr.setThreshold(threshold);
		} else {
			throw new NullPointerException("You must add ldr id: " + id
					+ " using addLDR first.");
		}
	}

	// ----- MAIN -----
	public int getTiming() {
		return GAME_TEMPO;
	}

	/**
	 * Sets the looping timing. Default value is 1000 millisecond.
	 * 
	 * @param timing
	 *            : the time in milliseconds.
	 */
	public void setTiming(int timing) {
		GAME_TEMPO = timing;
	}

	class BattleBotLooper extends BaseIOIOLooper {

		private DigitalOutput standbyPin_;

		int stbyPin = 42;

		@Override
		protected void setup() throws ConnectionLostException {
			try {

				if (statusListener != null)
					statusListener.onStatusChanged(Status.LOADING);

				ioio_.softReset();

				standbyPin_ = ioio_.openDigitalOutput(stbyPin, true);

				if (_motors != null) {
					for (Motor m : _motors.values()) {
						m.setIOIO(ioio_);
						m.initMotor();
					}
				}

				if (_servos != null) {
					for (Servo s : _servos.values()) {
						s.setIOIO(ioio_);
						s.initServo();
					}
				}

				if (_piezos != null) {
					for (Piezo p : _piezos.values()) {
						p.setIOIO(ioio_);
						p.initPiezo();
					}
				}

				if (_pushButtons != null) {
					for (PushButton pb : _pushButtons.values()) {
						pb.setIOIO(ioio_);
						pb.initPushButton();
					}
				}

				if (_leds != null) {
					for (Led l : _leds.values()) {
						l.setIOIO(ioio_);
						l.initLed();
					}
				}

				if (_rgbLeds != null) {
					for (RGBLed rgb : _rgbLeds.values()) {
						rgb.setIOIO(ioio_);
						rgb.initRGBLed();
					}
				}

				if (_ldrs != null) {
					for (LDR ldr : _ldrs.values()) {
						ldr.setIOIO(ioio_);
						ldr.initLDR();
					}
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
				standbyPin_.write(false);
			}
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {

				if (statusListener != null)
					statusListener.onStatusChanged(Status.LOOPING);

				ioio_.beginBatch();

				// Thread.sleep(GAME_TEMPO);

				checkMotors();
				checkServos();
				checkPiezos();
				checkPushButtons();
				checkLeds();
				checkRGBLeds();
				checkLDRs();

				Thread.sleep(GAME_TEMPO);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ioio_.endBatch();
			}
		}

		@Override
		public void disconnected() {
			// TODO Auto-generated method stub
			super.disconnected();
			disconnectComponents();
			if (statusListener != null)
				statusListener.onStatusChanged(Status.DISCONNECTED);
		}

		private void disconnectComponents() {
			if (_motors != null) {
				for (Motor m : _motors.values()) {
					m.close();
				}
			}

			if (_servos != null) {
				for (Servo s : _servos.values()) {
					s.close();
				}
			}

			if (_piezos != null) {
				for (Piezo p : _piezos.values()) {
					p.close();
				}
			}

			if (_pushButtons != null) {
				for (PushButton pb : _pushButtons.values()) {
					pb.close();
				}
			}

			if (_leds != null) {
				for (Led l : _leds.values()) {
					l.close();
				}
			}

			if (_rgbLeds != null) {
				for (RGBLed rgb : _rgbLeds.values()) {
					rgb.close();
				}
			}

			if (_ldrs != null) {
				for (LDR ldr : _ldrs.values()) {
					ldr.close();
				}
			}
		}

		private void checkMotors() throws ConnectionLostException,
				InterruptedException {

			if (_motors == null)
				return;

			HashMap<String, Motor> temp = _motors;

			Iterator<Entry<String, Motor>> it = temp.entrySet().iterator();

			int total = _motors.size(), i = 0;

			while (i < total) {
				Entry<String, Motor> entry = (Entry<String, Motor>) it.next();

				Motor m = entry.getValue();
				if (!m.getMove()) {
					m.brake();
				} else {
					if (m.getForward())
						m.moveForward();
					else
						m.moveBackward();
				}

				// avoids a ConcurrentModificationException
				// it.remove();
				i++;
			}
		}

		private void checkServos() throws ConnectionLostException,
				InterruptedException {

			if (_servos == null)
				return;

			HashMap<String, Servo> temp = _servos;

			Iterator<Entry<String, Servo>> it = temp.entrySet().iterator();

			int total = _servos.size(), i = 0;

			while (i < total) {
				Entry<String, Servo> entry = (Entry<String, Servo>) it.next();

				Servo s = entry.getValue();

				if (s.getMove()) {

					if (servo_progress != 0) {
						s.moveWithProgress(servo_progress);
					} else {
						s.move();
					}

				} else {
					s.returnStart();
				}

				// avoids a ConcurrentModificationException
				// it.remove();
				i++;
			}
		}

		private void checkPiezos() throws InterruptedException,
				ConnectionLostException {

			if (_piezos == null)
				return;

			HashMap<String, Piezo> temp = _piezos;

			Iterator<Entry<String, Piezo>> it = temp.entrySet().iterator();

			int total = _piezos.size(), i = 0;

			while (i < total) {
				Entry<String, Piezo> entry = (Entry<String, Piezo>) it.next();
				Piezo p = entry.getValue();
				String id = entry.getKey();

				float damage = p.readPiezo();
				float threshold = p.getThreshold();

				if (damage >= threshold) {
					if (piezoListener != null)
						piezoListener.onRead(id, damage);
				}

				// avoids a ConcurrentModificationException
				// it.remove();
				i++;
			}
		}

		private void checkPushButtons() throws InterruptedException,
				ConnectionLostException {

			if (_pushButtons == null)
				return;

			HashMap<String, PushButton> temp = _pushButtons;

			Iterator<Entry<String, PushButton>> it = temp.entrySet().iterator();

			int total = _pushButtons.size(), i = 0;

			while (i < total) {
				Entry<String, PushButton> entry = (Entry<String, PushButton>) it
						.next();
				PushButton p = entry.getValue();
				String id = entry.getKey();

				boolean read = p.readButton();

				if (pushButtonListener != null && read)
					pushButtonListener.onPressed(id);

				// avoids a ConcurrentModificationException
				// it.remove();
				i++;
			}
		}

		private void checkLeds() throws InterruptedException,
				ConnectionLostException {

			if (_leds == null)
				return;

			HashMap<String, Led> temp = _leds;

			Iterator<Entry<String, Led>> it = temp.entrySet().iterator();

			int total = _leds.size(), i = 0;

			while (i < total) {
				Entry<String, Led> entry = (Entry<String, Led>) it.next();
				Led l = entry.getValue();
				String id = entry.getKey();

				l.toggle();

				if (ledListener != null)
					ledListener.onStatusChanged(id, l.isON());

				// avoids a ConcurrentModificationException
				// it.remove();
				i++;
			}
		}

		private void checkRGBLeds() throws InterruptedException,
				ConnectionLostException {

			if (_rgbLeds == null)
				return;

			HashMap<String, RGBLed> temp = _rgbLeds;

			Iterator<Entry<String, RGBLed>> it = temp.entrySet().iterator();

			int total = _rgbLeds.size(), i = 0;

			while (i < total) {
				Entry<String, RGBLed> entry = (Entry<String, RGBLed>) it.next();
				RGBLed l = entry.getValue();
				// String id = entry.getKey();

				l.changeColor();

				i++;
			}
		}

		private void checkLDRs() throws InterruptedException,
				ConnectionLostException {

			if (_ldrs == null)
				return;

			HashMap<String, LDR> temp = _ldrs;

			Iterator<Entry<String, LDR>> it = temp.entrySet().iterator();

			int total = _ldrs.size(), i = 0;

			while (i < total) {
				Entry<String, LDR> entry = (Entry<String, LDR>) it.next();
				LDR l = entry.getValue();
				String id = entry.getKey();

				float read = l.readValue();

				if (ldrListener != null && read != 0.0f)
					ldrListener.onRead(id, read);

				i++;
			}
		}
	}

	@Override
	public IOIOLooper createIOIOLooper(String connectionType, Object extra) {
		// TODO Auto-generated method stub
		return sharedInstance == null ? new BattleBotLooper() : sharedInstance;
	}

	public HashMap<String, Motor> getMotors() {
		return _motors;
	}

	public HashMap<String, Servo> getServos() {
		return _servos;
	}

	public HashMap<String, Piezo> getPiezos() {
		return _piezos;
	}

	public HashMap<String, PushButton> getPushButtons() {
		return _pushButtons;
	}
}
