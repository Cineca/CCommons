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
