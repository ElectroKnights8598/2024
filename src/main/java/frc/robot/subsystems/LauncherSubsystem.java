package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LauncherSubsystem extends SubsystemBase {

  private CANSparkMax m_topMotor;
  private CANSparkMax m_bottomMotor;
  

  private boolean m_launcherRunning;
 


private static final LauncherSubsystem m_launcher = new LauncherSubsystem();

public static LauncherSubsystem getInstance(){
  return m_launcher;
}
enum state{
  launchnormal,
  DOWN,
  StOP,
  IDLE
  }
  state current = state.IDLE;
  /**
   * Creates a new LauncherSubsystem.
   */
  public LauncherSubsystem() {
    // create two new SPARK MAXs and configure them
    m_topMotor =
        new CANSparkMax(Constants.Launcher.kTopCanId, CANSparkLowLevel.MotorType.kBrushless);
    m_topMotor.setInverted(true);
    m_topMotor.setSmartCurrentLimit(Constants.Launcher.kCurrentLimit);
    m_topMotor.setIdleMode(IdleMode.kBrake);

    m_topMotor.burnFlash();

    m_bottomMotor =
        new CANSparkMax(Constants.Launcher.kBottomCanId, CANSparkLowLevel.MotorType.kBrushless);
    m_bottomMotor.setInverted(true);
    m_bottomMotor.setSmartCurrentLimit(Constants.Launcher.kCurrentLimit);
    m_bottomMotor.setIdleMode(IdleMode.kBrake);

    m_bottomMotor.burnFlash();

    m_launcherRunning = false;
  }
  

  /**
   * Turns the launcher on.  Can be run once and the launcher will stay running or run continuously in a {@code RunCommand}.
   */
  public void runLauncher() {
   current = state.launchnormal;
  }

  /**
   * Turns the launcher off.  Can be run once and the launcher will stay running or run continuously in a {@code RunCommand}.
   */
  public void stopLauncher() {
  current = state.StOP;
  }
  public void amplaunch(){
     current = state.DOWN;
  }



  @Override
  public void periodic() {
     SmartDashboard.updateValues();
    // this method will be called once per scheduler run
    // set the launcher motor powers based on whether the launcher is on or not
   switch ( current) {
    case launchnormal:{
       m_topMotor.set(Constants.Launcher.kTopPower);
       m_bottomMotor.set(Constants.Launcher.kBottomPower);
    }
    break;
      case StOP:{
        m_topMotor.set(0.0);
      m_bottomMotor.set(0.0);
      }
      break;
      case DOWN:{
        m_topMotor.set(.2);
        m_bottomMotor.set(.1);
      }

   
    default:
      break;
   }
 
   
   
    // if (m_launcherRunning) {
    //   m_topMotor.set(Constants.Launcher.kTopPower);
    //   m_bottomMotor.set(Constants.Launcher.kBottomPower);
    // } else {
    //   m_topMotor.set(0.0);
    //   m_bottomMotor.set(0.0);
    // }
  }
}
