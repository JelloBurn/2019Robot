package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Hatch {
    private Solenoid solGrab = null;
    private Solenoid solRelease = null;
    private Solenoid solExtend = null;
    private Solenoid solRetract = null;

    public static final int configPractice = 0;
    public static final int configCompetition = 1;
    public static final int modeUnknown = 0;
    public static final int modeGrab = 1;
    public static final int modeExtend = 2;

    public Hatch(int config) {
        switch (config) {
            case configPractice:
                solGrab = new Solenoid(6);
                solRelease = new Solenoid(7);
                solExtend = new Solenoid(1);
                solRetract = new Solenoid(2);
                break;
            case configCompetition:
                solGrab = new Solenoid(4);
                solRelease = new Solenoid(3);
                solExtend = new Solenoid(0);
                solRetract = new Solenoid(7);
                break;
            default:
                System.out.println("ERROR: bad Hatch configuration requested");
                break;
        }
    }

    public void cmdGrab(Boolean position) {
        solGrab.set(position);
        solRelease.set(!position);
    }

    public void cmdExtend(Boolean position) {
        solExtend.set(position);
        solRetract.set(!position);
    }
}