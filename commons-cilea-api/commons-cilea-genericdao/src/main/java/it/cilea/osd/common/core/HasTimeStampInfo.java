package it.cilea.osd.common.core;

public interface HasTimeStampInfo
{
    /**
     * Return a "not null" object with creation/last update informations
     * 
     * @return a "not null" object with creation/last update informations
     */
    public ITimeStampInfo getTimeStampInfo();
}
