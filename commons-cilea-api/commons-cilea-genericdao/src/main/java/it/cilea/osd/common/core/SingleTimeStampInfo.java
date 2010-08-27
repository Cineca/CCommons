package it.cilea.osd.common.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
/**
 * This class mantains basic information about an event. At the moment only the
 * timestamp of the event is recorded but the class stay here for future
 * extension.
 */
public class SingleTimeStampInfo implements Serializable {
	
	/**
	 * timestamp 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	public SingleTimeStampInfo() {
		
	}

	/**
     * Fast constructor.
     * 
     * @param date
     *            the event timestamp
     */
    public SingleTimeStampInfo(Date date) {
		this.timestamp = date;
	}

	   
    /**
     * Getter method.
     * 
     * return
     *            the event timestamp
     */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
     * Setter method.
     * 
     * @param date
     *            the event timestamp
     */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
