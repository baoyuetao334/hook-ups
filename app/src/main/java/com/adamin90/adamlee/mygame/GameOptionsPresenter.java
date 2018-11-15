package com.adamin90.adamlee.mygame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;

import com.adamin90.adamlee.mygame.mvp.ViewPresenter;
import com.adamin90.adamlee.mygame.util.FontCache;
import com.adamin90.adamlee.mygame.util.Views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2015/8/21.
 */
public class GameOptionsPresenter extends ViewPresenter<GameOptionsView> implements GameOptionsView.Presenter {
    private static GameOptionsPresenter a;
    private final LevelManager mLevelManager;
    private boolean c;

    static GameOptionsPresenter a(Context context) {
        if (a == null) {
            a = new GameOptionsPresenter(context);
        }
        return a;
    }

    static GameOptionsPresenter a() {
        if (a != null) {
            return a;
        }
        throw new IllegalStateException("Presenter not initialized");
    }

    private GameOptionsPresenter(Context context) {
        this.mLevelManager = LevelManager.getInstance(context);
    }

    public void attachView(GameOptionsView gameOptionsView) {
        super.attachView(gameOptionsView);
        b();
    }

    public void toggleMenu() {
        this.c = !this.c;
        if (getView() != null) {
            ((GameOptionsView) getView()).a(this.c, true);
        }
        GameScenePresenter.a().a(this.c);
        if (this.c) {
            SoundManager.get().d();
        } else {
            SoundManager.get().e();
        }
    }

    public boolean isMenuExpanded() {
        return this.c;
    }

    public void toggleSound() {
        SoundManager.get().j();
    }

    public boolean isSoundEnabled() {
        return SoundManager.get().k();
    }

    public void nextLevel() {
        if (this.c && this.mLevelManager.isGameEnd()) {
            SoundManager.get().f();
            this.mLevelManager.nextLevel();
            GameScenePresenter.a().startNewGame(false);
        }
    }

    public void previousLevel() {
        if (this.c && this.mLevelManager.isContinueGame()) {
            SoundManager.get().f();
            this.mLevelManager.preLevel();
            GameScenePresenter.a().startNewGame(false);
        }
    }

    public boolean isNextEnabled() {
        return this.mLevelManager.isGameEnd();
    }

    public boolean isPreviousEnabled() {
        return this.mLevelManager.isContinueGame();
    }

    public void screenshot() {
//        GameScenePresenter.width().width("screenshot", String.valueOf(this.height.cells()), 0);
        GameSceneView gameSceneView = (GameSceneView) GameScenePresenter.a().getView();
        if (gameSceneView != null) {
            Context context = gameSceneView.getContext();
            Bitmap createBitmap = Bitmap.createBitmap(gameSceneView.getWidth(), gameSceneView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(gameSceneView.d);
            canvas.save();
            canvas.translate(0.0f, (float) (-Views.dpToPx(context, 12.0f)));
            gameSceneView.draw(canvas);
            canvas.restore();
            Paint paint = new Paint(1);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(gameSceneView.e);
            String format = String.format("#%s", new Object[]{Integer.valueOf(this.mLevelManager.currentLevel())});
            paint.setTextSize((float) Views.dpToPx(context, 21.0f));
            paint.setTypeface(FontCache.loadFont(context, FontCache.BOLD));
            canvas.drawText(format, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() - Views.dpToPx(context, 50.0f)), paint);
            paint.setTextSize((float) Views.dpToPx(context, 16.0f));
            paint.setTypeface(FontCache.loadFont(context, FontCache.LIGHT));
            paint.setAlpha(153);
            canvas.drawText("lixiaopeng.top", (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() - Views.dpToPx(context, 25.0f)), paint);
            Parcelable a = a(gameSceneView.getContext(), createBitmap, this.mLevelManager.currentLevel());
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", String.format("Loop %s http://lixiaopeng.top", new Object[]{format}));
            intent.putExtra("android.intent.extra.STREAM", a);
            intent.setType("image/*");
            gameSceneView.getContext().startActivity(Intent.createChooser(intent, "Share Image"));
        }
    }

    void b() {
        if (getView() != null) {
            ((GameOptionsView) getView()).setOptionsColor(this.mLevelManager.a);
            ((GameOptionsView) getView()).setSoundEnabled(isSoundEnabled());
            ((GameOptionsView) getView()).a(isMenuExpanded(), false);
            ((GameOptionsView) getView()).b(this.mLevelManager.isContinueGame(), this.mLevelManager.isGameEnd());
        }
    }

    void a(boolean z) {
        if (getView() != null) {
            ((GameOptionsView) getView()).setScreenshotVisible(z);
        }
    }

    private Uri a(Context context, Bitmap bitmap, int i) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InfiniteLoop");
        file.mkdirs();
        File file2 = new File(file, "loop_" + i + ".png");
        try {
            OutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
        }
        Uri fromFile = Uri.fromFile(file2);
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(fromFile);
        context.sendBroadcast(intent);
        return fromFile;
    }

}
