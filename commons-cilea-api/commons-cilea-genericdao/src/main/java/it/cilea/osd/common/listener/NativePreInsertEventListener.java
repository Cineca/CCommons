package it.cilea.osd.common.listener;

import it.cilea.osd.common.model.Identifiable;

public interface NativePreInsertEventListener
{

    <T extends Identifiable> void onPreInsert(T entity);

}
