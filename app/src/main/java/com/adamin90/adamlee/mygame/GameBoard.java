package com.adamin90.adamlee.mygame;

import com.adamin90.adamlee.mygame.data.Level;
import com.adamin90.adamlee.mygame.util.IAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Administrator on 2015/8/21.
 */
public class GameBoard {
    final int width;
    final int height;
    GameBoardCell[][] cells;
    private final PMRandom pmRandom;

    enum Edge {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT;

        static final Edge[] edges;

        static {
            edges = values();
        }

        Edge next() {
            return get(ordinal() + 1);
        }

        Edge reverse() {
            return get(ordinal() + 2);
        }

        Edge next(Edge edge) {
            return get((ordinal() - edge.ordinal()) + edges.length);
        }

        static Edge get(int i) {
            return edges[i % 4];
        }
    }

    GameBoard(Level level) {
        this.pmRandom = PMRandom.getInstance();
        this.width = level.width;
        this.height = level.height;
        this.cells = new GameBoardCell[width][height];
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Set hashSet = new HashSet();
                List<Integer> list = level.tiles.get(j).get(i);
                if (list.get(0) > 0) {
                    hashSet.add(Edge.TOP);
                }
                if (list.get(1) > 0) {
                    hashSet.add(Edge.RIGHT);
                }
                if (list.get(2) > 0) {
                    hashSet.add(Edge.BOTTOM);
                }
                if (list.get(3) > 0) {
                    hashSet.add(Edge.LEFT);
                }
                this.cells[i][j] = new GameBoardCell(i, j, this, hashSet, Edge.get(list.get(4)));
            }
        }
    }

    GameBoard(int width, int height, float f) {
        this.pmRandom = PMRandom.getInstance();
        this.width = width;
        this.height = height;
        this.cells =  new GameBoardCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.cells[i][j] = new GameBoardCell(i, j, this);
            }
        }
        if (((double) this.pmRandom.rand()) > 0.9d || width == height) {
            a(f);
        } else {
            b(f);
        }
    }

    boolean allConnected() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (!this.cells[i][j].allConnected()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void a(final float f) {
//        width(GameBoard$$Lambda$1.width(this, ((double) this.pmRandom.height()) > 0.7d, isGameEnd));
        a(new IAction<GameBoardCell>() {
            @Override
            public void call(GameBoardCell gameBoardCell) {
                a(((double) pmRandom.rand())>0.7d,f,gameBoardCell);
            }
        });
    }

    private /* synthetic */ void a(boolean z, float f, GameBoardCell gameBoardCell) {
        boolean add = true;
        if (((double) gameBoardCell.x) <= Math.ceil((double) (((float) this.width) - 1.0f)) / 2.0d) {
            if (((double) gameBoardCell.y) <= Math.ceil((double) (((float) this.height) - 1.0f)) / 2.0d || !z) {
                GameBoardCell gameBoardCell2 = this.cells[(this.width - 1) - gameBoardCell.x][gameBoardCell.y];
                GameBoardCell gameBoardCell3 = this.cells[gameBoardCell.x][(this.height - 1) - gameBoardCell.y];
                GameBoardCell gameBoardCell4 = this.cells[(this.width - 1) - gameBoardCell.x][(this.height - 1) - gameBoardCell.y];
                if (!gameBoardCell.allEdges.contains(Edge.TOP)) {
                    boolean z3 = this.pmRandom.rand() > f;
                    gameBoardCell.modifyEdge(Edge.TOP, z3);
                    gameBoardCell2.modifyEdge(Edge.TOP, z3);
                    if (z) {
                        gameBoardCell3.modifyEdge(Edge.BOTTOM, z3);
                        gameBoardCell4.modifyEdge(Edge.BOTTOM, z3);
                    }
                }
                if (!gameBoardCell.allEdges.contains(Edge.RIGHT)) {
                    if (this.pmRandom.rand() <= f) {
                        add = false;
                    }
                    gameBoardCell.modifyEdge(Edge.RIGHT, add);
                    gameBoardCell2.modifyEdge(Edge.LEFT, add);
                    if (z) {
                        gameBoardCell3.modifyEdge(Edge.RIGHT, add);
                        gameBoardCell4.modifyEdge(Edge.LEFT, add);
                    }
                }
            }
        }
    }

    private void b(final float f) {
//        width(GameBoard$$Lambda$2.width(this, isGameEnd));
        a(new IAction<GameBoardCell>() {
            @Override
            public void call(GameBoardCell gameBoardCell) {
                modifyCellEdge(f,gameBoardCell);
            }
        });
        b();
        c();
    }

    private /* synthetic */ void modifyCellEdge(float f, GameBoardCell gameBoardCell) {
        boolean add = true;
        if (!gameBoardCell.allEdges.contains(Edge.TOP)) {
            gameBoardCell.modifyEdge(Edge.TOP, this.pmRandom.rand() > f);
        }
        if (!gameBoardCell.allEdges.contains(Edge.RIGHT)) {
            Edge edge = Edge.RIGHT;
            if (this.pmRandom.rand() <= f) {
                add = false;
            }
            gameBoardCell.modifyEdge(edge, add);
        }
    }

    private void b() {
//        width(GameBoard$$Lambda$3.width(this));
        a(new IAction<GameBoardCell>() {
            @Override
            public void call(GameBoardCell gameBoardCell) {
                b(gameBoardCell);
            }
        });
    }

    private /* synthetic */ void b(GameBoardCell gameBoardCell) {
        if (gameBoardCell.count() <= 1) {
            for (Edge edge : Edge.values()) {
                GameBoardCell b = gameBoardCell.border(edge);
                if (!gameBoardCell.edges.contains(edge) && b != null && b.count() == 1 && this.pmRandom.rand() > 0.85f) {
                    b.edges.add(edge.reverse());
                    gameBoardCell.edges.add(edge);
                }
            }
        }
    }

    private void c() {
//        width(GameBoard$$Lambda$4.width(this));
        a(new IAction<GameBoardCell>() {
            @Override
            public void call(GameBoardCell gameBoardCell) {
              a(gameBoardCell);
            }
        });
    }

    private /* synthetic */ void a(GameBoardCell gameBoardCell) {
        if (gameBoardCell.count() >= 4) {
            for (Edge edge : Edge.values()) {
                if (gameBoardCell.border(edge) != null && this.pmRandom.rand() > 0.95f) {
                    gameBoardCell.modifyEdge(edge, false);
                }
            }
        }
    }

    private void a(IAction<GameBoardCell> action1) {
        for (int i = 0; i < this.width; i++) {
            for (int i2 = 0; i2 < this.height; i2++) {
                action1.call(this.cells[i][i2]);
            }
        }
    }
}