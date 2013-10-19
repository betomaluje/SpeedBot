package com.betomaluje.robot.app;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.IOIOLooperProvider;
import ioio.lib.util.android.IOIOAndroidApplicationHelper;

import java.util.HashMap;

import android.app.Application;
import android.util.Log;

import com.betomaluje.robot.Motor;
import com.betomaluje.robot.Piezo;
import com.betomaluje.robot.Servo;
import com.betomaluje.robot.classes.IOIOBoardValues;
import com.betomaluje.robot.classes.Robot;

public class SpeedBot extends Application implements IOIOLooperProvider {

	private final IOIOAndroidApplicationHelper helper_ = new IOIOAndroidApplicationHelper(
			this, this);

	private String TAG = "SpeedBot";
	public Robot player;
	public Boolean IS_CONNECTED = false, IS_DAMAGED = false;
	public int selected_enemy = 0, selected_robot = 0, GAME_TEMPO = 1000, servo_progress = 0;
	public IOIOLooper sharedInstance = null;
	public static SpeedBot sharedSpeedBot = null;
	public float punch_threshold = 0.28f;

	public HashMap<String, Motor> _motores = null;
	public HashMap<String, Servo> _servos = null;
	public HashMap<String, Piezo> _piezos = null;

	public static SpeedBot getInstance() {
		if (sharedSpeedBot == null) {
			sharedSpeedBot = new SpeedBot();
		}
		return sharedSpeedBot;
	}

	public void create() {
		helper_.create();
		Log.d(TAG, "create!");
	}

	public void destroy() {
		helper_.destroy();
		Log.d(TAG, "destroy!");
	}

	public void start() {
		helper_.start();
		Log.d(TAG, "start!");
	}

	public void stop() {
		helper_.stop();
		Log.d(TAG, "stop!");
	}

	public void restart() {
		helper_.restart();
		Log.d(TAG, "restart!");
	}

	public Motor addMotor(String id, int pwmPin, int in1Pin, int in2Pin) {
		if (_motores == null) {
			_motores = new HashMap<String, Motor>();
		}

		Motor m = new Motor();
		m.setPwmPin(pwmPin);
		m.setDigitalOutputs(in1Pin, in2Pin);

		_motores.put(id, m);

		return m;
	}

	public Servo addServo(String id, int pwmPin) {
		if (_servos == null) {
			_servos = new HashMap<String, Servo>();
		}

		Servo s = new Servo();
		s.setPwmPin(pwmPin);

		_servos.put(id, s);

		return s;
	}

	public Piezo addPiezo(String id, int aPin) {
		if (_piezos == null) {
			_piezos = new HashMap<String, Piezo>();
		}

		Piezo p = new Piezo();
		p.setaPin(aPin);

		_piezos.put(id, p);

		return p;
	}

	public void setRobot(Robot r) {
		this.player = r;
	}

	public Boolean getIS_CONNECTED() {
		return IS_CONNECTED;
	}

	public Boolean getIS_DAMAGED() {
		return IS_DAMAGED;
	}

	public void moveMotor(String id, String direction) {
		Motor m = _motores.get(id);
		if (_motores != null && m != null) {
			m.setMove(true);
			if (direction.equals("f")) {
				m.setForward(true);
			} else if (direction.equals("b")) {
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
		Motor m = _motores.get(id);
		if (_motores != null && m != null) {
			m.setMove(false);
		} else {
			throw new NullPointerException("You must add motor id: " + id
					+ " using addMotor first.");
		}
	}

	public void stopAllMotors() {
		for (Motor m : _motores.values()) {
			m.setMove(false);
		}
	}

	public void moveServoWithProgress(String id, int progress) {
		Servo s = _servos.get(id);
		if (_servos != null && s != null) {
			s.setMove(true);
			servo_progress = progress;
		} else {
			throw new NullPointerException("You must add servo id: " + id
					+ " using addServo first.");
		}
	}
	
	public void moveServo(String id) {
		Servo s = _servos.get(id);
		if (_servos != null && s != null) {
			s.setMove(true);
		} else {
			throw new NullPointerException("You must add servo id: " + id
					+ " using addServo first.");
		}
	}

	public int getGAME_TEMPO() {
		return GAME_TEMPO;
	}

	public void setGAME_TEMPO(int gAME_TEMPO) {
		GAME_TEMPO = gAME_TEMPO;
	}

	class BattleBotLooper extends BaseIOIOLooper {

		private DigitalOutput standbyPin_;
		private float speed;

		@Override
		protected void setup() throws ConnectionLostException {
			try {

				ioio_.softReset();

				speed = player.getSpeed();

				standbyPin_ = ioio_.openDigitalOutput(IOIOBoardValues.STBY_PIN, true);

				for (Motor m : _motores.values()) {
					m.setIOIO(ioio_);
					m.initMotor();
				}

				for (Servo s : _servos.values()) {
					s.setIOIO(ioio_);
					s.initServo();
				}

				for (Piezo p : _piezos.values()) {
					p.setIOIO(ioio_);
					p.initPiezo();
				}

				IS_CONNECTED = true;

			} catch (NullPointerException e) {
				e.printStackTrace();
				standbyPin_.write(false);
			}
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {
				ioio_.beginBatch();

				checkMotors();
				checkServos();
				checkPiezos();

				Thread.sleep(GAME_TEMPO);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ioio_.endBatch();
			}
		}

		@Override
		public void disconnected(){
			// TODO Auto-generated method stub
			super.disconnected();
			IS_CONNECTED = false;
		}

		private void checkMotors() throws ConnectionLostException,
				InterruptedException {

			for (Motor m : _motores.values()) {
				if (!m.getMove()) {
					m.brake();
				} else {
					if (m.getForward()) {
						m.move(speed);
					} else {
						m.move(-1 * speed);
					}
				}
			}
		}

		private void checkServos() throws ConnectionLostException,
				InterruptedException {
			try {

				if (_servos.get("right").getMove()) {
					
					if(servo_progress != 0){
						_servos.get("right").moveWithProgress(servo_progress);
					} else {
						_servos.get("right").move();
					}
										
				} else {
					_servos.get("right").returnStart();
				}

				if (_servos.get("left").getMove()) {
					
					if(servo_progress != 0){
						_servos.get("left").moveWithProgress(servo_progress);
					} else {
						_servos.get("left").move();
					}					
					
				} else {
					_servos.get("left").returnStart();
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		private void checkPiezos() throws InterruptedException,
				ConnectionLostException {

			IS_DAMAGED = false;
			float damage = 0.0f;
			try {
				for (Piezo p : _piezos.values()) {

					damage = p.readPiezo();
					
					if (damage >= punch_threshold) {
						int current_health = (int) (player.getCurrent_health() - damage * 1000);
						player.setCurrent_health(current_health);
						IS_DAMAGED = true;
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IOIOLooper createIOIOLooper(String connectionType, Object extra) {
		// TODO Auto-generated method stub
		if (sharedInstance == null) {
			Log.d(TAG, "createIOIOLooper");
			sharedInstance = new BattleBotLooper();
		}

		return sharedInstance;
	}

	public Robot getRobot() {
		return player;
	}

	public HashMap<String, Motor> get_motores() {
		return _motores;
	}
	
	public HashMap<String, Servo> get_servos() {
		return _servos;
	}
	
	public HashMap<String, Piezo> get_piezos() {
		return _piezos;
	}
}
