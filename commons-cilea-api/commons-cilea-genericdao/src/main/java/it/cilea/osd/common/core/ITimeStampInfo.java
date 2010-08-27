package it.cilea.osd.common.core;

/**
 * Interface that persistent class should implements to support the
 * automatically record of creation/last update information
 * 
 * @author cilea
 * 
 */
public interface ITimeStampInfo {
	public void setInfoCreated(SingleTimeStampInfo timestamp);
	public void setInfoLastModified(SingleTimeStampInfo timestamp);
	public SingleTimeStampInfo getTimestampCreated();
	public SingleTimeStampInfo getTimestampLastModified();
}
