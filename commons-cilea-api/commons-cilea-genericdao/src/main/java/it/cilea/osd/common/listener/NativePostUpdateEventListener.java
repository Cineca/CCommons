package it.cilea.osd.common.listener;

import it.cilea.osd.common.model.Identifiable;

public interface NativePostUpdateEventListener
{

    <T extends Identifiable> void onPostUpdate(T entity);

}
