package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class drive {
    private static final int wheelHL = 1;
    private static final int wheelHR = 0;
    private static final int wheelBL = 2;
    private static final int wheelBR = 3;

    private final WPI_TalonSRX talonHL;
    private final WPI_TalonSRX talonHR;
    private final WPI_TalonSRX talonBL;
    private final WPI_TalonSRX talonBR;
    private final MecanumDrive mecanum;
    private final ADXRS450_Gyro gyro;

    private int driveMode;

    public static final int modeCargo = 1;
    public static final int modeHatch = -1;
    public static final int modeField = 0;

    public drive() {
        talonHL = new WPI_TalonSRX(wheelHL);
        talonHR = new WPI_TalonSRX(wheelHR);
        talonBL = new WPI_TalonSRX(wheelBL);
        talonBR = new WPI_TalonSRX(wheelBR);
        mecanum = new MecanumDrive(talonHL, talonBR, talonHR, talonBL);
        gyro = new ADXRS450_Gyro();
        driveMode = modeHatch;
    }

    public void cartMove(double forward, double right, double cw) {
        if (driveMode != 0) {
            mecanum.driveCartesian(driveMode * forward,
                                   driveMode * right,
                                   cw,
                                   0.0);
        } else {
            mecanum.driveCartesian(forward,
                                   right,
                                   cw,
                                   gyro.getAngle());
        }
    }

    public void setMode(int mode) {
        driveMode = mode;
    }
}