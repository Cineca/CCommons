package it.cilea.osd.common.listener;

import it.cilea.osd.common.model.Identifiable;

public interface NativePostDeleteEventListener
{

    <P> void onPostDelete(P entity);

}
