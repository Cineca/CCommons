package it.cilea.osd.common.listener;

import it.cilea.osd.common.model.Identifiable;

public interface NativePreUpdateEventListener
{

    <T extends Identifiable> void onPreUpdate(T entity);

}
