/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import frc.robot.Cargo;
import frc.robot.Drive;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final Control m_control = new Control();
  private final Drive m_drive = new Drive(Drive.configCompetition, true);
  private final Hatch m_hatch = new Hatch(Hatch.configCompetition);
  private final UsbCamera m_cameraField = CameraServer.getInstance().startAutomaticCapture(0);
  private final UsbCamera m_cameraHatch = CameraServer.getInstance().startAutomaticCapture(1);
  private VideoSink m_cameraServer = CameraServer.getInstance().getServer();
  private int timerHatchExtend = 0;

  private final static int timeExtend = 200;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("Starting robotInit() method.");
    m_control.setHatchReleased();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
    System.out.println("Starting disabledInit() method.");
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    System.out.println("Starting autonomousInit() method.");
    m_hatch.cmdGrab(true);
    m_hatch.cmdExtend(false);
//    m_cargo.setMode(Cargo.modeHold, true);
    m_control.setHatchGrabbed();
    m_control.setHatchMode();
    m_drive.cmdHatchMode();
    m_cameraServer.setSource(m_cameraHatch);
    timerHatchExtend = timeExtend;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    System.out.println("Starting teleopInit() method.");
  }

  private void workHatch() {
    if (m_control.getGrabbing()) {
      m_hatch.cmdGrab(true);
    } else {
      m_hatch.cmdGrab(false);
    }

    if (m_control.getExtended()) {
      m_hatch.cmdExtend(true);
    } else {
      m_hatch.cmdExtend(false);
    }

    if (m_control.getHatchView()) {
      m_cameraServer.setSource(m_cameraHatch);
    } else {
      m_cameraServer.setSource(m_cameraField);
    }
  }

//  private void workCargo() {
//    m_drive.cmdCargoMode();
//  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    m_control.periodic();

    if (timerHatchExtend > 0) {
      timerHatchExtend -= 1;
      if (timerHatchExtend == 0) {
        System.out.print("extend hatch at time ");
        System.out.println(timerHatchExtend);
        m_hatch.cmdExtend(true);
        m_control.setHatchExtended();
      }
    }
    workHatch();    
    m_drive.cmdMove(m_control.getDriveX(), m_control.getDriveY(), m_control.getDriveR());
  }

  @Override
  public void testInit() {
    System.out.println("Starting testInit() method.");
    m_hatch.cmdExtend(false);
    m_hatch.cmdGrab(false);
}

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    m_control.periodic();
  }
}