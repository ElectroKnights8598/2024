// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.FeedLauncher;
import frc.robot.commands.Autos;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Hang;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import frc.utils.GamepadUtils;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.util.ReplanningConfig;
import com.revrobotics.ColorSensorV3.Register;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
   private final Field2d field;


  // The robot's subsystems
  public static  final DriveSubsystem m_robotDrive =  DriveSubsystem.getInstance();
  public static final ArmSubsystem m_arm =  ArmSubsystem.getInstance();
  public static final IntakeSubsystem m_intake = IntakeSubsystem.getInstance();
  public static final LauncherSubsystem m_launcher = LauncherSubsystem.getInstance();
//comment from here to 
  public static final Hang m_hangleft = Hang.getleftInstance();
private static final Hang m_righthang = Hang.getrightInstance();
// here out press ctrl and / at the same time
  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);
   XboxController m_driverController2 = new XboxController(OIConstants.kdriver2);
FeedLauncher m_feedlauncher = new FeedLauncher();
Autos auto = new Autos();
//private final SendableChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    field = new Field2d();
        SmartDashboard.putData("Field", field);
         PathPlannerLogging.setLogCurrentPoseCallback((pose) -> {
            // Do whatever you want with the pose here
            field.setRobotPose(pose);
        });
        PathPlannerLogging.setLogTargetPoseCallback((pose) -> {
            // Do whatever you want with the pose here
            field.getObject("target pose").setPose(pose);
        });
        PathPlannerLogging.setLogActivePathCallback((poses) -> {
            // Do whatever you want with the poses here
            field.getObject("path").setPoses(poses);
        });

    // NamedCommands.registerCommand("feedlaunchr", m_feedlauncher);
    //     NamedCommands.registerCommand("exampleCommand",null );
    //     NamedCommands.registerCommand("someOtherCommand", null );
        
    //       configureButtonBindings();
    //       autoChooser = AutoBuilder.buildAutoChooser("route");
    
    //     SmartDashboard.putData("Auto Chooser", autoChooser);
    //     Command fb = new PathPlannerAuto("route");
    //     autoChooser.addOption("Forward back", null);
      

    // Configure default commands
    m_robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () ->
                m_robotDrive.drive(
                    -GamepadUtils.squareInput(
                        m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                    -GamepadUtils.squareInput(
                        m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                    -GamepadUtils.squareInput(
                        m_driverController.getRightX(), OIConstants.kDriveDeadband),
                    true,
                    false),
            m_robotDrive));

    // set the arm subsystem to run the "runAutomatic" function continuously when no other command
    // is running
    m_arm.setDefaultCommand(new RunCommand(() -> m_arm.runAutomatic(), m_arm));

    // set the intake to stop (0 power) when no other command is running
    m_intake.setDefaultCommand(new RunCommand(() -> m_intake.setPower(0.0), m_intake));

    // configure the launcher to stop when no other command is running
    m_launcher.setDefaultCommand(new RunCommand(() -> m_launcher.stopLauncher(), m_launcher));
    m_hangleft.setDefaultCommand(new RunCommand(() -> m_hangleft.stopoff(), m_hangleft));
    m_righthang.setDefaultCommand(new RunCommand(() -> m_righthang.stopoff(), m_righthang));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    // button to put swerve modules in an "x" configuration to hold position
    new JoystickButton(m_driverController, XboxController.Button.kX.value)
        .whileTrue(new RunCommand(() -> m_robotDrive.setX(), m_robotDrive));
         new JoystickButton(m_driverController, XboxController.Button.kA.value)
        .whileTrue(new RunCommand(() -> m_robotDrive.resetgyro(), m_robotDrive));

    // set up arm preset positions
    new JoystickButton(m_driverController2, XboxController.Button.kLeftBumper.value)
        .onTrue(new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kScoringPosition)));
  new JoystickButton(m_driverController2, XboxController.Button.kRightBumper.value)
        .onTrue(new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kIntakePosition)));
    new JoystickButton(m_driverController2, XboxController.Button.kStart.value)
        .onTrue(new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kHomePosition)));
    // intake controls (run while button is held down, run retract command once when the button is
    // released)
    new Trigger(
            () ->
                m_driverController2.getRightTriggerAxis()
                    > Constants.OIConstants.kTriggerButtonThreshold)
        .whileTrue(new RunCommand(() -> m_intake.setPower(Constants.Intake.kIntakePower), m_intake))
        .onFalse(m_intake.retract());
         new Trigger(
            () ->
                m_driverController2.getLeftTriggerAxis()
                    > Constants.OIConstants.kTriggerButtonThreshold)
        .whileTrue(new RunCommand(() -> m_intake.setPower(-Constants.Intake.kIntakePower), m_intake));
    // launcher controls (button to pre-spin the launcher and button to launch)
    new JoystickButton(m_driverController2, XboxController.Button.kA.value)
        .whileTrue(new RunCommand(() -> m_launcher.runLauncher(), m_launcher));
      // hang code so comment from here 
        new JoystickButton(m_driverController,XboxController.Button.kLeftBumper.value)
        .whileTrue(new RunCommand(()-> m_hangleft.turnon(), m_hangleft));
        new JoystickButton(m_driverController,XboxController.Button.kB.value)
        .whileTrue(new RunCommand(()-> m_hangleft.goback(), m_hangleft));
    new JoystickButton(m_driverController,XboxController.Button.kRightBumper.value)
        .whileTrue(new RunCommand(()-> m_righthang.turnon(), m_righthang));
       new  JoystickButton(m_driverController,XboxController.Button.kY.value)
       
        .whileTrue(new RunCommand(()-> m_righthang.goback(), m_righthang));
    new JoystickButton(m_driverController2, XboxController.Button.kB.value)
        .whileTrue(new RunCommand(() -> m_intake.setPower(1)));
         new JoystickButton(m_driverController2, XboxController.Button.kX.value)
        .whileTrue(new RunCommand(() -> m_launcher.amplaunch(), m_launcher));
        // to here out with the same method on top
