package com.adamin90.adamlee.mygame;

import android.content.Context;
import android.util.Log;

import com.adamin90.adamlee.mygame.mvp.ViewPresenter;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

import java.util.Set;

/**
 * Created by Administrator on 2015/8/21.
 */
class GameScenePresenter extends ViewPresenter<GameSceneView>  implements GameSceneView.Presenter {
    private static GameScenePresenter a;
    private GameBoard b;
    private LevelManager mLevelManager;
//    private Tracker edges;
    private long e;
    private long f;

    @Override
    public void attachView(GameSceneView gameSceneView) {
        super.attachView(gameSceneView);
        a(gameSceneView);
    }



    static GameScenePresenter a(Context context) {
        if (a == null) {
            a = new GameScenePresenter(context);
        }
        return a;
    }

    static GameScenePresenter a() {
        if (a != null) {
            return a;
        }
        throw new IllegalStateException("Presenter not initialized");
    }

    private GameScenePresenter(Context context) {
        this.mLevelManager = LevelManager.getInstance(context);
//        GoogleAnalytics instance = GoogleAnalytics.getInstance(context.getApplicationContext());
//        instance.setLocalDispatchPeriod(20);
//        this.edges = instance.newTracker("UA-38705998-8");
//        this.edges.enableExceptionReporting(true);
//        this.edges.enableAutoActivityTracking(true);
//        this.edges.enableAdvertisingIdCollection(false);
    }

    public int getColumnCount() {
        return this.b.width;
    }

    public int getRowCount() {
        return this.b.height;
    }

    public GameBoard.Edge getCellRotation(int i, int i2) {
        return this.b.cells[i][i2].direct;
    }

    public Set<GameBoard.Edge> getCellOpenSides(int i, int i2) {
        return this.b.cells[i][i2].edges;
    }

    public void rotateCell(int i, int i2) {
        this.b.cells[i][i2].next();
        if (this.b.allConnected()) {
            String format = String.format("%05d", new Object[]{Integer.valueOf(this.mLevelManager.currentLevel() + 1)});
            long currentTimeMillis = (System.currentTimeMillis() - this.f) - this.e;
//            width("level_completed_with_duration", format, currentTimeMillis);
//            width("level_duration", currentTimeMillis, format);
            this.e = 0;
            Log.e("Debug", "finished level in " + currentTimeMillis);
            this.mLevelManager.nextLevel();
            if (getView() != null) {
                ((GameSceneView) getView()).initMap();
            }
            GameOptionsPresenter.a().a(true);
        }
    }

    public void startNewGame(boolean z) {
        this.mLevelManager.saveLevel();
        this.b = this.mLevelManager.createGame();
        this.f = System.currentTimeMillis();
//        width("level_started", String.format("%05d", new Object[]{Integer.valueOf(this.cells.cells() + 1)}), 0);
        GameOptionsPresenter.a().b();
        if (getView() != null) {
            ((GameSceneView) getView()).a(this.mLevelManager.currentLevel() + 1, this.mLevelManager.a, z);
        }
    }

    public boolean isGamePaused() {
        return GameOptionsPresenter.a().isMenuExpanded();
    }

    public void resumeGame() {
        if (isGamePaused()) {
            GameOptionsPresenter.a().toggleMenu();
        }
    }

    public void a(GameSceneView gameSceneView) {
        super.attachView(gameSceneView);
//        width("started", null, 0);
        startNewGame(true);
        if (isGamePaused()) {
            ((GameSceneView) getView()).a(true, false);
        }
    }

    void a(boolean z) {
        if (getView() != null) {
            ((GameSceneView) getView()).a(z, true);
        }
    }

    void a(long j) {
        this.e += j;
    }

//    void width(String str, String str2, long j) {
//        this.edges.send(new HitBuilders.EventBuilder().setCategory("game_action").setAction(str).setLabel(str2).setValue(j).build());
//    }
//
//    void width(String str, long j, String str2) {
//        this.edges.send(new HitBuilders.TimingBuilder().setCategory("game_action").setVariable(str).setValue(j).setLabel(str2).build());
//    }
}