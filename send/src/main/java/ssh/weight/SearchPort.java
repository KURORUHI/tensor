package ssh.weight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
//Do not use!!!!!!because this is Only Serial Port Search
public class SearchPort {
	protected static final Logger logger = LoggerFactory.getLogger(SearchPort.class);
	public SearchPort() {
		super();
	}
	public static int SearchPortNumber() {
		for(int pt =1;pt<=256;pt++) {
				try {
					@SuppressWarnings("unused")
					CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM"+Integer.toString(pt));
						return pt;
					} 
				catch (NoSuchPortException e) {
						
					}
		}
		return 0;
	}
}
