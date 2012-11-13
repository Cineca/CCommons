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

/**
 * This class came from Ted Bergeron 
 * see: http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html
 * 
 * 
 * This interface defines an object that can be selected
 * such as with a drop down list.
 *
 * @author Ted Bergeron
 * @version $Id: Selectable.java,v 1.2 2008-11-18 17:26:23 bollinicvs Exp $
 */
public interface Selectable {
    String getIdentifyingValue();

    String getDisplayValue();
}
