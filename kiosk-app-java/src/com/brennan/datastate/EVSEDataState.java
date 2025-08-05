package com.brennan.datastate;

import com.brennan.utils.SwingObservableState;

public class EVSEDataState {
  public SwingObservableState<Boolean> verifyComplete = new SwingObservableState<>(false);

  public SwingObservableState<Boolean> rfidScanned = new SwingObservableState<Boolean>(false);
  public SwingObservableState<Boolean> evConnected = new SwingObservableState<Boolean>(true);

  public SwingObservableState<Float> power = new SwingObservableState<>(0f);
  public SwingObservableState<Float> voltage = new SwingObservableState<Float>(null);
  public SwingObservableState<Float> current = new SwingObservableState<Float>(null);

  public SwingObservableState<Integer> soc = new SwingObservableState<Integer>(null);

}
