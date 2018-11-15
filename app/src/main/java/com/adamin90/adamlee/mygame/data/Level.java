package com.adamin90.adamlee.mygame.data;

import java.util.List;

/**
 * Created by adamlee on 2015/8/21.
 */
public final class Level {
    public final int height;
    public final Integer hue;
    public final List<List<List<Integer>>> tiles;
    public final int width;

    public Level(int width, int height, int hue, List<List<List<Integer>>> list) {
        this.width = width;
        this.height = height;
        this.hue = Integer.valueOf(hue);
        this.tiles = list;
    }


}
