/**
 * Cilea Commons Framework
 * 
 * Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
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
        return timestampLastModified.getTimestamp();
    }
}
