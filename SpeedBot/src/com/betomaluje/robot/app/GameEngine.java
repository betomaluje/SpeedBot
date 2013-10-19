package com.betomaluje.robot.app;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.betomaluje.robot.classes.Robot;

public abstract class GameEngine implements Runnable {

	public Handler mHandler = new Handler();
	public Boolean isComplete = false;

	public int largo_runnable = 60000;

	public float punch_threshold = 0.4f;

	public void run() {
		if (!isComplete) {
			initPlayer();
		} else {
			Log.v("GameEngine", "Éxito :D");
		}
	}

	public abstract void initPlayer();

	public abstract void setText(final TextView view, final String str);

	public abstract void updateHealthBar(final double current,
			final double total);

	public abstract void updateExperienceBar(final int current,
			final double total);

	public abstract void updateLoadingBar(final String text);

	public abstract int checkDamage(float punch_strength, Robot r, int enemy_id);

	public void checkLevelUp(double experience_gained, Robot player) {
		double remaining_exp = 0;

		int current_level, next_experience, current_experience;
		double total_experience = 0;

		current_level = player.getCurrent_level();
		current_experience = (int) player.getExperience();

		total_experience = current_experience + experience_gained;

		Log.e("GameEngine", experience_gained + " gained!");

		next_experience = (int) player.getNext_experience();

		if (total_experience >= next_experience) {
			Log.d("GameEngine", "level up :D!");
			// if the current experience is greater than the next experience

			current_level++;
			player.setCurrent_level(current_level);

			// remaining_exp = what i earn - amount to collect
			remaining_exp = total_experience - next_experience;

			Log.v("GameEngine", "Remaining exp: " + remaining_exp);
			Log.v("GameEngine", "Next exp: " + player.getNext_experience());
			Log.v("GameEngine", "New Level: " + current_level);

			player.setExperience(0);

			checkLevelUp((int) remaining_exp, player);

		} else {
			Log.i("GameEngine", "same level :(");

			// also store it on sqlite
			player.setExperience(total_experience);

			Log.i("GameEngine", "Rest exp: " + total_experience);
			return;
		}
	}

	public Runnable getRunnable() {
		return this;
	}

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public int getLargo_runnable() {
		return largo_runnable;
	}

	public void setLargo_runnable(int largo_runnable) {
		this.largo_runnable = largo_runnable;
	}

	public float getPunch_threshold() {
		return punch_threshold;
	}

	public void setPunch_threshold(float punch_threshold) {
		this.punch_threshold = punch_threshold;
	}
}
