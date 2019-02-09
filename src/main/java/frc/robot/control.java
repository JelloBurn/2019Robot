package frc.robot;

import java.lang.Math;

import frc.robot.Drive;
import frc.robot.Hatch;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

public class Control {
    private Joystick stick = new Joystick(0);

    private int modeDrive = Drive.modeStart;
    private int modeHatch = Hatch.modeStart;
    private Boolean prevCatchRelease;
    private UsbCamera camHatch;
    private UsbCamera camCargo;
    private VideoSink camServer = CameraServer.getInstance().getServer();

    private static final int axisSpin = 4;
    private static final int buttonHatch = 1;
    private static final int buttonCargo = 2;
    private static final int buttonCatchRelease = 4;

    private static final double deadbandX = 0.075;
    private static final double deadbandY = 0.075;
    private static final double deadbandR = 0.075;
    
    public static final int modeHatchGrab = 1;
    public static final int modeHatchRelease = 2;

    public Control(Joystick input, UsbCamera cameraHatch, UsbCamera cameraCargo) {
        stick = input;
        camHatch = cameraHatch;
        camCargo = cameraCargo;
        prevCatchRelease = false;
        camServer.setSource(camHatch);
    }

    private double xferDeadband(double value, double width) {
        if (Math.abs(value) < width) {
            return 0.0;
        }
        return value;
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
                modeHatch ^= Hatch.modeGrab;
            }
            prevCatchRelease = true;
        } else {
            prevCatchRelease = false;
        }
        return modeHatch;
    }

    public void teleOp() {
        if (stick.getRawButton(buttonHatch)) {
          camServer.setSource(camHatch);
        }
    
        if (stick.getRawButton(buttonCargo)) {
          camServer.setSource(camCargo);
        }
    
    }

    public void setMode(int mode) {
        switch (mode) {
            case modeHatchGrab:     modeHatch |= ~Hatch.modeGrab; break;
            case modeHatchRelease:  modeHatch &= ~Hatch.modeGrab; break;
            default: System.out.println("Error: Setting unknown control mode"); break;
        }
    }    
}