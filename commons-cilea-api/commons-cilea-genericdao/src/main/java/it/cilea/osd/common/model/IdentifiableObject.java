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
package it.cilea.osd.common.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class came from Ted Bergeron 
 * see: http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html
 *  
 * 
 * Base class for objects that can be uniquely identified via an Integer
 * property, id.
 * 
 * @author Ted Bergeron
 * @version $Id: IdentifiableObject.java,v 1.2 2008-11-18 17:26:23 bollinicvs Exp $
 */
@MappedSuperclass
public abstract class IdentifiableObject extends BaseObject implements
		Identifiable, Serializable {

	public abstract Integer getId();
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof IdentifiableObject)) {
			return false;
		}

		IdentifiableObject obj = (IdentifiableObject) object;

		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				getId(), obj.getId()).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(MAGICNUM1, MAGICNUM2).appendSuper(
				super.hashCode()).append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				getId()).toString();
	}

}
