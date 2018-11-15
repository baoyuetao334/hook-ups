package com.adamin90.adamlee.mygame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.adamin90.adamlee.mygame.mvp.ViewAttacher;
import com.adamin90.adamlee.mygame.util.ElasticEvaluator;
import com.adamin90.adamlee.mygame.util.FontCache;
import com.adamin90.adamlee.mygame.util.Views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/8/21.
 */
public class GameSceneView extends View {
    private static final TypeEvaluator argbEvaluator = new ArgbEvaluator();
    private boolean A;
    private boolean B;
    private boolean C;
    int a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int backgroundColor;
    SoundManager soundManager;
    Presenter presenter;
    private final Paint k;
    private final Paint l;
    private final Paint m;
    private int n;
    private int o;
    private int p;
    private float q;
    private float r;
    private float s;
    private float t;
    private float u;
    private float v;
    private final List<Node> nodes;
    private final List<Node> curNodes;
    private final List<TextNode> textNodes;
    private final Map<Node, Animator> animatorMap;

    interface Presenter extends ViewAttacher<GameSceneView> {
        Set<GameBoard.Edge> getCellOpenSides(int i, int i2);

        GameBoard.Edge getCellRotation(int i, int i2);

        int getColumnCount();

        int getRowCount();

        boolean isGamePaused();

        void resumeGame();

        void rotateCell(int i, int i2);

        void startNewGame(boolean z);
    }

    /* renamed from: com.balysv.loop.GameSceneView.1 */
    class AnonymousClass1 extends AnimatorListenerAdapter {
        final /* synthetic */ Node val$touchedNode;

        AnonymousClass1(Node node) {
            this.val$touchedNode = node;
        }

        public void onAnimationStart(Animator animator) {
            GameSceneView.this.soundManager.i();
        }

        public void onAnimationEnd(Animator animator) {
            GameSceneView.this.animatorMap.remove(this.val$touchedNode);
            GameSceneView.this.presenter.rotateCell(this.val$touchedNode.x, this.val$touchedNode.y);
        }
    }

    /* renamed from: com.balysv.loop.GameSceneView.3 */
    class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ List<Animator> val$animators;
        final /* synthetic */ TextNode val$textNode;

        AnonymousClass3(TextNode textNode, List list) {
            this.val$textNode = textNode;
            this.val$animators = list;
        }

