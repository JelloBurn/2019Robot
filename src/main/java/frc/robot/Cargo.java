package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Cargo {
    private WPI_TalonSRX cargoLeft;
    private WPI_TalonSRX cargoRight;
    private WPI_VictorSPX cargoHandler;
    private Solenoid cargoHold;
    private Solenoid cargoDrop;
    private DigitalInput upperLimit;

    private int cargoTimer;
    private int handlerTimer;
    private Boolean handlerRising;

    private static final double lowerPower = -0.25;
    private static final double raisePower = 0.25;
    private static final double recvPower = 0.1;
    private static final int recvTime = 5;
    private static final double restCargoPower = 0.0;
    private static final double restHandlePower = 0.0;
    private static final double sendPower = -0.3;
    private static final int sendTime = 10;

    public static final int modeHold = 1;
    public static final int modeLower = 2;

    public Cargo() {
        cargoLeft = new WPI_TalonSRX(5);
        cargoRight = new WPI_TalonSRX(6);
        cargoHandler = new WPI_VictorSPX(1);
        cargoHold = new Solenoid(2);
        cargoDrop = new Solenoid(5);
        upperLimit = new DigitalInput(1);
        cargoTimer = 0;
        handlerTimer = 0;
        handlerRising = false;
    }

    public void cmdDown() {
        if (cargoTimer == 0) {
            cargoHandler.set(lowerPower);
//            cargoTimer = position;
            System.out.print("lowering cargo to position ");
//            System.out.println(position);
        } else {
            System.out.println("WARNING: ignoring cargoLower request");
        }
    }

    public void cmdRetract() {
//        handlerRetract = true;
        cargoHandler.set(raisePower);
        System.out.println("retracting cargo handler");
    }

    public void send() {
        if (cargoTimer == 0) {
            cargoLeft.set(sendPower);
            cargoRight.set(sendPower);
            cargoTimer = sendTime;
            System.out.println("sending cargo");
        } else {
            System.out.println("WARNING: ignoring cargoSend request");
        }
    }

    public void setMode(int mode, Boolean value) {
        switch (mode) {
            case modeHold: cargoHold.set(value);
                           cargoDrop.set(!value);
                           if (cargoTimer == 0 && value == true) {
                                cargoLeft.set(recvPower);
                                cargoRight.set(-recvPower);
                                cargoTimer = recvTime;
                                System.out.println("receiving cargo");
                            } else {
                                System.out.println("WARNING: ignoring cargoRecv request");
                            }
                            break;
            case modeLower: if (handlerTimer == 0) {
                                if (value) {
                                    cargoHandler.set(1.0);
                                    handlerTimer = 50;
                                } else {
                                    cargoHandler.set(-1.0);
                                    handlerRising = true;
                                }
                            }
                            break;
            //case modeRaise: cargoHandler.set(0.2);
            //case modeLower: ;
            default: System.out.println("ERROR: bad cargo mode requested");
                     break;
        }
    }

    public void periodic() {
        if (cargoTimer > 0) {
            cargoTimer -= 1;
            if (cargoTimer == 0) {
                cargoLeft.set(restCargoPower);
                cargoRight.set(restCargoPower);
            System.out.println("powering down cargo motors");
            }
        }
        if (handlerTimer > 0) {
            handlerTimer -= 1;
            if (handlerTimer == 0) {
                cargoHandler.set(restHandlePower);
                System.out.println("powering down handler motor");
            }
       }
       if (handlerRising & !upperLimit.get()) {
           handlerRising = false;
           cargoHandler.set(restHandlePower);
       }
    }
}