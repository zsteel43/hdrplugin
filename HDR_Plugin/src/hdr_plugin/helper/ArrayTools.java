/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.helper;

/**
 *
 * @author Alex
 */
public class ArrayTools {
    public static double[][] subarray2D(double[][] in, int lb1, int ub1, int lb2, int ub2) {
        double out[][] = new double[ub1 - lb1 + 1][ub2 - lb2 + 1];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[i].length; j++) {
                out[i][j] = in[lb1 + i][lb2 + j];
            }
        }
        return out;
    }

    public static double[] subarray1D(double[] in, int lb1, int ub1) {
        double out[] = new double[ub1 - lb1 + 1];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[lb1 + i];
        }
        return out;
    }

    static int rank(double[] in, int m, int n) {
        double eps = Math.pow(2.0, -52.0);
        double tol = Math.max(m, n) * in[0] * eps;
        int r = 0;
        for (int i = 0; i < in.length; i++) {
            if (in[i] > tol) {
                r++;
            }
        }
        return r;
    }
}
