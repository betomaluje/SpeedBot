SpeedBot
========

I created this library to make it easier to everyone to implement electric components such as dc motors, servos or electric piezos into the [development board IOIO](https://github.com/ytai/ioio/wiki).
It's very simple to use.

The problem was that I wanted to make cool stuff with IOIO but didn't knew the specs of the different electronic components (as example, a servomotor is pwm output type and it's controlled by it's pulsewidth).

Electronics are not very familiar for me. I passed hours learning about it so I created this micro framework to help anyone who is like me and doesn't know much of electronics.

# Usage
**First** thing you have to do is to **import the framework** to your ecplipse workspace, then **right click** -> **android** -> **check isLibrary** (below).

**Then** on the _proyect you want to use it_, just **right click** it -> **android** -> **add library (below) and select SpeedBot**.

# Example

This is an example to implement a dc motor (over a motor driver such as TB6612FNG). All this code is within your activity where you want to control your robot.

## Step 1: getting a SpeedBot Instance
```java
// make a SpeedBot instance  
private SpeedBot sb = SpeedBot.getInstance();
```

Once you have a instance, you have to implement Runnable on your Activity and add this Overriden method

```java
public void run() {
    if (sb.getIS_CONNECTED()) {
        Log.v("YOUR ACTIVITY", "Connection Established!");
    } else {
	Log.v("YOUR ACTIVITY", "Connection Lost!");
    }
}
```

When you have this, somewhere in your code (let's say on your OnCreate method), you must add the electronic components you're using. In this example:  

```java
// both motors
sb.addMotor("left", PWMA_PIN, AIN1_PIN, AIN2_PIN);
sb.addMotor("right", PWMB_PIN, BIN1_PIN, BIN2_PIN);
```
the _addMotor_ method uses this parameters:
```java
public Motor addMotor(String id, int pwmPin, int in1Pin, int in2Pin)
```
where id is the identifier you provide to SpeedBot to know which motor to move on the future, and PWMA_PIN, AIN1_PIN, AIN2_PIN, PWMB_PIN, BIN1_PIN, BIN2_PIN are int numbers according on the physical pins you put your motor driver.  

Finally, you must put this lines of code:

```java
//establishes the communication with the physical robot
sb.create();
sb.start();
```

Once you've added all your components, you can interact with them everywhere in your code. 

The methods implemented on SpeedBot to interact with dc motors are:
```java
public void moveMotor(String id, String direction)
```
where you must provide the same id used on the creation of the motor (addMotor method) and the direction has two different values: **"f"** to move **forward** and **"b"** to move **backwards**.

To stop a motor you can use 
```java
public void stopMotor(String id)
```

For example I move the motor forward and backwards using a joystick:  

```java
private JoystickMovedListener _listener = new JoystickMovedListener() {
    private int my_x, my_y;
    public void OnMoved(int x, int y) {
	my_x = x;
	my_y = -y;

        //my_y and my_x values from 0.0 to 1.0
	if (my_y > 0.6) {
            // move forward
	    sb.moveMotor("left", "f");
	    sb.moveMotor("right", "f");
	} else if (my_y < -1 * 0.6) {
	    // move backward
	    sb.moveMotor("left", "b");
	    sb.moveMotor("right", "b");
	} else if (my_x > 0.6) {
	    // turn right
	    sb.moveMotor("left", "f");
	    sb.stopMotor("right");
	} else if (my_x < -1 * 0.6) {
	    // turn left
	    sb.stopMotor("left");
	    sb.moveMotor("right", "f");
	} else {
	    sb.stopMotor("left");
	    sb.stopMotor("right");
	}
    }
 
    public void OnReleased() {
        sb.stopAllMotors();
    }

    public void OnReturnedToCenter() {
    }
};
```

The same applies to Servo class and Piezo class.

The idea is that everyone can support it and add new implementations temperature sensors, humity sensors, RGB leds, etc.

Feel free to fork it, add things, etc and... of course... use it!

I hope it helps somebody. If you have any questions, feel free to ask me.

Thanks for your time and I hope this proyect gets bigger and bigger! :D
