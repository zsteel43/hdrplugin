/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration;

import flanagan.math.Matrix;
import hdr_plugin.helper.ImageJTools;
import java.io.Serializable;

/**
 *
 * @author Alexander Heidrich
 */
public class DebevecCalculator implements Serializable, ResponseFunctionCalculator {

    private int Zmin;
    private int Zmax;
    private int[][][] imgPixelsZ;
    private int noOfImagesQ;
    private int noOfPixelsP;
    private int arrayWidth;
    private int arrayHeight;
    private double[] shutterSpeeds;

    public DebevecCalculator(int[][][] imgPixelsZ, int arrayWidth, int arrayHeight, int noOfImagesQ, int noOfPixelsP, double[] shutterSpeeds, int Zmin, int Zmax) {
        this.imgPixelsZ = imgPixelsZ;
        this.noOfImagesQ = noOfImagesQ;
        this.noOfPixelsP = noOfPixelsP;
        this.shutterSpeeds = shutterSpeeds;
        this.Zmax = Zmax;
        this.Zmax = Zmin;
    }

    public DebevecCalculator() {
        
    }

    private double w(int z) {
        if (z <= 0.5 * (Zmin + Zmax)) {
            return z - Zmin;
        } else {
            return Zmax - z;
        }
    }

    public void doIt(int[][] Z) {
        int n = Zmax-Zmin + 1;
        int k = 0;
        double lambda = 10;

        double[][] a = new double[getNoOfPixelsP() * noOfImagesQ + n - 1][n + getNoOfPixelsP()];
        double[] b = new double[a.length];

        for (int i = 0; i < Z.length; i++) {            // for all pixels
            for (int j = 0; j < noOfImagesQ; j++) {     // for all images
                int value = Z[i][j];
                double wij = w(value);
                if (wij == 0.) {
                    continue;
                }
                a[k][value] = wij;
                a[k][n + i] = -wij;
                b[k] = wij * Math.log(shutterSpeeds[j]);
                k++;
            }
        }
        a[k][128] = 1.0;
        k++;

        for (int i = 0; i < n - 2; i++) {
            a[k][i] = lambda * w(i + 1);
            a[k][i + 1] = -2.0 * lambda * w(i + 1);
            a[k][i + 2] = lambda * w(i + 1);
            k++;
        }

        if (k < getNoOfPixelsP() * noOfImagesQ + n - 1) {
            double[][] at = new double[k][n + getNoOfPixelsP()];
            double[] bt = new double[k];
            at = ImageJTools.subarray2D(a, 0, k - 1, 0, n + getNoOfPixelsP() - 1);
            bt = ImageJTools.subarray1D(b, 0, k - 1);
            a = at;
            b = bt;
        }

        Matrix A = new Matrix(a);
        double[] x = A.solveLinearSet(b);

        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
    }

    /**
     * @return the noOfPixelsP
     */
    public int getNoOfPixelsP() {
        return noOfPixelsP;
    }
}
