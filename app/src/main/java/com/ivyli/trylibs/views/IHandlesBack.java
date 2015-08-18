package com.ivyli.trylibs.views;

public interface IHandlesBack{
    /**
     * Returns <code>true</code> if back event was handled, <code>false</code> if someone higher in
     * the chain should.
     */
    boolean onBackPressed();
}
