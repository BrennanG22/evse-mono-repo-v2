package com.brennan.datastate;

import com.brennan.utils.SwingObservableState;

public class EVSEDataState {
  public SwingObservableState<Boolean> verifyComplete = new SwingObservableState<>(true);

  public SwingObservableState<Float> power = new SwingObservableState<>(0f);
  public SwingObservableState<Float> voltage = new SwingObservableState<Float>(null);
  public SwingObservableState<Float> current = new SwingObservableState<Float>(null);

  public SwingObservableState<Integer> soc = new SwingObservableState<Integer>(null);

}
