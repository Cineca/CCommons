package it.cilea.osd.common.listener;

public interface NativeLoadEventListener
{

    <T> void onLoad(T entity);

}
