package com.brennan.datastate;

import com.brennan.utils.SwingObservableState;

public class EVSEDataState {
  public SwingObservableState<Boolean> verifyComplete = new SwingObservableState<>(true);

  public SwingObservableState<Float> power = new SwingObservableState<>(0f);

}
