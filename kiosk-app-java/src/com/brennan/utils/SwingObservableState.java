package com.brennan.utils;

import javax.swing.SwingUtilities;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SwingObservableState<T> {
    private T value;
    private final Map<Integer, Consumer<T>> listeners = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(0);

    public SwingObservableState(T initialValue) {
        this.value = initialValue;
    }

    public synchronized T get() {
        return value;
    }

    public synchronized void set(T newValue) {
        if ((value == null && newValue != null) || (value != null && !value.equals(newValue))) {
            this.value = newValue;
            notifyListeners();
        }
    }

    public synchronized int addListener(Consumer<T> listener) {
        int id = nextId.getAndIncrement();
        listeners.put(id, listener);

        T currentValue = value;
        SwingUtilities.invokeLater(() -> listener.accept(currentValue));

        return id;
    }

    public synchronized void removeListener(int id) {
        listeners.remove(id);
    }

    private void notifyListeners() {
        T currentValue = this.value;
        for (Consumer<T> listener : listeners.values()) {
            SwingUtilities.invokeLater(() -> listener.accept(currentValue));
        }
    }
}
