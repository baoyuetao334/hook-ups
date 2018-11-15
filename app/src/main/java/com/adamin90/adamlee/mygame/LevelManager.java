package com.adamin90.adamlee.mygame;

import android.content.Context;
import android.content.SharedPreferences;

import com.adamin90.adamlee.mygame.data.Level;
import com.adamin90.adamlee.mygame.data.LevelData;

import java.util.Map;

/**
 * Created by Administrator on 2015/8/21.
 */
public class LevelManager {
    private static LevelManager mLevelManager;
    int a;
    private int mCurrentLevel;
    private int mHighestLevel;
    private final SharedPreferences mPreferences;
    private final Map<String, Level> mLevelMap;
    private final PMRandom mRandom;

    static LevelManager getInstance(Context context) {
        if (mLevelManager == null) {
            mLevelManager = new LevelManager(context.getApplicationContext());
        }
        return mLevelManager;
    }

    private LevelManager(Context context) {
        this.mPreferences = context.getSharedPreferences("loops", 0);
        this.mLevelMap = LevelData.loadFromResources(context, R.raw.levels);
        this.mRandom = PMRandom.getInstance();
        this.mCurrentLevel = this.mPreferences.getInt("current_level", 0);
        this.mHighestLevel = this.mPreferences.getInt("highest_level", this.mCurrentLevel);
    }

    GameBoard createGame() {
        int curLevel = currentLevel();
        Level level = this.mLevelMap.get(String.valueOf(curLevel));
        if (level != null) {
            return new GameBoard(level);
        }
        int floor = (int) Math.floor(Math.log((double) curLevel) / Math.log(2.16d));
        int floor2 = (int) Math.floor(Math.log((double) curLevel) / Math.log(1.58d));
        if (((double) this.mRandom.rand()) > 0.5d) {
            floor2++;
        }
        if (((double) this.mRandom.rand()) > 0.5d) {
            floor++;
        }
        curLevel = Math.min(floor, 8);
        int min = Math.min(floor2, 13);
        float b = (float) ((((double) this.mRandom.rand()) * 0.24d) + 0.33d);
        if (((double) this.mRandom.rand()) > 0.9d) {
            floor2 = floor;
        } else {
            floor2 = (int) (Math.ceil(((double) ((float) (curLevel - 3))) * Math.pow((double) this.mRandom.rand(), 0.3333333333333333d)) + 3.0d);
            floor = (int) (Math.ceil(((double) ((float) (min - 3))) * Math.pow((double) this.mRandom.rand(), 0.3333333333333333d)) + 3.0d);
        }
        return new GameBoard(floor2, floor, b);
    }

    void saveLevel() {
        this.mRandom.calcValue((((long) this.mCurrentLevel) * 1000) + ((long) this.mCurrentLevel));
        this.a = (int) (this.mRandom.rand() * 360.0f);
        this.mPreferences.edit().putInt("current_level", this.mCurrentLevel).apply();
        if (this.mCurrentLevel > this.mHighestLevel) {
            this.mPreferences.edit().putInt("highest_level", this.mCurrentLevel).apply();
            this.mHighestLevel = this.mCurrentLevel;
        }
    }

    int currentLevel() {
        return this.mCurrentLevel;
    }

    void nextLevel() {
        this.mCurrentLevel++;
    }

    void preLevel() {
        this.mCurrentLevel--;
    }

    boolean isGameEnd() {
        return this.mCurrentLevel < this.mHighestLevel;
    }

    boolean isContinueGame() {
        return this.mCurrentLevel > 0;
    }

}
