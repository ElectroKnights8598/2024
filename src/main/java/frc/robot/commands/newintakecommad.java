package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import edu.wpi.first.wpilibj.Timer;
public class newintakecommad extends Command{
    private Timer m_timer;
    IntakeSubsystem m_intake = IntakeSubsystem.getInstance();
    // help
    public newintakecommad(){
      
        addRequirements(m_intake);
    }
    @Override
          public void initialize() {
            m_timer = new Timer();
            m_timer.start();
          }

          @Override
          public void execute() {
            m_intake.setPower(.5);

          }

          @Override
          public boolean isFinished() {
            return m_timer.get() > 3;
          }

          @Override
          public void end(boolean interrupted) {
            m_intake.setPower(0);
           
          }
}
