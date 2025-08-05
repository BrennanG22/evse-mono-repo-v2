package com.brennan.evse;

import com.brennan.datastate.EVSEDataState;

public class DummyInterface implements EVSECommunication{

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDataState'");
    }
    
}
