package Proj3;
import java.util.Collections;

import java.util.stream.IntStream;

public class GaussianEliminationParallel {

    public static double det = 1.0;
    public static int k;

    public static double solve(double[][] a, double[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("Invalid dimensions");
        }

        int n = b.length, p = b[0].length;
        if (a.length != n || a[0].length != n) {
            throw new IllegalArgumentException("Invalid dimensions");
        }

        IntStream.range(0, n - 1).forEach(i -> {

            k = i;

            IntStream.range(i + 1, n).forEach(j -> {
                if (Math.abs(a[j][i]) > Math.abs(a[k][i])) {
                    k = j;
                }
            });

            IntStream.range(i + 1, n).forEach(j -> {
                if (Math.abs(a[j][i]) > Math.abs(a[k][i])) {
                    k = j;
                }
            });

            if (k != i) {
                det = -det;

                IntStream.range(i, n).parallel().forEach(j -> {
                        double s = a[i][j];
                        a[i][j] = a[k][j];
                        a[k][j] = s;
                    // });
                });


                IntStream.range(0, p).parallel().forEach(j -> {

                    double s = b[i][j];
                    b[i][j] = b[k][j];
                    b[k][j] = s;

                });



            }

            IntStream.range(i + 1, n).parallel().forEach(j -> {
                double s = a[j][i] / a[i][i];

                IntStream.range(i + 1, n).forEach(k -> {
                    a[j][k] -= s * a[i][k];
                });

                IntStream.range(0, p).forEach(k -> {
                    b[j][k] -= s * b[i][k];
                });
            });

        });

        IntStream.rangeClosed(0, n - 1).boxed().sorted(Collections.reverseOrder()).forEach(i -> {

            IntStream.range(i + 1, n).parallel().forEach(j -> {
                double s = a[i][j];

                IntStream.range(0, p).forEach(k -> {
                    b[i][k] -= s * b[j][k];

                });

            });

            double s = a[i][i];
            det *= s;

            IntStream.range(0, p).forEach(k -> {
                b[i][k] /= s;
            });

        });

        return det;
    }

    public static void main(String[] args) {

        int size = 1000;
        double[][] a = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < j) {
                    a[i][j] = 0;
                } else {
                    a[i][j] = 1.1;
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

        System.out.println(duration + " nanoseconds (parallel)");
    }

}
