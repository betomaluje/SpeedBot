package com.betomaluje.robot.joystick;

public class JoystickSensor {

	private int my_x, my_y;

	public JoystickSensor() {
		super();
	}

	public JoystickMovedListener getListener() {
		return _listener;
	}

	private JoystickMovedListener _listener = new JoystickMovedListener() {

		public void OnMoved(int x, int y) {
			my_x = x;
			my_y = -y;
		}

		public void OnReleased() {
		}

		public void OnReturnedToCenter() {
		}
	};

	public int getX() {
		// TODO Auto-generated method stub
		return my_x;
	}

	public int getY() {
		// TODO Auto-generated method stub
		return my_y;
	};

}
