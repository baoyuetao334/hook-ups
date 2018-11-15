package com.adamin90.adamlee.mygame;

import java.util.Random;

/**
 * Created by Administrator on 2015/8/21.
 */
public final class PMRandom {

    private  static  PMRandom pmRandom;

    private  long nextValue;

    public PMRandom() {
       nextValue = (long)new Random().nextInt(10000);
    }

    public  static  PMRandom getInstance(){
        if(pmRandom == null){
            pmRandom = new PMRandom();
        }
        return pmRandom;
    }

    void calcValue(long j){
        nextValue = j % Integer.MAX_VALUE;
        if(nextValue <= 0 ){
            nextValue += (Integer.MAX_VALUE - 1);
        }
        calc();
        calc();
        calc();
    }


    long calc(){
        nextValue =(nextValue *16807) % Integer.MAX_VALUE;
        return nextValue;
    }

    float rand() {
        return (float) ((((double) calc()) - 1.0d) / 2.147483646E9d);
    }

}
