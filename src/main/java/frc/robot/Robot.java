/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Drive;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //private static final String kDefaultAuto = "Default";
  //private static final String kCustomAuto = "My Auto";
  //private String m_autoSelected;
  //private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();
  private final Joystick m_stick = new Joystick(0);
  private final Drive m_drive = new Drive(m_gyro);
  private final Hatch m_hatch = new Hatch();
  private final UsbCamera m_cameraCargo = CameraServer.getInstance().startAutomaticCapture(0);
  private final UsbCamera m_cameraHatch = CameraServer.getInstance().startAutomaticCapture(1);
  private final Control m_control = new Control(m_stick, m_cameraHatch, m_cameraCargo);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("Starting robotInit() method.");
    //m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    //m_chooser.addOption("My Auto", kCustomAuto);
    //SmartDashboard.putData("Auto choices", m_chooser);
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
    m_hatch.setMode(Hatch.modeStart & ~Hatch.modeGrab);
    m_control.setMode(Control.modeHatchRelease);
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
    //m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    //switch (m_autoSelected) {
    //  case kCustomAuto:
        // Put custom auto code here
    //    break;
    //  case kDefaultAuto:
    //  default:
        // Put default auto code here
    //    break;
    //}
  }

  @Override
  public void teleopInit() {
    System.out.println("Starting teleopInit() method.");
    m_hatch.setMode(Hatch.modeStart | Hatch.modeGrab);
    m_control.setMode(Control.modeHatchGrab);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    m_drive.setMode(m_control.getDriveMode());
    m_drive.cartMove(m_control.getDriveX(), m_control.getDriveY(), m_control.getDriveR());

    m_hatch.setMode(m_control.getHatchMode());

    m_control.teleOp();
  }

  @Override
  public void testInit() {
    System.out.println("Starting testInit() method.");
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
