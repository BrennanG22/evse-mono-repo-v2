package com.brennan.evse;

import com.brennan.datastate.EVSEDataState;

public interface EVSEComunaction {

  public void startCharge();

  public void stopCharge();

  public boolean isAuthed();

  public void setDataState(EVSEDataState state);
}
