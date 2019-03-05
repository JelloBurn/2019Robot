package frc.robot;

import java.lang.Math;

import frc.robot.Drive;

import edu.wpi.first.wpilibj.Joystick;

public class Control {
    private Joystick stick = new Joystick(0);

    private int state = 0;
    private Boolean prevGrab;
    private Boolean prevExtend;
    private Boolean prevHatch;

    private static final int axisSpin = 4;
    private static final int axisSpeed = 3;
    private static final int axisRaise = 2; // raise cargo handler
    private static final int buttonDriveMode = 4;  //cargo/hatch drive mode
    private static final int buttonGrabRelease = 1;
    private static final int buttonExtend = 8; // extend/retract hatch handler 
    private static final int buttonCatch = 1; // catch cargo
    private static final int buttonThrow = 2; // throw cargo
    private static final int buttonLower = 5; // lower cargo handler

    private static final double deadbandX = 0.075;
    private static final double deadbandY = 0.075;
    private static final double deadbandR = 0.075;
    private static final double speedMin = 0.5;
    
    public static final int modeHatch = 1;
    public static final int modeGrab = 2;
    public static final int modeExtend = 4;

    public Control() {
        prevGrab = false;
        prevHatch = false;
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

    public int getDrive() {
        if ((state & modeHatch) == modeHatch) {
            return Drive.modeHatch;
        } else {
            return Drive.modeCargo;
        }
    }

    public Boolean getHatchGrab () {
        return (state & modeGrab) == modeGrab;
    }

    public void setMode(int key, Boolean value) {
        switch (key) {
            case modeHatch:
                if (value) {
                    state |= modeHatch;
                } else {
                    state &= ~modeHatch;
                }
                break;
            case modeGrab:
                if (value) {
                    state |= modeGrab;
                } else {
                    state &= ~modeGrab;
                }
                break;
            case modeExtend:
                if (value) {
                    state |= modeExtend;
                } else {
                    state &= ~modeExtend;
                }
                break;
            default:
                System.out.println("Error: bad Control mode requested");
                break;
        }
    }

    private Boolean handleButton(int mode, int button, Boolean previous) {
        Boolean current = stick.getRawButton(button);
        if (current & !previous) {
            state ^= mode;

            System.out.print("Button ");
            System.out.print(button);
            if ((state & mode) == mode) {
                System.out.println(" pressed, state now true");
            } else {
                System.out.println(" pressed, state now false");
            }
        }
        return current;
    }

    public void periodic() {
        prevHatch = handleButton(modeHatch, buttonDriveMode, prevHatch);
        prevGrab = handleButton(modeGrab, buttonGrabRelease, prevGrab);
        prevExtend = handleButton(modeExtend, buttonExtend, prevExtend);
    }
}