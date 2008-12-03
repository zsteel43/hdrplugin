/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.response;

//import flanagan.math.Matrix;

import hdr_plugin.helper.ArrayTools;
import java.io.Serializable;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.AbstractIterationMonitor;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;

/**
 *
 * @author Alexander Heidrich
 */
public class DebevecCalculator implements Serializable, ResponseFunctionCalculator {

    private int[][][] imgPixelsZ;
    private ResponseFunctionCalculatorSettings settings;

    public DebevecCalculator(int[][][] imgPixelsZ, ResponseFunctionCalculatorSettings settings) {
        this.imgPixelsZ = imgPixelsZ;
        this.settings = settings;
    }

    private double w(int z) {
        if (z <= 0.5 * (settings.getZmin() + settings.getZmax())) {
            return z - settings.getZmin();
        } else {
            return settings.getZmax() - z;
        }
    }

    public double[] calcResponse(int channel) {
        int n = settings.getZmax() - settings.getZmin() + 1;
        int mid = n / 2;
        int k = 0;
        double lambda = 1;

        System.out.println("Debevec start");

        double[][] a = new double[settings.getNoOfPixelsN() * settings.getNoOfImagesP() + n - 1][n + settings.getNoOfPixelsN()];
        double[] b = new double[a.length];

        for (int i = 0; i < imgPixelsZ[channel].length; i++) {    // for all pixels
            for (int j = 0; j < settings.getNoOfImagesP(); j++) { // for all images
                int value = imgPixelsZ[channel][i][j];
                double wij = w(value);
                if (wij == 0.) {
                    continue;
                }
                a[k][value] = wij;
                a[k][n + i] = -wij;
                b[k] = wij * Math.log(settings.getExpTimes()[j]);
                k++;
            }
        }

        a[k][mid] = 1.0;
        k++;

        for (int i = 0; i < n - 2; i++) {
            a[k][i] = lambda * w(i + 1);
            a[k][i + 1] = -2.0 * lambda * w(i + 1);
            a[k][i + 2] = lambda * w(i + 1);
            k++;
        }

        if (k < settings.getNoOfPixelsN() * settings.getNoOfImagesP() + n - 1) {
            double[][] at = new double[k][n + settings.getNoOfPixelsN()];
            double[] bt = new double[k];
            at = ArrayTools.subarray2D(a, 0, k - 1, 0, n + settings.getNoOfPixelsN() - 1);
            bt = ArrayTools.subarray1D(b, 0, k - 1);
            a = at;
            b = bt;
        }

        System.out.println("Debevec end");
        
        Matrix A = new DenseMatrix(a);
        Vector B = new DenseVector(b);
        Vector X = new DenseVector(A.numColumns());        
        Vector x = A.solve(B, X);
        DenseVector ax = new DenseVector(x);
        System.out.println(ax.toString());
        double[] out = ax.getData();
        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }

        System.out.println("solve end");

        return null;// ArrayTools.subarray1D(x, 0, n-1);

    }

    public void saveResponse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ResponseFunctionCalculatorSettings getResponseFunctionCalculatorSettings() {
        return settings;
    }

    public String getAlgorithm() {
        return "Debevec";
    }

    public String getAlgorithmReference() {
        return " ";
    }

    public static class SimpleIterationMonitor extends AbstractIterationMonitor {

        private int max;

        private SimpleIterationMonitor(int i) {
            this.max = i;
        }

        @Override
        protected boolean convergedI(double arg0, Vector arg1) throws IterativeSolverNotConvergedException {
            return convergedI(arg0);
        }

        @Override
        protected boolean convergedI(double arg0) throws IterativeSolverNotConvergedException {
            return iter >= max;
        }

    }
}
