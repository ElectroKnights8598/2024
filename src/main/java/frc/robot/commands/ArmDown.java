package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class ArmDown extends Command{
    private Timer m_timer;
    ArmSubsystem m_arm = ArmSubsystem.getInstance();
    
    public ArmDown(){
      
        addRequirements(m_arm);
    }
    @Override
          public void initialize() { 
             m_arm.setTargetPosition(Constants.Arm.kIntakePosition); 
            SmartDashboard.putString("Cheese1", "Arm down started!");
          
          }

          @Override
          public void execute() {
            SmartDashboard.putString("Cheese2", "Arm down executed!");
            m_arm.runAutomatic();
          }

          @Override
          public boolean isFinished() {
           return m_arm.position()>= Constants.Arm.kIntakePosition;
         
          }

          @Override
          public void end(boolean interrupted) {
            SmartDashboard.putString("blueberries", "Arm down finished");
          }
}