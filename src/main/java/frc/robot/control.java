package frc.robot;

import java.lang.Math;

import frc.robot.drive;

import edu.wpi.first.wpilibj.Joystick;

public class control {
    private Joystick stick;
    private drive wheels;

    Boolean prevCatchRelease;

    private static final int axisSpin = 4;
    private static final int buttonHatch = 1;
    private static final int buttonCargo = 2;
    private static final int buttonField = 3;
    private static final int buttonCatchRelease = 4;

    private static final double deadbandX = 0.075;
    private static final double deadbandY = 0.075;
    private static final double deadbandR = 0.075;
    
    public control(Joystick input, drive driveTrain) {
        stick = input;
        wheels = driveTrain;
        prevCatchRelease = false;
    }

    private double xferDeadband(double value, double width) {
        if (Math.abs(value) < width) {
            return 0.0;
        }
        return value;
    }

    public void teleOp() {
        double x = xferDeadband(stick.getX(), deadbandX);
        double y = xferDeadband(stick.getY(), deadbandY);
        double r = xferDeadband(stick.getRawAxis(axisSpin), deadbandR);
        wheels.cartMove(x, y, r);
    
        if (stick.getRawButton(buttonHatch)) {
          wheels.setMode(drive.modeHatch);
        }
    
        if (stick.getRawButton(buttonCargo)) {
          wheels.setMode(drive.modeCargo);
        }
    
        if (stick.getRawButton(buttonField)) {
          wheels.setMode(drive.modeField);
        }    

        if (stick.getRawButton(buttonCatchRelease)) {
            if (!prevCatchRelease) { // button was just pressed
                // if hatch is grabbed, release it
                // otherwise grab it
                assert false;
            }
            prevCatchRelease = true;
        } else {
            prevCatchRelease = false;
        }
    }
}