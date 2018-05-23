package com.jlc.app.milk_mini.shell.selfImp;

import java.util.Iterator;

/**
 * Created by king on 16/12/14.
 */

public class MIterable implements Iterable {
    private Iterable iterable;

    public MIterable(Iterable iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator iterator() {
        return iterable.iterator();
    }
}
