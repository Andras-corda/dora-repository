package com.qbits.dora.client;

import com.qbits.dora.carving.Carving;

import java.util.BitSet;

public class SapProgress {
    public final BitSet spots;
    public final BitSet scraped = new BitSet(Carving.SIZE * Carving.SIZE);

    public SapProgress(BitSet spots) {
        this.spots = spots;
    }

    public void scrape(int x, int y) {
        scraped.set(y * Carving.SIZE + x);
    }

    public int percent() {
        if (spots.isEmpty()) {
            return 100;
        }
        BitSet found = (BitSet) spots.clone();
        found.and(scraped);
        return found.cardinality() * 100 / spots.cardinality();
    }
}
