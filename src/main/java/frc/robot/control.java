package frc.robot;

import java.lang.Math;

import frc.robot.Drive;
import frc.robot.Hatch;

import edu.wpi.first.wpilibj.Joystick;

public class Control {
    private Joystick stick = new Joystick(0);

    private int modeDrive = Drive.modeInit;
    private int modeHatch = Hatch.modeUnknown;
    private Boolean prevCatchRelease;

    private static final int axisSpin = 4;
    private static final int axisSpeed = 2;
    private static final int buttonHatch = 1;
    private static final int buttonCargo = 2;
    private static final int buttonCatchRelease = 4;

    private static final double deadbandX = 0.075;
    private static final double deadbandY = 0.075;
    private static final double deadbandR = 0.075;
    private static final double speedMin = 0.5;
    
    public static final int modeHatchGrab = 1;
    public static final int modeHatchRelease = 2;

    public Control() {
        prevCatchRelease = false;
    }

    private double xferDeadband(double value, double width) {
        double speed = stick.getRawAxis(axisSpeed) * (1.0 - speedMin) + speedMin;
        if (Math.abs(value) < width) {
            return 0.0;
        }
        return value * speed;
    }

    public double getDriveX() {
        return xferDeadband(stick.getX(), deadbandX);
    }

    public double getDriveY() {
        return xferDeadband(stick.getY(), deadbandY);
    }

    public double getDriveR() {
        return xferDeadband(stick.getRawAxis(axisSpin), deadbandR);
    }

    public int getDriveMode() {
        if (stick.getRawButton(buttonHatch) && stick.getRawButton(buttonCargo)) {
            modeDrive = Drive.modeField;
        } else {
            if (stick.getRawButton(buttonHatch)) { modeDrive = Drive.modeHatch; }
            if (stick.getRawButton(buttonCargo)) { modeDrive = Drive.modeCargo; }
        }
        return modeDrive;
    }

    public int getHatchMode () {
        if (stick.getRawButton(buttonCatchRelease)) {
            if (!prevCatchRelease) { // button was just pressed
                // if hatch is grabbed, release it
                // otherwise grab it
                if (modeHatch == Hatch.modeGrab) {
                    modeHatch = Hatch.modeRelease;
                } else{
                    modeHatch = Hatch.modeGrab;
                }
            }
            prevCatchRelease = true;
        } else {
            prevCatchRelease = false;
        }
        return modeHatch;
    }

    public void setMode(int mode) {
        switch (mode) {
            case modeHatchGrab:     modeHatch = Hatch.modeGrab; break;
            case modeHatchRelease:  modeHatch = Hatch.modeRelease; break;
            default: System.out.println("Error: Setting unknown control mode"); break;
        }
    }    
}