package com.betomaluje.robot.classes;

public class Robot {

	private double total_health, current_health, experience, next_experience,
			caracter_constant;
	private float speed, currentX, currentY;
	private int id_robot, current_level, rightPunchStrength, leftPunchStrength,
			wins, losses;
	private String nombre;

	public Robot() {
	}

	public void setTotal_health(double total_health) {
		this.total_health = total_health;
	}

	public void setCurrent_health(double current_health) {
		this.current_health = current_health;
	}

	public Robot(int id_robot) {
		this.id_robot = id_robot;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getTotal_health() {
		return total_health;
	}

	public void setTotal_health(int total_health) {
		this.total_health = total_health;
	}

	public double getCurrent_health() {
		return current_health;
	}

	public void setCurrent_health(int current_health) {
		this.current_health = current_health;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public void setNewExperience(double experience) {
		float c = (float) (((this.current_health / this.total_health) * this.experience) + this.experience);
		this.experience = c;
	}

	public double getNext_experience() {
		next_experience = 0;
		int a;
		double a_2 = 0;
		for (a = 0; a <= current_level; a++) {
			a_2 = Math.pow(a, 2.0);
			next_experience += (int) (a_2 * getCaracter_constant(current_level));
		}
		return next_experience;
	}

	public void setNext_experience(double next_experience) {
		this.next_experience = next_experience;
	}

	public int getRightPunchStrength() {
		return rightPunchStrength;
	}

	public void setRightPunchStrength(int rightPunchStrength) {
		this.rightPunchStrength = rightPunchStrength;
	}

	public int getLeftPunchStrength() {
		return leftPunchStrength;
	}

	public void setLeftPunchStrength(int leftPunchStrength) {
		this.leftPunchStrength = leftPunchStrength;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getId_robot() {
		return id_robot;
	}

	public void setId_robot(int id_robot) {
		this.id_robot = id_robot;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getCurrent_level() {
		return current_level;
	}

	public void setCurrent_level(int current_level) {
		this.current_level = current_level;
	}

	public float getCurrentX() {
		return currentX;
	}

	public void setCurrentX(float currentX) {
		this.currentX = currentX;
	}

	public float getCurrentY() {
		return currentY;
	}

	public void setCurrentY(float currentY) {
		this.currentY = currentY;
	}

	public double getCaracter_constant(int current_level) {

		if (current_level > 0 && current_level < 11) {
			// between 1 and 10
			caracter_constant = 7.0;
		} else if (current_level > 10 && current_level < 21) {
			// between 11 and 20
			caracter_constant = 7.1;
		} else if (current_level > 20 && current_level < 31) {
			// between 21 and 30
			caracter_constant = 7.2;
		} else if (current_level > 30 && current_level < 41) {
			// between 31 and 40
			caracter_constant = 7.3;
		} else if (current_level > 40 && current_level < 51) {
			// between 41 and 50
			caracter_constant = 7.5;
		}

		return caracter_constant;
	}
}
