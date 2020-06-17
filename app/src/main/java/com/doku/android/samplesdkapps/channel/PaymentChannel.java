package com.doku.android.samplesdkapps.channel;

/**
 * Created by dedye on 03,June,2020
 */
public enum PaymentChannel {
    VA_MANDIRI  (1),
    VA_SYARIAH_MANDIRI (2);

    private int value;

    PaymentChannel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
