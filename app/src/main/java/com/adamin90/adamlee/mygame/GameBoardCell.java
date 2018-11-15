package com.adamin90.adamlee.mygame;

//import com.google.android.gms.common.api.CommonStatusCodes;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/8/21.
 */
class GameBoardCell {
    final int x;
    final int y;
    GameBoard.Edge direct;
    final Set<GameBoard.Edge> edges;
    final Set<GameBoard.Edge> allEdges;
    private final GameBoard gameBoard;

    /* renamed from: com.balysv.loop.GameBoardCell.1 */
    /* synthetic */ static class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            a = new int[GameBoard.Edge.values().length];
            try {
                a[GameBoard.Edge.TOP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[GameBoard.Edge.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[GameBoard.Edge.BOTTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[GameBoard.Edge.LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    GameBoardCell(int x, int y, GameBoard gameBoard) {
        this.edges = new HashSet();
        this.allEdges = new HashSet();
        this.x = x;
        this.y = y;
        this.gameBoard = gameBoard;
        if (y == 0) {
            this.allEdges.add(GameBoard.Edge.TOP);
        }
        if (x == gameBoard.width - 1) {
            this.allEdges.add(GameBoard.Edge.RIGHT);
        }
        if (y == gameBoard.height - 1) {
            this.allEdges.add(GameBoard.Edge.BOTTOM);
        }
        if (x == 0) {
            this.allEdges.add(GameBoard.Edge.LEFT);
        }
        this.direct = GameBoard.Edge.get((int) (PMRandom.getInstance().calc() % 4));
    }

    GameBoardCell(int x, int y, GameBoard gameBoard, Set<GameBoard.Edge> set, GameBoard.Edge direct) {
        this.edges = new HashSet();
        this.allEdges = new HashSet();
        this.x = x;
        this.y = y;
        this.gameBoard = gameBoard;
        this.edges.addAll(set);
        this.allEdges.addAll(Arrays.asList(GameBoard.Edge.values()));
        this.direct = direct;
    }

    void next() {
        this.direct = this.direct.next();
    }

    int count() {
        return this.edges.size();
    }

    void modifyEdge(GameBoard.Edge edge, boolean add) {
        GameBoardCell border = border(edge);
        GameBoard.Edge reverse = edge.reverse();
        if (!this.allEdges.contains(edge) && !border.allEdges.contains(reverse)) {
            editEdge(edge, add);
            this.allEdges.add(edge);
            border.editEdge(reverse, add);
            border.allEdges.add(reverse);
        }
    }

    //是否和此方向连接
    boolean isConnect(GameBoard.Edge edge) {
        return this.edges.contains(edge.next(this.direct));
    }

    //根据方向获取相邻的
    GameBoardCell border(GameBoard.Edge edge) {
        int x = this.x;
        int y = this.y;
        switch (AnonymousClass1.a[edge.ordinal()]) {
            case 1 /*1*/:
                y--;
                break;
            case 2 /*2*/:
                x++;
                break;
            case 3 /*3*/:
                y++;
                break;
            case 4 /*4*/:
                x--;
                break;
        }
        if (x < 0 || x >= this.gameBoard.width || y < 0 || y >= this.gameBoard.height) {
            return null;
        }
        return this.gameBoard.cells[x][y];
    }

    boolean allConnected() {
        if (this.y > 0) {
            if (isConnect(GameBoard.Edge.TOP) != border(GameBoard.Edge.TOP).isConnect(GameBoard.Edge.BOTTOM)) {
                return false;
            }
        } else if (isConnect(GameBoard.Edge.TOP)) {
            return false;
        }
        if (this.x < this.gameBoard.width - 1) {
            if (isConnect(GameBoard.Edge.RIGHT) != border(GameBoard.Edge.RIGHT).isConnect(GameBoard.Edge.LEFT)) {
                return false;
            }
        } else if (isConnect(GameBoard.Edge.RIGHT)) {
            return false;
        }
        if (this.y < this.gameBoard.height - 1) {
            if (isConnect(GameBoard.Edge.BOTTOM) != border(GameBoard.Edge.BOTTOM).isConnect(GameBoard.Edge.TOP)) {
                return false;
            }
        } else if (isConnect(GameBoard.Edge.BOTTOM)) {
            return false;
        }
        if (this.x > 0) {
            if (isConnect(GameBoard.Edge.LEFT) != border(GameBoard.Edge.LEFT).isConnect(GameBoard.Edge.RIGHT)) {
                return false;
            }
        } else if (isConnect(GameBoard.Edge.LEFT)) {
            return false;
        }
        return true;
    }

    private void editEdge(GameBoard.Edge edge, boolean add) {
        if (add) {
            this.edges.add(edge);
        } else {
            this.edges.remove(edge);
        }
    }

}