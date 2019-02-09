package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Hatch {
    private static final int c_grab = 6;
    private static final int c_release = 7;

    private int stateGrip = modeNone;
//    private int statePosition;

    private final Solenoid solGrab = new Solenoid(c_grab);
    private final Solenoid solRelease = new Solenoid(c_release);

    private static final int modeNone = 0;
    public static final int modeGrab = 1;
    public static final int modeRelease = 2;
    public static final int modeExtend = 3;
    public static final int modeRetract = 4;
    public static final int modeSwitchGrip = 5;

    public Hatch() {
        stateGrip = modeNone;
        release();
//        statePosition = modeRetract;
    }

    private void grab() {
        if (stateGrip != modeGrab) {
            solGrab.set(true);
            solRelease.set(false);
            stateGrip = modeGrab;    
        }
    }

    private void release() {
        if (stateGrip != modeRelease) {
            solGrab.set(false);
            solRelease.set(true);
            stateGrip = modeRelease;
        }
    }

    public void setMode(int mode) {
        switch (mode) {
            case modeGrab:       grab();       break;
            case modeRelease:    release();    break;
            case modeSwitchGrip: switchGrip(); break;
            default: assert false;
        } 
    }

    public void switchGrip() {
        if (stateGrip == modeRelease) {
            grab();
        } else {
            release();
        }
    }
}