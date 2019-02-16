package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Hatch {
    private static final int c_grab = 6;
    private static final int c_release = 7;
    private static final int c_extend = 1;
    private static final int c_retract = 2;

    private int state = modeStart;

    private final Solenoid solGrab = new Solenoid(c_grab);
    private final Solenoid solRelease = new Solenoid(c_release);
    private final Solenoid solExtend = new Solenoid(c_extend);
    private final Solenoid solRetract = new Solenoid(c_retract);

    public static final int modeStart = 0;
    public static final int modeGrab = 1;
    public static final int modeExtend = 2;

    public Hatch() {
        state = modeStart;
    }

    public void setMode(int mode) {
        if ((state & modeGrab) != (mode & modeGrab)) {
            Boolean setting = (mode & modeGrab) != 0;
            solGrab.set(setting);
            solRelease.set(!setting);
        }
        if ((state & modeExtend) != (mode & modeExtend)) {
            Boolean setting = (mode & modeExtend) != 0;
            solExtend.set(setting);
            solRetract.set(!setting);
        }
        state = mode;
    }
}