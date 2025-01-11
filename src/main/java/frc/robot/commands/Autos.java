package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.revrobotics.ColorSensorV3.Register;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveSubsystem;

public class Autos extends Command{
  DriveSubsystem m_driveSubsystem  = DriveSubsystem.getInstance();
  ArmSubsystem m_arm =  ArmSubsystem.getInstance();
      private final SendableChooser<Command> autonChooser;
     
     
      Intakelauncher intake = new Intakelauncher();
      Retract retract = new Retract();
      ArmDown armdown = new ArmDown();
      ArmUp armup  = new ArmUp();
    FeedLauncher feedLauncher = new FeedLauncher();
    LaunchTheNote launchTheNote = new LaunchTheNote();
    Launchthenote2 launchthenote2 = new Launchthenote2();
    newintakecommad newintic = new newintakecommad();

      public Autos(){
AutoBuilder.configureHolonomic(
      m_driveSubsystem::getPose, // Robot pose supplier
      m_driveSubsystem::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
      m_driveSubsystem::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
      m_driveSubsystem::driveRobotRelative, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
      new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
              new PIDConstants(2, 0.0, 0.0), // Translation PID constants
              new PIDConstants(1.5, 0.0, 0.0), // Rotation PID constants
              3, // Max module speed, in m/s
              .521, // Drive base radius in meters. Distance from robot center to furthest module.
              new ReplanningConfig() // Default path replanning config. See the API for the options here
      ),
      () -> {
          // Boolean supplier that controls when the path will be mirrored for the red alliance
          // This will flip the path being followed to the red side of the field.
          // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

          var alliance = DriverStation.getAlliance();
          if (alliance.isPresent()) {
            return alliance.get() == DriverStation.Alliance.Red;
          }
          return false;
        },
      m_driveSubsystem // Reference to this subsystem to set requirements
);



        autonChooser = new SendableChooser<Command>();
        NamedCommands.registerCommand("intake", intake.andThen(retract));
        NamedCommands.registerCommand("armdown",  new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kIntakePosition)));
        NamedCommands.registerCommand("armup",  new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kScoringPosition)));
        NamedCommands.registerCommand("feedlauncher", feedLauncher);
        NamedCommands.registerCommand("launchthenote2", launchthenote2);
        NamedCommands.registerCommand("newint", newintic);
         NamedCommands.registerCommand("launchthenote", launchTheNote);
         autonChooser.setDefaultOption("nope", new InstantCommand());
         buildAuto("route");
         buildAuto("spline");
        // buildAuto("testarm");
         buildAuto("shoot");
     buildAuto("left 1 note");
      buildAuto("left 2 note");
      buildAuto("do not use");
      buildAuto("moveup");
      buildAuto("3 note");
       SmartDashboard.putData("Auton Chooser", autonChooser);
autonChooser.addOption("TestArm", new TestArm());
     
       
      }
      public Command getSelected() {
        return autonChooser.getSelected();
      }
     
       
    
        private void buildAuto(String autoName) {
            Command autoCommand = AutoBuilder.buildAuto(autoName);
            autonChooser.addOption(autoName, autoCommand);
          }
}
