package com.akg.gp.fcmdemo;

/**
 * Created by GP on 10-Feb-17.
 */

public interface NetworkResponseListener {
     void preRequest();
     Object postRequest(Object object);
}
