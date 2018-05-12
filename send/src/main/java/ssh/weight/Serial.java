package ssh.weight;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Serial
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static String Serialnumber = "COM1";
	public static Serial instance;
	Thread srThread;
	Thread swThread;
	CommPortIdentifier portIdentifier;
	CommPort commPort;
	SerialPort serialPort;
	InputStream in;
	OutputStream out;
	
    static String temp="";
    
    public Serial()
    {
        super();
    }
    
    public void connect ( String portName ) throws Exception
    {
    	instance = this;
    	try {
        portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Errror : Port useing...");
        }
        else
        {
            
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                
                 //read, Thread Port OPEN
                srThread = new Thread(new SerialReader(in));
                swThread = new Thread(new SerialWriter(out));
                srThread.start();
                swThread.start();
                
            }
            else
            {
                System.out.println("Error: Only start of this instance");
            }
        }     
    	}catch(NoSuchPortException e) {
    		ssh.alert.alertLevel.showalertLevel(5);
    		RootLayoutCtlr.setFlag(1);
    	}
    }
    /*��� ����*/
	public static void stop() throws Exception
    {
    	 instance.portIdentifier=null;
    	 instance.commPort.close();
    	 instance.serialPort.close();
    	 instance.in.close();
    	 instance.out.close();
    	 instance.srThread = null;
    	 instance.swThread = null;
    }
    /** */
    //������ ����
    public static class SerialReader extends Serial implements Runnable 
    {
        InputStream in;
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            StringBuffer result = new StringBuffer();
            try {
            	Thread.sleep(1000);
            	logger.info("Initializing...");
	            RootLayoutCtlr.setFlag(0);
	            for(int i = 5 ; i > 0 ; i--) {
				logger.info(i+"");
				Thread.sleep(1000);
	            }
	            OverViewCtlr.setspButton(true);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {      
                	Thread.sleep(1000); // 1�ʸ��� ���۰� ����
                	if(RootLayoutCtlr.getlogFlag() != 1) break;
                	result.append(new String(buffer,0,len));
                	logger.debug(result.toString());
                	temp = result.toString();     
                	if(temp == "")ssh.alert.alertLevel.showalertLevel(10);
                	OverViewCtlr.setLogData(temp);
                	result.setLength(0);
                }
            }
            catch ( IOException e )
            {
               e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
        }
    }

    /** */
    //������ �۽�
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void main ( String[] args )
    {
    	System.setProperty(XmlConfigurationFactory.CONFIGURATION_FILE_PROPERTY,"conf/log4j2.xml");
        try
        {
            (new Serial()).connect(Serialnumber); //�Է��� ��Ʈ�� ����
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String GetResult() {
    	if(temp!="" ||temp!=" "||temp!="^")
    	return temp;
    	else
    		return "E";
    }
    
    public static void SetSeport(String s){
    	Serialnumber = s;
    	 
    }
    
}