package it.cilea.osd.common.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Simple embeddable object to mantains timestamp information on creation and
 * lastupdate
 * 
 * 
 * @author cilea
 */
@Embeddable
public class TimeStampInfo implements ITimeStampInfo, Serializable
{

    @Embedded
    @AttributeOverride(name = "timestamp", column = @Column(name = "timestampCreated"))
    /** information about the creation of the object */
    private SingleTimeStampInfo timestampCreated;

    @Embedded
    @AttributeOverride(name = "timestamp", column = @Column(name = "timestampLastModified"))
    /** information about the last modification of the object */
    private SingleTimeStampInfo timestampLastModified;

    /**
     * Setter method.
     * 
     * @return information about the creation of the object
     */
    public SingleTimeStampInfo getTimestampCreated()
    {
        return timestampCreated;
    }

    /**
     * Getter method.
     * 
     * @return information about the last modification of the object
     */
    public SingleTimeStampInfo getTimestampLastModified()
    {
        return timestampLastModified;
    }

    /**
     * Setter method.
     * 
     * @param timestamp
     *            information about the creation of the object
     */
    public void setInfoCreated(SingleTimeStampInfo timestamp)
    {
        this.timestampCreated = timestamp;
    }

    /**
     * Setter method.
     * 
     * @param timestamp
     *            information about the last modification of the object
     */
    public void setInfoLastModified(SingleTimeStampInfo timestamp)
    {
        this.timestampLastModified = timestamp;
    }
    
    public Date getCreationTime()
    {
        if (timestampCreated == null || timestampCreated.getTimestamp() == null)
        {
            return null;
        }
        return timestampCreated.getTimestamp();
    }
    
    public Date getLastModificationTime()
    {
        if (timestampLastModified == null || timestampLastModified.getTimestamp() == null)
        {
            return getCreationTime();
        }
        return timestampCreated.getTimestamp();
    }
}
