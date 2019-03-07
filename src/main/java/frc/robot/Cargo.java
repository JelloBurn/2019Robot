package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;

public class Cargo {
    private WPI_TalonSRX cargoLeft;
    private WPI_TalonSRX cargoRight;
    private WPI_VictorSPX cargoHandler;
    private Solenoid cargoHold;
    private Solenoid cargoDrop;
    private int timer;

    private static final double lowerPower = -0.25;
    private static final double raisePower = 0.25;
    private static final double recvPower = 0.5;
    private static final int recvTime = 5;
    private static final double restCargoPower = 0.0;
    private static final double restHandlePower = 0.0;
    private static final double sendPower = -1.0;
    private static final int sendTime = 10;

    public static final int modeHold = 1;

    public Cargo() {
        cargoLeft = new WPI_TalonSRX(4);
        cargoRight = new WPI_TalonSRX(5);
        cargoHandler = new WPI_VictorSPX(6);
        cargoHold = new Solenoid(2);
        cargoDrop = new Solenoid(5);
        timer = 0;
    }

    public void lower(int position) {
        if (timer == 0) {
            cargoHandler.set(lowerPower);
            timer = position;
            System.out.print("lowering cargo to position ");
            System.out.println(position);
        } else {
            System.out.println("WARNING: ignoring cargoLower request");
        }
    }

    public void raise(int position) {
        if (timer == 0) {
            cargoHandler.set(raisePower);
            timer = position;
            System.out.print("raising cargo to position ");
            System.out.println(position);
        } else {
            System.out.println("WARNING: ignoring cargoRaise request");
        }
    }

    public void recv() {
        if (timer == 0) {
            cargoLeft.set(recvPower);
            cargoRight.set(recvPower);
            timer = recvTime;
            System.out.println("receiving cargo");
        } else {
            System.out.println("WARNING: ignoring cargoRecv request");
        }
    }

    public void send() {
        if (timer == 0) {
            cargoLeft.set(sendPower);
            cargoRight.set(sendPower);
            timer = sendTime;
            System.out.println("sending cargo");
        } else {
            System.out.println("WARNING: ignoring cargoSend request");
        }
    }

    public void setMode(int mode, Boolean value) {
        switch (mode) {
            case modeHold: cargoHold.set(value);
                           cargoDrop.set(!value);
                           if (timer == 0 && value == true) {
                                cargoLeft.set(recvPower);
                                cargoRight.set(recvPower);
                                timer = recvTime;
                                System.out.println("receiving cargo");
                            } else {
                                System.out.println("WARNING: ignoring cargoRecv request");
                            }
                            break;
            default: System.out.println("ERROR: bad cargo mode requested");
                     break;
        }
    }

    public void periodic() {
        if (timer > 0) {
            timer -= 1;
            if (timer == 0) {
                cargoLeft.set(restCargoPower);
                cargoRight.set(restCargoPower);
                cargoHandler.set(restHandlePower);
                System.out.println("powering down cargo motors");
            }
        }
    }
}