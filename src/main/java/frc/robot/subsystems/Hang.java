package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Hang extends SubsystemBase {
    private CANSparkMax m_hang;
    private boolean isiton;
    private boolean goback;
enum state{
UP,
DOWN,
STOP,
IDLE
}
state current = state.IDLE;
//comment from here    
private static final Hang m_hangleft = new Hang(Constants.hang.leftcanID,false);
private static final Hang m_righthang = new Hang(Constants.hang.rightcanID, false);
    public static Hang getleftInstance(){
        return m_hangleft;
    }
    public static Hang getrightInstance(){
        return m_righthang;
    }
    // to here out highlight all of it  and press ctrl and / at the same time 
    /**
     * @param canID The CANID for the motor connected to the actuator
     * @param ifreverse Since they will be side by side one will be reversed 
     */
    public Hang(int canID, boolean ifreverse ){
       current = state.IDLE;
m_hang = new CANSparkMax(canID, MotorType.kBrushless);
m_hang.setInverted(ifreverse);
m_hang.setSmartCurrentLimit(Constants.hang.kCurrentLimit);
m_hang.setIdleMode(IdleMode.kBrake);
m_hang.burnFlash();
    }

    public void turnon(){
     current = state.UP;
    }
    public void stopoff(){
      current = state.STOP;
    }
    public void goback(){
        current = state.DOWN;
    }
    
    
    @Override
    public void periodic(){
        switch (current) {
            case UP:{
                m_hang.set(Constants.hang.speedup);
            }
                break;
            case DOWN:{
                m_hang.set(Constants.hang.speeddown);
            }
                break;
             case STOP:{
            m_hang.set(0);
                }
                break;
        }
    }
}
