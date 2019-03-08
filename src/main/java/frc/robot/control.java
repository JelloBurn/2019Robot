package frc.robot;

import java.lang.Math;

import edu.wpi.first.wpilibj.Joystick;

public class Control {
    private Joystick stick = new Joystick(0);

    private int state = 0;
    private Boolean prevGrab;
    private Boolean prevExtend;
    private Boolean prevHatch;

    private static final int axisSpin = 4;
    private static final int axisSpeed = 3;
//    private static final int axisRaise = 2; // raise cargo handler
    private static final int buttonDrive = 1;  //cargo/hatch drive mode
    private static final int buttonGrab = 4; // grab/release hatch cover
    private static final int buttonExtend = 8; // extend/retract hatch handler 
//    private static final int buttonCatch = 1; // catch cargo
//    private static final int buttonThrow = 2; // throw cargo
//    private static final int buttonLower = 5; // lower cargo handler

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
        prevExtend = false;
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

    
    public Boolean getMode(int key) {
        switch (key) {
            case modeHatch:
                return (state & modeHatch) == modeHatch;
            case modeGrab:
                return (state & modeGrab) == modeGrab;
            case modeExtend:
                return (state & modeExtend) == modeExtend;
            default:
                System.out.println("ERROR: get bad Control mode");
                return false;
        }
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
                System.out.println("Error: set bad Control mode");
                break;
        }
    }

    private Boolean handleButton(int mode, int button, Boolean previous, String nameTrue, String nameFalse) {
        Boolean current = stick.getRawButton(button);
        if (current & !previous) {
            state ^= mode;

            System.out.print("Button ");
            System.out.print(button);
            System.out.print(" pressed, state now ");
            if ((state & mode) == mode) {
                System.out.println(nameTrue);
            } else {
                System.out.println(nameFalse);
            }
        }
        return current;
    }

    public void periodic() {
        prevHatch = handleButton(modeHatch, buttonDrive, prevHatch, "hatchDriving", "cargoDriving");
        if ((state & modeHatch) == modeHatch) {
            // hatch mode controls
            prevGrab = handleButton(modeGrab, buttonGrab, prevGrab, "hatchGrabbed", "hatchReleased");
            prevExtend = handleButton(modeExtend, buttonExtend, prevExtend, "hatchExtended", "hatchRetracted");
        } else {
            // cargo mode controls
        }
    }
}