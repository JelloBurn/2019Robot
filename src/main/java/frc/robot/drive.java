package frc.robot;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive {
    private final WPI_TalonSRX talonHL;
    private final WPI_TalonSRX talonHR;
    private final WPI_TalonSRX talonBL;
    private final WPI_TalonSRX talonBR;
    private final MecanumDrive mecanum;

    private ADXRS450_Gyro gyro;
    private int driveMode;
    private double currentX, currentY, currentR;

    // practice bot
    private static final int wheelHL = 1;
    private static final int wheelHR = 0;
    private static final int wheelBL = 2;
    private static final int wheelBR = 3;
    public static final int modeCargo = 1;
    public static final int modeHatch = -1;
/*
    // competition bot
    private static final int wheelHL = 0;
    private static final int wheelHR = 3;
    private static final int wheelBL = 2;
    private static final int wheelBR = 1;
    public static final int modeCargo = -1;
    public static final int modeHatch = 1;
    // balance parameters
    // hatch / cargo = 2.9403
    // compressor / power = 1.0833
*/
    public static final int modeField = 0;
    public static final int modeInit = modeHatch;

    private static double accelLimitX = 0.1;
    private static double accelLimitY = 0.1;
    private static double accelLimitR = 0.1;
    private static double decelFactor = 2.0;

    public Drive() {
        talonHL = new WPI_TalonSRX(wheelHL);
        talonHR = new WPI_TalonSRX(wheelHR);
        talonBL = new WPI_TalonSRX(wheelBL);
        talonBR = new WPI_TalonSRX(wheelBR);
        mecanum = new MecanumDrive(talonHL, talonBR, talonHR, talonBL);
        gyro = new ADXRS450_Gyro();
        driveMode = modeHatch;
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

    public void cartMove(double forward, double right, double cw) {
        if (driveMode != 0) {
            currentX = xferLimitAccel(currentX, driveMode * forward, accelLimitX);
            currentY = xferLimitAccel(currentY, driveMode * right, accelLimitY);
            currentR = xferLimitAccel(currentR, cw, accelLimitR);
            mecanum.driveCartesian(currentX, currentY, currentR, 0.0);
        } else {
            // curX == currentX * cos(gyro)?
            // curY == currentY * sin(gyro)?
            currentX = xferLimitAccel(currentX, forward, accelLimitX);
            currentY = xferLimitAccel(currentY, right, accelLimitY);
            currentR = xferLimitAccel(currentR, cw, accelLimitR);
            mecanum.driveCartesian(currentX, currentY, currentR, gyro.getAngle());
        }
    }

    public void setMode(int mode) {
        driveMode = mode;
    }
}