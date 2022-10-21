package es.upv.grycap.tracer.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.exceptions.TraceException;
import es.upv.grycap.tracer.exceptions.UncheckedInterruptedException;
import es.upv.grycap.tracer.model.TracerTime;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TimeManager {
	
	protected TracerTime tracerTime;
	
	public TimeManager(@Autowired final TracerTime tracerTime) {
		this.tracerTime = tracerTime;
		
	}
	
	public synchronized long getTime() {
		if (tracerTime.isUseLocal()) {
			try {
				TimeUnit.MILLISECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new UncheckedInterruptedException(e);
			}
			return Instant.now().toEpochMilli();
		} else { 
	        NTPUDPClient timeClient = new NTPUDPClient();
	        timeClient.setDefaultTimeout(tracerTime.getNtpTimeout());
	        InetAddress inetAddress;
	        Long id = null;
	        for (String timeServer: tracerTime.getTimeServers()) {
	        	int retries = tracerTime.getRetries();
	        	do {
					try {
						inetAddress = InetAddress.getByName(timeServer);
				        TimeInfo timeInfo = timeClient.getTime(inetAddress);
				        id =  timeInfo.getMessage().getTransmitTimeStamp().getTime();
						TimeUnit.MILLISECONDS.sleep(2);
					} catch (SocketTimeoutException e) {
						log.error("Error getting the timespamp: " + e.getLocalizedMessage());
						--retries;
					} catch (IOException | InterruptedException e) {
						log.error("Error getting the timespamp", e);
						--retries;
					}
	        	} while (retries >= 0 && id == null);
	        }
	        if (id == null) {
	        	throw new TraceException("Unable to get the current time from the list of NIST time servers, cannot generate the trace ID.");
	        } else {
	        	return id;
	        }
			
		}
	}

}
