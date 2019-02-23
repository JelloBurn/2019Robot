package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Hatch {
    // practice bot
    private static final int c_grab = 6;
    private static final int c_release = 7;
    private static final int c_extend = 1;
    private static final int c_retract = 2;
/*
    // competition bot
    private static final int c_grab = 4;
    private static final int c_release = 3;
    private static final int c_extend = 0;
    private static final int c_retract = 7;
*/
    private int state = modeUnknown;

    private final Solenoid solGrab = new Solenoid(c_grab);
    private final Solenoid solRelease = new Solenoid(c_release);
    private final Solenoid solExtend = new Solenoid(c_extend);
    private final Solenoid solRetract = new Solenoid(c_retract);

    public static final int modeUnknown = 0;
    public static final int modeGrab = 1;
    public static final int modeRelease = -1;
    public static final int modeRetract = -1;
    public static final int modeExtend = 1;

    public Hatch() {
        state = modeUnknown;
    }

    public int getMode() {
        return state;
    }

    public int getGrab() {
        if (!solGrab.get() ^ solRelease.get()) {
            return modeUnknown;
        }
        if (solGrab.get()) {
            return modeGrab;
        } else {
            return modeRelease;
        }
    }

    public void setGrab(int mode) {
        switch (mode) {
            case modeGrab: solGrab.set(true);
                           solRelease.set(false);
                           break;
            case modeRelease: solGrab.set(false);
                              solRelease.set(true);
                              break;
            default: System.out.println("ERROR: bad Hatch.setGrab mode requested");
        }
    }

    public int getExtend() {
        if (!solExtend.get() ^ solRetract.get()) {
            return modeUnknown;
        }
        if (solExtend.get()) {
            return modeExtend;
        } else {
            return modeRetract;
        }
    }

    public void setExtend(int mode) {
        switch (mode) {
            case modeExtend: solExtend.set(true);
                             solRetract.set(false);
                             break;
            case modeRetract: solExtend.set(false);
                              solRetract.set(true);
                              break;
            default: System.out.println("ERROR: bad Hatch.setExtend mode requested");
        }
    }
}