// regular code
    new JoystickButton(m_driverController2, XboxController.Button.kY.value)
        .onTrue(m_intake.feedLauncher(m_launcher));
        // new JoystickButton(m_driverController, XboxController.Button.kRightBumper.value)
        // .whileTrue(new RunCommand(
        //     () -> m_robotDrive.setX(),
        //     m_robotDrive));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Create config for trajectory
    // TrajectoryConfig config =
    //     new TrajectoryConfig(
    //             AutoConstants.kMaxSpeedMetersPerSecond,
    //             AutoConstants.kMaxAccelerationMetersPerSecondSquared)
    //         // Add kinematics to ensure max speed is actually obeyed
    //         .setKinematics(DriveConstants.kDriveKinematics);

    // // An example trajectory to follow. All units in meters.
    // Trajectory exampleTrajectory =
    //     TrajectoryGenerator.generateTrajectory(
    //         // Start at the origin facing the +X direction
    //         new Pose2d(0, 0, new Rotation2d(0)),
    //         // Pass through these two interior waypoints, making an 's' curve path
    //         List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
    //         // End 3 meters straight ahead of where we started, facing forward
    //         new Pose2d(3, 0, new Rotation2d(0)),
    //         config);

    // var thetaController =
    //     new ProfiledPIDController(
    //         AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    // thetaController.enableContinuousInput(-Math.PI, Math.PI);

    // SwerveControllerCommand swerveControllerCommand =
    //     new SwerveControllerCommand(
    //         exampleTrajectory,
    //         m_robotDrive::getPose, // Functional interface to feed supplier
    //         DriveConstants.kDriveKinematics,

    //         // Position controllers
    //         new PIDController(AutoConstants.kPXController, 0, 0),
    //         new PIDController(AutoConstants.kPYController, 0, 0),
    //         thetaController,
    //         m_robotDrive::setModuleStates,
    //         m_robotDrive);

    // // Reset odometry to the starting pose of the trajectory.
    // m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // // Run path following command, then stop at the end.
    // return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false, false));
    
    
//PathPlannerPath path = PathPlannerPath.fromPathFile("route");
//return new PathPlannerAuto("path");

return /*autoChooser.getSelected();*/ auto.getSelected().andThen(new RunCommand(() -> m_robotDrive.setX(), m_robotDrive));
  }
}