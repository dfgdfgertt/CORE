package com.automation.test.reader;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.apache.log4j.Logger;

public class IPv4StringBuilder extends AbstractDecoratorReader<String, String> {
	private Logger logger = Logger.getLogger(this.getClass());
	public IPv4StringBuilder(Reader<String> parent) {
		super(parent);
	}

	@Override
	public String read() throws Exception {
		String ipString = parent.getData();
		String ipv4String = "";
		try {
			IPAddress ipAddr = new IPAddressString(ipString).toAddress();
			if (ipAddr.isIPv4()) {
				ipv4String = ipString;
			} else if (ipAddr.isIPv6()) {
				logger.error("This is ipv6 address: " + ipAddr.toAddressString().toString());
				if (ipAddr.isIPv4Convertible()) {
					ipv4String = ipAddr.toIPv4().toAddressString().toString();
					logger.error("Convert the above ipv6 to ipv4 address: " + ipv4String);
				} else {
					logger.error("Can NOT convert the above ipv6 to ipv4 address");
				}
			}
		} catch (AddressStringException e) {
			logger.error(e.getMessage());
		}
		return ipv4String;
	}

}
