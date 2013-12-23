SpeedBot
========

With SpeedBot yo just have to focus on your app cycle and not in the IOIO cycle!

I created this library to make it easier to everyone to implement electric components such as dc motors, photo-resistors or LDRs, servos or electric piezos into the [IOIO development board](https://github.com/ytai/ioio/wiki).
It's very simple to use.

The problem was that I wanted to make cool stuff with IOIO but didn't knew the specs of the different electronic components (for example, a servomotor has to be at PWM output pin type and it's controlled by it's pulsewidth).

I've spent hours learning about it so I created this library to help anyone who is like me and doesn't know much of electronics.

# Usage
**First** thing you have to do is to **import the library** to your Eclipse workspace, then **right click** -> **Android** -> **check isLibrary** (below).

**Then** on the _project you want to use it_, just **right click** it -> **Android** -> **add library (below) and select SpeedBot**.

# Example

This is an example to implement a dc motor (over a motor driver such as TB6612FNG). All this code is within your Activity or Service where you want to control your board. Also you don't have to extend IOIOActivity or IOIOService, just our regular Activity and Service.

## Step 1: getting a SpeedBot Instance
```java
// make a SpeedBot instance  
private SpeedBot sb = SpeedBot.getInstance();
```

## Step 2: setting the Timer
Once you have a instance, you can set the loop time of the board by calling:

```java
sb.setTiming(200);
```
The default time is 1000 [ms].

## Step 3: adding components
Now somewhere in your code (let's say on your OnCreate method), you must add the electronic components you're using. You can add the different components:

```java
// both motors
sb.addMotor("myMotor", 12, 13, 14);
sb.addLed("myLed", 20);
sb.addPushButton("menuButton", 22);
sb.addRGBLed("myRGBLed", 23, 24, 25);
sb.addLDR("photo-resistor", 27);
```

## Step 3.1 (optional): setting listeners
For some components, you can set a listener to know when something relevant is happening:
```java
sb.setIOIOStatusListener(ioioListener);
sb.setPushButtonListener(pushButtonListener);
sb.setLedListener(ledListener);
sb.setLDRListener(ldrListener);
```

The IOIOStatusListener helps to know when the IOIO board is connecting, when it's looping and when it's disconnected. This way if you know it's disconnected, you can change your UI accordingly:

```java
private IOIOStatusListener ioioListener = new IOIOStatusListener() {
	@Override
	public void onStatusChanged(Status status) {
		// TODO Auto-generated method stub
		if (status == Status.LOADING) {
			Log.v(TAG, "Setting up IOIO...");
		} else if (status == Status.LOOPING) {
			// do things while looping :)
			enableUI(true);
		} else if (status == Status.DISCONNECTED) {
			Log.v(TAG, "IOIO disconnected...");
			enableUI(false);
		}
	}
};
```

There's also several listeners:
PiezoListener:
```java
public void onRead(String id, float value);
```
Gives the id of the electric piezo and its value.

PushButtonListener:
```java
public void onPressed(String id);
```
Gives the id of the pressed button.

LedListener:
```java
public void onStatusChanged(String id, boolean isON);
```
Gives the identifier for that led and its status (on or off).

LDRListener:
```java
public void onRead(String id, float value);
```
Gives the identifier of the LDR and its value. 1.0 it's maximum value, 0.0 it's minimum.

## Step 4: establishing communication with the IOIO board.
Finally, you must put this lines of code to establish the communication with the IOIO board:
```java
// establishes the communication with the physical board
sb.create();
sb.start();
```
## Step 5: Interact with your robot!
Once you've added all your components, you can interact with them everywhere in your code. 

Some of the methods implemented on SpeedBot to interact with your components are:
```java
// moves "myMotor" forward. Two different values, Motor.Direction.BACKWARD and Motor.Direction.FORWARD
sb.moveMotor("myMotor", Motor.Direction.FORWARD);

// turns "myLed" ON
sb.turnLed("myLed", true);

// changes "myRGBLed" color to red
sb.setRGBLedColors("myRGBLed", 0, 0, 255);
```

Note: Remember to use the same id's to interact with your components!

You can use all of this methods whenever you like in your code.

The idea is that everyone can support it and add new implementations temperature sensors, humity sensors, RGB leds, etc.

Feel free to fork it, add things, etc and... of course... use it!

I hope it helps somebody. If you have any questions, feel free to ask me.

Thanks for your time and I hope this project gets bigger and bigger! :D
