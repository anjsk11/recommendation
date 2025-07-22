package com.sensingbros.recommendation.util;

public class HeatmapUtils {
    private static final double MIN_LAT = 0.0;
    private static final double MAX_LAT = 30.0;
    private static final double MIN_LNG = 0.0;
    private static final double MAX_LNG = 30.0;
    private static final int GRID_SIZE = 30;

    public static int[] getGridIndex(double lat, double lng) {
        int row = (int) ((lat - MIN_LAT) / (MAX_LAT - MIN_LAT) * GRID_SIZE);
        int col = (int) ((lng - MIN_LNG) / (MAX_LNG - MIN_LNG) * GRID_SIZE);

        row = Math.max(0, Math.min(GRID_SIZE - 1, row));
        col = Math.max(0, Math.min(GRID_SIZE - 1, col));
        return new int[]{row, col};
    }
}