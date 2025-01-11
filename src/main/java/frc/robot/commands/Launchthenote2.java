package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import edu.wpi.first.wpilibj.Timer;
public class Launchthenote2 extends Command{
    private Timer m_timer;
    

    IntakeSubsystem m_intake = IntakeSubsystem.getInstance();
    LauncherSubsystem m_launcher = LauncherSubsystem.getInstance();
    
    // help
    public Launchthenote2(){
      
        addRequirements(m_intake, m_launcher);
    }
    @Override
          public void initialize() {
            m_timer = new Timer();
            m_timer.restart();
          }

          @Override
          public void execute() {
            if(m_timer.get()>=1){
            m_intake.setPower(1);
            }
            m_launcher.runLauncher();
            //m_launcher.runLauncher();

          }

          @Override
          public boolean isFinished() {
            return m_timer.get() > 1.5;
          }

          @Override
          public void end(boolean interrupted) {
            m_intake.setPower(0);
            m_launcher.stopLauncher();
          }
}
