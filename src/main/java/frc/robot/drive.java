package frc.robot;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive {
    private WPI_TalonSRX talonHL;
    private WPI_TalonSRX talonHR;
    private WPI_TalonSRX talonBL;
    private WPI_TalonSRX talonBR;
    private MecanumDrive mecanum;

    private int driveMode;
    private double currentX, currentY, currentR;

    public static final int configPractice = 0;
    public static final int configCompetition = 1;
    public static final int modeHatch = 0;
    public static final int modeCargo = 1;

    private static double accelLimitX = 0.1;
    private static double accelLimitY = 0.1;
    private static double accelLimitR = 0.1;
    private static double decelFactor = 2.0;
    private static int hatchFactor = 0;
    private static int cargoFactor = 0;
    private static final int unknownFactor = 0;

    public Drive(int config, int mode) {
        switch (config) {
            case configPractice:
                talonHL = new WPI_TalonSRX(1);
                talonHR = new WPI_TalonSRX(0);
                talonBL = new WPI_TalonSRX(2);
                talonBR = new WPI_TalonSRX(3);
                cargoFactor = 1;
                hatchFactor = -1;
                break;
            case configCompetition:
                talonHL = new WPI_TalonSRX(4);
                talonHR = new WPI_TalonSRX(3);
                talonBL = new WPI_TalonSRX(2);
                talonBR = new WPI_TalonSRX(1);
                cargoFactor = -1;
                hatchFactor = 1;
                break;
            default:
                System.out.println("ERROR: bad Drive configuration requested");
                break;
        }
        setMode(mode);
        mecanum = new MecanumDrive(talonHL, talonBR, talonHR, talonBL);
    }

    private double xferLimitAccel(double current, double desired, double limit) {
        double delta = desired - current;
        if (delta * current < 0) {  // braking is faster than accelerating for safety reasons
            limit *= decelFactor;
        }
        if (Math.abs(delta) > limit) {
            if (delta < 0) {
                delta = -limit;
            } else {
                delta = limit;
            }
        }
        return current + delta;
    }

    public void move(double forward, double right, double cw) {
        if (driveMode != unknownFactor) {
            currentX = xferLimitAccel(currentX, driveMode * forward, accelLimitX);
            currentY = xferLimitAccel(currentY, driveMode * right, accelLimitY);
            currentR = xferLimitAccel(currentR, cw, accelLimitR);
            mecanum.driveCartesian(currentX, currentY, currentR, 0.0);
        }
    }

    public void setMode(int mode) {
        switch (mode) {
            case modeCargo:
                driveMode = cargoFactor;
                break;
            case modeHatch:
                driveMode = hatchFactor;
                break;
            default:
                System.out.println("ERROR: bad Drive mode requested");
                driveMode = unknownFactor;
                break;
        }
    }
}