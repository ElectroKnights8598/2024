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
import frc.robot.subsystems.DriveSubsystem;

public class Autos {
  DriveSubsystem m_driveSubsystem  = DriveSubsystem.getInstance();
      private final SendableChooser<Command> autonChooser;
     
     
      Intakelauncher intake = new Intakelauncher();
      Retract retract = new Retract();
      ArmDown armdown = new ArmDown();
      ArmUp armup  = new ArmUp();
    FeedLauncher feedLauncher = new FeedLauncher();
    LaunchTheNote launchTheNote = new LaunchTheNote();

      public Autos(){
AutoBuilder.configureHolonomic(
      m_driveSubsystem::getPose, // Robot pose supplier
      m_driveSubsystem::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
      m_driveSubsystem::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
      m_driveSubsystem::driveRobotRelative, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
      new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
              new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
              new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
              4.5, // Max module speed, in m/s
              0.4, // Drive base radius in meters. Distance from robot center to furthest module.
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
        NamedCommands.registerCommand("armdown", armdown);
        NamedCommands.registerCommand("armup", armup);
        NamedCommands.registerCommand("feedlauncher", feedLauncher);
         NamedCommands.registerCommand("launchthenote", launchTheNote);
         autonChooser.setDefaultOption("nope", null);
         buildAuto("route");
         buildAuto("testarm");
         buildAuto("shoot");
     buildAuto("left 1 note");
      buildAuto("left 2 note");
       SmartDashboard.putData("Auton Chooser", autonChooser);

     
       
      }
      public Command getSelected() {
        return autonChooser.getSelected();
      }
    
        private void buildAuto(String autoName) {
            Command autoCommand = AutoBuilder.buildAuto(autoName);
            autonChooser.addOption(autoName, autoCommand);
          }
}
