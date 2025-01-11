package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class ArmUp extends Command{
    private Timer m_timer;
    ArmSubsystem m_arm = ArmSubsystem.getInstance();
    // help
    public ArmUp(){
      
        addRequirements(m_arm);
    }
    @Override
          public void initialize() {       
                        m_arm.setTargetPosition(Constants.Arm.kScoringPosition);
                        SmartDashboard.putString("Cheese", "Arm up started!");
     
          }

          @Override
          public void execute() {
            m_arm.runAutomatic();
          }
//gkhkhjhkjhkjhjhjhkjhkjhkjhkjhkjhjh
          @Override
          public boolean isFinished() {
            return m_arm.position()>= Constants.Arm.kScoringPosition;          }

          @Override
          public void end(boolean interrupted) {
            SmartDashboard.putString("apple", "Arm up is finoshed");
          }
}