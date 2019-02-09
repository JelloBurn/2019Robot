package frc.robot;

import java.lang.Math;

import frc.robot.Drive;
import frc.robot.Hatch;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

public class Control {
    private Joystick stick;
    private Drive wheels;
    private Hatch panel;

    private Boolean prevCatchRelease;
    private UsbCamera camHatch;
    private UsbCamera camCargo;
    private VideoSink camServer = CameraServer.getInstance().getServer();

    private static final int axisSpin = 4;
    private static final int buttonHatch = 1;
    private static final int buttonCargo = 2;
    private static final int buttonField = 3;
    private static final int buttonCatchRelease = 4;

    private static final double deadbandX = 0.075;
    private static final double deadbandY = 0.075;
    private static final double deadbandR = 0.075;
    
    public Control(Joystick input, Drive driveTrain, UsbCamera cameraHatch, UsbCamera cameraCargo, Hatch asmHatch) {
        stick = input;
        wheels = driveTrain;
        camHatch = cameraHatch;
        camCargo = cameraCargo;
        panel = asmHatch;
        prevCatchRelease = false;
        wheels.setMode(Drive.modeHatch);
        camServer.setSource(camHatch);
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
          wheels.setMode(Drive.modeHatch);
          camServer.setSource(camHatch);
        }
    
        if (stick.getRawButton(buttonCargo)) {
          wheels.setMode(Drive.modeCargo);
          camServer.setSource(camCargo);
        }
    
        if (stick.getRawButton(buttonField)) {
          wheels.setMode(Drive.modeField);
        }    

        if (stick.getRawButton(buttonCatchRelease)) {
            if (!prevCatchRelease) { // button was just pressed
                // if hatch is grabbed, release it
                // otherwise grab it
                panel.setMode(Hatch.modeSwitchGrip);
            }
            prevCatchRelease = true;
        } else {
            prevCatchRelease = false;
        }
    }
}