        public void onAnimationEnd(Animator animator) {
            GameSceneView.this.C = true;
            GameSceneView.this.textNodes.remove(this.val$textNode);
            for (Animator start : this.val$animators) {
                start.start();
            }
        }
    }

    class Node {
        int alpha;
        Bitmap image;
        Rect rect;
        float rotation;
        int x;
        int y;

        private Node() {
        }

        void draw(Canvas canvas) {
            canvas.save();
            GameSceneView.this.l.setAlpha(Math.min(MotionEventCompat.ACTION_MASK, (int) (GameSceneView.this.v * ((float) this.alpha))));
            canvas.rotate(this.rotation, (float) (this.rect.left + (GameSceneView.this.p / 2)), (float) (this.rect.top + (GameSceneView.this.p / 2)));
            canvas.drawBitmap(this.image, (float) this.rect.left, (float) this.rect.top, GameSceneView.this.l);
            canvas.restore();
        }

        void setRotation(float f) {
            this.rotation = f;
            GameSceneView.this.invalidate();
        }

        void setAlpha(int i) {
            this.alpha = i;
            GameSceneView.this.invalidate();
        }
    }

    class TextNode {
        int alpha;
        int color;
        float scale;
        String text;

        private TextNode() {
        }

        void draw(Canvas canvas) {
            canvas.save();
            GameSceneView.this.m.setColor(this.color);
            GameSceneView.this.m.setAlpha(this.alpha);
            canvas.scale(this.scale, this.scale, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
            canvas.drawText(this.text, (float) (canvas.getWidth() / 2), ((float) (canvas.getHeight() / 2)) + ((GameSceneView.this.m.getTextSize() * this.scale) / 3.0f), GameSceneView.this.m);
            canvas.restore();
        }

        void setAlpha(int i) {
            this.alpha = i;
            GameSceneView.this.invalidate();
        }

        void setScale(float f) {
            this.scale = f;
            GameSceneView.this.invalidate();
        }
    }

    public GameSceneView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.k = new Paint(1);
        this.l = new Paint(1);
        this.m = new Paint(1);
        this.t = 0.0f;
        this.u = 1.0f;
        this.v = 1.0f;
        this.nodes = new ArrayList<>();
        this.curNodes = new ArrayList<>();
        this.textNodes = new ArrayList<>();
        this.animatorMap = new HashMap<>();
        this.backgroundColor = -1;
        this.k.setStyle(Paint.Style.STROKE);
        this.m.setTextAlign(Paint.Align.CENTER);
        this.m.setTextSize((float) getResources().getDimensionPixelSize(R.dimen.level_text_size));
        this.m.setTypeface(FontCache.loadFont(context, FontCache.LIGHT));
        this.presenter = GameScenePresenter.a(getContext());
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.soundManager = SoundManager.get();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.presenter.attachView(this);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.presenter.detachView(this);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        this.n = i;
        this.o = i2;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (this.C && motionEvent.getAction() == 0) {
            if (this.presenter.isGamePaused()) {
                this.presenter.resumeGame();
            } else if (!this.A) {
                Node a = containPosition(x, y);
                if (!(a == null || this.presenter.getCellOpenSides(a.x, a.y).isEmpty())) {
                    if (this.animatorMap.containsKey(a)) {
                        ((Animator) this.animatorMap.get(a)).end();
                    }
                    ObjectAnimator duration = ObjectAnimator.ofFloat(a, "rotation", a.rotation, a.rotation + 90.0f).setDuration(150);
                    this.animatorMap.put(a, duration);
                    duration.addListener(new AnonymousClass1(a));
                    duration.start();
                }
            } else if (this.B) {
                this.B = false;
                this.A = false;
                this.C = false;
                GameOptionsPresenter.a().a(false);
                ValueAnimator duration2 = ValueAnimator.ofInt(MotionEventCompat.ACTION_MASK, 0).setDuration(300);
//                duration2.addUpdateListener(GameSceneView$$Lambda$1.lambdaFactory$(this));
                duration2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setAlpha(animation);
                    }
                });
                duration2.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        GameSceneView.this.nodes.clear();
                        GameSceneView.this.presenter.startNewGame(true);
                    }
                });
                duration2.start();
            }
        }
        return true;
    }

    private /* synthetic */ void setAlpha(ValueAnimator valueAnimator) {
        for (Node node : this.nodes) {
            node.alpha = (Integer) valueAnimator.getAnimatedValue();
        }
        invalidate();
    }

    private Node containPosition(int x, int y) {
        for (Node node : this.nodes) {
            if (node.rect.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.translate(0.0f, this.t);
        canvas.scale(this.u, this.u, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
        for (Node draw : this.nodes) {
            draw.draw(canvas);
        }
        for (Node draw2 : this.curNodes) {
            draw2.draw(canvas);
        }
        for (TextNode draw3 : this.textNodes) {
            draw3.draw(canvas);
        }
        canvas.restore();
    }

    void a(int i, int i2, boolean z) {
        b();
        a(i2);
        this.q = (float) ((this.n / 2) - ((this.presenter.getColumnCount() * this.p) / 2));
        this.r = ((((float) this.o) - this.s) / 2.0f) - ((float) ((this.presenter.getRowCount() * this.p) / 2));
        this.nodes.clear();
        for (int i3 = 0; i3 < this.presenter.getColumnCount(); i3++) {
            for (int i4 = 0; i4 < this.presenter.getRowCount(); i4++) {
                this.nodes.add(createNode(i3, i4, this.b, this.c));
            }
        }
        List arrayList = new ArrayList();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(900);
        valueAnimator.setObjectValues(this.backgroundColor, this.a);
//        valueAnimator.addUpdateListener(GameSceneView$$Lambda$2.lambdaFactory$(this));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                d(animation);
            }
        });
        valueAnimator.setEvaluator(argbEvaluator);
        for (Node node : this.nodes) {
            int sqrt = (int) Math.sqrt(Math.pow((double) ((this.n / 2) - (node.rect.left + (this.p / 2))), 2.0d) + Math.pow((double) ((this.o / 2) - (node.rect.top + (this.p / 2))), 2.0d));
            ObjectAnimator duration = ObjectAnimator.ofInt(node, "alpha", 0, MotionEventCompat.ACTION_MASK).setDuration(300);
            duration.setStartDelay((long) ((sqrt / 2) + 200));
            arrayList.add(duration);
        }
        TextNode textNode = new TextNode();
        textNode.color = this.c;
        textNode.text = String.format("#%s", i);
        Animator ofFloat = ObjectAnimator.ofFloat(textNode, "scale", 0.9f, 1.0f);
        ofFloat.setDuration(z ? 1300 : 1100);
        Animator ofInt = ObjectAnimator.ofInt(textNode, "alpha", 0, MotionEventCompat.ACTION_MASK);
        ofInt.setDuration(400);
        Animator ofInt2 = ObjectAnimator.ofInt(textNode, "alpha", MotionEventCompat.ACTION_MASK, 0);
        ofInt2.setDuration(z ? 250 : 400);
        ofInt2.setStartDelay(z ? 1050 : 700);
        final AnimatorSet animatorSet = new AnimatorSet();
        if (z) {
            animatorSet.play(valueAnimator).before(ofFloat).before(ofInt).before(ofInt2);
        } else {
            animatorSet.playTogether(valueAnimator, ofFloat, ofInt, ofInt2);
        }
        this.C = false;
        this.textNodes.add(textNode);
        animatorSet.addListener(new AnonymousClass3(textNode, arrayList));
//        Views.runAfterMeasure(this, GameSceneView$$Lambda$3.lambdaFactory$(this, animatorSet));
        Views.runAfterMeasure(this, new Runnable() {
            @Override
            public void run() {
                a(animatorSet);
            }
        });
    }

    private /* synthetic */ void d(ValueAnimator valueAnimator) {
        setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    private /* synthetic */ void a(AnimatorSet animatorSet) {
        this.soundManager.g();
        animatorSet.start();
    }

    void a(boolean z, boolean z2) {
        float f = 0.0f;
        float f2 = 1.0f;
        if (z2) {
            float[] fArr = new float[2];
            fArr[0] = z ? 0.0f : 1.0f;
            if (!z) {
                f2 = 0.0f;
            }
            fArr[1] = f2;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            ofFloat.setEvaluator(new ElasticEvaluator(500.0f));
//            ofFloat.addUpdateListener(GameSceneView$$Lambda$4.lambdaFactory$(this));
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    c(animation);
                }
            });
            ofFloat.setDuration(500);

            ofFloat.start();
            return;
        }
        float f3;
        if (z) {
            f3 = 0.9f;
        } else {
            f3 = 1.0f;
        }
        this.u = f3;
        if (z) {
            f = (float) (-this.p);
        }
        this.t = f;
        if (z) {
            f2 = 0.3f;
        }
        this.v = f2;
        invalidate();
    }

    private /* synthetic */ void c(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.u = 1.0f - (0.1f * floatValue);
        this.t = ((float) (-this.p)) * floatValue;
        this.v = 1.0f - (floatValue * 0.7f);
        invalidate();
    }

    private void b() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.scene_padding_left_right);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.scene_padding_top_bot);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.tile_size);
        float columnCount = (float) ((this.n - (dimensionPixelSize * 2)) / this.presenter.getColumnCount());
        float rowCount = (float) ((this.o - (dimensionPixelSize2 * 2)) / this.presenter.getRowCount());
        if (rowCount < ((float) dimensionPixelSize3)) {
            float dimensionPixelSize4 = (float) ((this.o - getResources().getDimensionPixelSize(R.dimen.scene_padding_top_min)) - dimensionPixelSize2);
            this.s = Math.min(dimensionPixelSize4 - (((float) this.presenter.getRowCount()) * rowCount), (((float) dimensionPixelSize3) - rowCount) * ((float) this.presenter.getRowCount()));
            rowCount += this.s / ((float) this.presenter.getRowCount());
        } else {
            this.s = 0.0f;
        }
        this.p = (int) Math.min((double) dimensionPixelSize3, Math.floor((double) (Math.min(rowCount, columnCount) / 2.0f)) * 2.0d);
    }

    private void a(int i) {
        if (getBackground() != null) {
            this.backgroundColor = ((ColorDrawable) getBackground()).getColor();
        }
        this.a = Color.HSVToColor(new float[]{(float) i, 0.1f, 0.91f});
        this.b = Color.HSVToColor(new float[]{(float) i, 0.1f, 0.91f});
        this.c = Color.HSVToColor(new float[]{(float) i, 0.42f, 0.58f});
        this.d = Color.HSVToColor(new float[]{(float) i, 0.92f, 0.2f});
        this.e = Color.HSVToColor(new float[]{(float) i, 0.85f, 0.97f});
        this.f = Color.HSVToColor(new float[]{(float) i, 0.92f, 0.2f});
    }

    void initMap() {
        this.A = true;
        for (int i = 0; i < this.presenter.getColumnCount(); i++) {
            for (int j = 0; j < this.presenter.getRowCount(); j++) {
                this.curNodes.add(createNode(i, j, this.e, this.f));
            }
        }
        ValueAnimator duration = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(300);
//        duration.addUpdateListener(GameSceneView$$Lambda$5.lambdaFactory$(this));
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                b(animation);
            }
        });
        duration.addListener(new AnimatorListenerAdapter() {

            public void onAnimationStart(Animator animator) {
                GameSceneView.this.soundManager.h();
            }

            public void onAnimationEnd(Animator animator) {
                GameSceneView.this.nodes.clear();
                GameSceneView.this.nodes.addAll(GameSceneView.this.curNodes);
                GameSceneView.this.curNodes.clear();
                GameSceneView.this.B = true;
            }
        });
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(300);
        valueAnimator.setObjectValues(new Object[]{Integer.valueOf(this.a), Integer.valueOf(this.d)});
