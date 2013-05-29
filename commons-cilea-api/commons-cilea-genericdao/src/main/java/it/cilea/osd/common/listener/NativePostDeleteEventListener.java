package it.cilea.osd.common.listener;


public interface NativePostDeleteEventListener
{

    <P> void onPostDelete(P entity);

}
