// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TestArm extends SequentialCommandGroup {

  /** Creates a new TestArm. */
  public TestArm() {
 ArmSubsystem m_arm =  ArmSubsystem.getInstance();
 PathPlannerPath.fromPathFile("mid path1");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
     new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kScoringPosition)),
     Commands.waitSeconds(2),
     new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kIntakePosition)),
     Commands.waitSeconds(2),
     new InstantCommand(() -> m_arm.setTargetPosition(Constants.Arm.kScoringPosition)));
  }
}
