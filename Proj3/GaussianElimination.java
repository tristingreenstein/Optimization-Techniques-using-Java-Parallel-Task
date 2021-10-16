package Proj3;



public class GaussianElimination {
    public static double solve(double[][] a, double[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("Invalid dimensions");
        }

        int n = b.length, p = b[0].length;
        if (a.length != n || a[0].length != n) {
            throw new IllegalArgumentException("Invalid dimensions");
        }

        double det = 1.0;

        for (int i = 0; i < n - 1; i++) {
            int k = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[k][i])) {
                    k = j;
                }
            }

            if (k != i) {
                det = -det;

                for (int j = i; j < n; j++) {
                    double s = a[i][j];
                    a[i][j] = a[k][j];
                    a[k][j] = s;
                }

                for (int j = 0; j < p; j++) {
                    double s = b[i][j];
                    b[i][j] = b[k][j];
                    b[k][j] = s;
                }
            }

            for (int j = i + 1; j < n; j++) {
                double s = a[j][i] / a[i][i];
                for (k = i + 1; k < n; k++) {
                    a[j][k] -= s * a[i][k];
                }

                for (k = 0; k < p; k++) {
                    b[j][k] -= s * b[i][k];
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                double s = a[i][j];
                for (int k = 0; k < p; k++) {
                    b[i][k] -= s * b[j][k];
                }
            }
            double s = a[i][i];
            det *= s;
            for (int k = 0; k < p; k++) {
                b[i][k] /= s;
            }
        }

        return det;
    }

    public static void main(String[] args) {
        int size = 100;
        double[][] a = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < j) {
                    a[i][j] = 0;
                } else {
                    a[i][j] = 45;
                }
            }
        }
        double[][] b = new double[size][1];

        for (int i = 0; i < size; i++) {
            b[i][0] = (double) i / (double) i + 1;
        }

        long startTime = System.nanoTime();

        System.out.println("det: " + solve(a, b));

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        System.out.println(duration + " milliseconds (not parallel)");
    }
}
