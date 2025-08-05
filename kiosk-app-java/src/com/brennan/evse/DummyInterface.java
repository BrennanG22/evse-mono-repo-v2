package com.brennan.evse;

import com.brennan.datastate.EVSEDataState;

public class DummyInterface implements EVSECommunication {

  public EVSEDataState Evsedata = new EVSEDataState();

  @Override
  public void startCharge() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'startCharge'");
  }

  @Override
  public void stopCharge() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'stopCharge'");
  }

  @Override
  public boolean isAuthed() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isAuthed'");
  }

  @Override
  public void setDataState(EVSEDataState state) {
    this.Evsedata = state;
  }

  @Override
  public void validate() {
    try {
      if (Evsedata.rfidScanned.get() && Evsedata.evConnected.get()) {
        Evsedata.verifyComplete.set(true);
      } else {
        Evsedata.verifyComplete.set(false);
      }
    } catch (Exception e) {

    }
  }

}