//        valueAnimator.addUpdateListener(GameSceneView$$Lambda$6.lambdaFactory$(this));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setBackgroundColor(animation);
            }
        });
        valueAnimator.setEvaluator(argbEvaluator);

        duration.start();
        valueAnimator.start();
    }

    private /* synthetic */ void b(ValueAnimator valueAnimator) {
        int animatedFraction = (int) (valueAnimator.getAnimatedFraction() * 255.0f);
            for (Node node : this.nodes) {
                node.alpha = 255 - animatedFraction;
            }
            for (Node node2 : this.curNodes) {
                node2.alpha = animatedFraction;
        }
        invalidate();
    }

    private /* synthetic */ void setBackgroundColor(ValueAnimator valueAnimator) {
        setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    private Node createNode(int i, int i2, int i3, int i4) {
        Node node = new Node();
        node.image = createBitmap(this.presenter.getCellOpenSides(i, i2), i3, i4);
        node.rect = new Rect((int) (this.q + ((float) (this.p * i))), (int) (this.r + ((float) (this.p * i2))), (int) ((this.q + ((float) (this.p * i))) + ((float) this.p)), (int) ((this.r + ((float) (this.p * i2))) + ((float) this.p)));
        node.rect.set(node.rect);
        node.rotation = (float) (this.presenter.getCellRotation(i, i2).ordinal() * 90);
        node.x = i;
        node.y = i2;
        return node;
    }

    private Bitmap createBitmap(Set<GameBoard.Edge> set, int i, int i2) {
        int size = set.size();
        boolean contains = set.contains(GameBoard.Edge.TOP);
        boolean contains2 = set.contains(GameBoard.Edge.BOTTOM);
        boolean contains3 = set.contains(GameBoard.Edge.LEFT);
        boolean contains4 = set.contains(GameBoard.Edge.RIGHT);
        Bitmap createBitmap = Bitmap.createBitmap(this.p, this.p, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        int i3 = (int) (((float) this.p) * 0.2f);
        int i4 = (int) (((float) this.p) * 0.125f);
        Path path = new Path();
        path.addArc(new RectF((float) ((-this.p) / 2), (float) ((-this.p) / 2), (float) (this.p / 2), (float) (this.p / 2)), 0.0f, 90.0f);
        Path path2 = new Path();
        path2.addArc(new RectF((float) (this.p / 2), (float) ((-this.p) / 2), ((float) this.p) * 1.5f, (float) (this.p / 2)), 90.0f, 90.0f);
        Path path3 = new Path();
        path3.addArc(new RectF((float) (this.p / 2), (float) (this.p / 2), ((float) this.p) * 1.5f, ((float) this.p) * 1.5f), 180.0f, 90.0f);
        Path path4 = new Path();
        path4.addArc(new RectF((float) ((-this.p) / 2), (float) (this.p / 2), (float) (this.p / 2), ((float) this.p) * 1.5f), 270.0f, 90.0f);
        if (size == 1) {
            path2 = new Path();
            if (contains) {
                path2.moveTo((float) (this.p / 2), 0.0f);
                path2.lineTo((float) (this.p / 2), (float) (this.p / 4));
            }
            if (contains4) {
                path2.moveTo((float) this.p, (float) (this.p / 2));
                path2.lineTo((float) (this.p - (this.p / 4)), (float) (this.p / 2));
            }
            if (contains2) {
                path2.moveTo((float) (this.p / 2), (float) this.p);
                path2.lineTo((float) (this.p / 2), (float) (this.p - (this.p / 4)));
            }
            if (contains3) {
                path2.moveTo(0.0f, (float) (this.p / 2));
                path2.lineTo((float) (this.p / 4), (float) (this.p / 2));
            }
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path2, this.k);
            path3 = new Path();
            path3.addOval(new RectF((float) (this.p / 4), (float) (this.p / 4), (float) ((this.p / 4) * 3), (float) ((this.p / 4) * 3)), Path.Direction.CW);
            canvas.drawPath(path3, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path3, this.k);
            canvas.drawPath(path2, this.k);
        } else if (size == 2) {
            Object obj = (contains && contains2) ? 1 : null;
            Object obj2 = (contains3 && contains4) ? 1 : null;
            if (obj == null && obj2 == null) {
                if (contains) {
                    if (contains3) {
                        path3 = path;
                    } else {
                        path3 = null;
                    }
                    if (!contains4) {
                        path2 = path3;
                    }
                } else if (contains2) {
                    if (contains3) {
                        path2 = path4;
                    } else {
                        path2 = null;
                    }
                    if (contains4) {
                        path2 = path3;
                    }
                } else {
                    path2 = null;
                }
                this.k.setColor(i);
                this.k.setStrokeWidth((float) i3);
                canvas.drawPath(path2, this.k);
                this.k.setColor(i2);
                this.k.setStrokeWidth((float) i4);
                canvas.drawPath(path2, this.k);
            } else {
                path2 = new Path();
                if (obj != null) {
                    path2.moveTo((float) (this.p / 2), 0.0f);
                    path2.lineTo((float) (this.p / 2), (float) this.p);
                } else {
                    path2.moveTo(0.0f, (float) (this.p / 2));
                    path2.lineTo((float) this.p, (float) (this.p / 2));
                }
                this.k.setColor(i);
                this.k.setStrokeWidth((float) i3);
                canvas.drawPath(path2, this.k);
                this.k.setColor(i2);
                this.k.setStrokeWidth((float) i4);
                canvas.drawPath(path2, this.k);
            }
        } else if (size == 3) {
            if (!contains) {
                path2 = path4;
            } else if (!contains4) {
                path2 = path;
                path3 = path4;
            } else if (!contains2) {
                path3 = path;
            } else if (contains3) {
                path2 = null;
                path3 = null;
            } else {
                Path path5 = path3;
                path3 = path2;
                path2 = path5;
            }
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path3, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path3, this.k);
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path2, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path2, this.k);
        } else if (size == 4) {
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path2, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path2, this.k);
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path3, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path3, this.k);
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path4, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path4, this.k);
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path, this.k);
            canvas.clipRect((float) (this.p / 4), 0.0f, (float) ((this.p / 4) * 3), ((float) this.p) * 0.375f);
            this.k.setColor(i);
            this.k.setStrokeWidth((float) i3);
            canvas.drawPath(path2, this.k);
            this.k.setColor(i2);
            this.k.setStrokeWidth((float) i4);
            canvas.drawPath(path2, this.k);
        }
        return createBitmap;
    }
}