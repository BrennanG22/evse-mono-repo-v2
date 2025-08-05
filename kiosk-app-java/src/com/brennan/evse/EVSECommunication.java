package com.brennan.evse;

import com.brennan.datastate.EVSEDataState;

public interface EVSECommunication {

  public void startCharge();

  public void stopCharge();

  public boolean isAuthed();

  public void setDataState(EVSEDataState state);

  public void validate();
}
