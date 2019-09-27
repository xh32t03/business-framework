package com.easyway.business.framework.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IdLock<T> {

    private ConcurrentLinkedQueue<T> idQueue;

    public IdLock(){
        idQueue = new ConcurrentLinkedQueue<T>();
    }

    public void lock(T id) {
        while (idQueue.contains(id)) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        idQueue.add(id);
    }

    public void unlock(T id) {
        idQueue.remove(id);
    }
}
