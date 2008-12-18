/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.response.mitsunaga;

import hdr_plugin.response.*;
import hdr_plugin.response.weight.WeightFunction;
import hdr_plugin.response.weight.SimpleWeightFunction;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public class MitsunagaCalculator implements ResponseFunctionCalculator {

    private int[][][] imgPixelsZ;
    private ResponseFunctionCalculatorSettings settings;
    private WeightFunction w;

    public MitsunagaCalculator(int[][][] imgPixelsZ, ResponseFunctionCalculatorSettings settings) {
        this.imgPixelsZ = imgPixelsZ;
        this.settings = settings;
        w = new SimpleWeightFunction(settings.getZmin(), settings.getZmax());
    }

    public double[] calcResponse(int channel, int lambda) {
        int Q = settings.getNoOfImages();
        int N = 3;
        double[][] c = new double[N][25];
        double[] b = new double[N];
        double Imax = 1.0;
        double[] coef = new double[20];
        for (int i = 0; i < (Q - 1); i++) { // for all images
            double R = settings.getExpTimes()[i] / settings.getExpTimes()[i + 1];
            for (int j = 0; j < imgPixelsZ[channel].length; j++) { // for pixels
                double M1 = imgPixelsZ[channel][j][i];
                double M2 = imgPixelsZ[channel][j][i + 1];
                double an = Math.pow(M1, N) - R * Math.pow(M2, N);
                for (int e = 0; e < N; e++) {
                    double aj = Math.pow(M1, e) - R * Math.pow(M2, e);
                    for (int m = 0; m < N; m++) {
                        double ai = Math.pow(M1, m) - R * Math.pow(M2, m);
                        c[e][m] += ((ai - an) * (aj - an));
                    }
                    b[e] += (-an * Imax * (aj - an));
                }
            }
        }
        Matrix A = new DenseMatrix(c);
        Vector B = new DenseVector(b);
        Vector X = new DenseVector(A.numColumns());
        A.solve(B, X);
        DenseVector ax = new DenseVector(X);
        double[] result = ax.getData();

        double cn;
        double sum = 0;
        for (int e = 0; e < N; e++) {
            sum += result[e];
        }
        cn = 1.0 - sum;

        coef[0] = cn;

        for (int i = (N - 1); i >= 0; i--) {
            coef[N - i] = result[i];
        }

        for (int q = 0; q < coef.length; q++) {
            System.out.println(coef[q]);
        }

        double Iresp[] = new double[settings.getZmax() - settings.getZmin() + 1];

        for (int m = 0; m < settings.getZmax() - settings.getZmin() + 1; m++) {

            double sum_ = 0;

            for (int r = 0; r < (N + 1); r++) {
                sum_ += (Math.pow((double) m, (N - r)) * coef[r]);
            //sum_ += (Math.pow((double) m, (N - r)) * coef[r]);
            }
            Iresp[m] = sum_;
        }

        return Iresp;
    }

    public WeightFunction getW() {
        return w;
    }

    public ResponseFunctionCalculatorSettings getResponseFunctionCalculatorSettings() {
        return settings;
    }

    public String getAlgorithm() {
        return "Mitsunaga";
    }

    public String getAlgorithmReference() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
