package net.sf.nextbus.nextbuslogger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class Main 
{
    private static ClassPathXmlApplicationContext springCtx;
    public static void main( String[] args )
    {
        System.out.println("*** NextBus VehicleLocation Logger Demo ***");
        springCtx = new ClassPathXmlApplicationContext(new String[]{              
                    "nextbus-logger-spring-context.xml"
                });
    }
}
