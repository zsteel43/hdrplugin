/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration;

import ij.text.TextWindow;

/**
 *
 * @author Alexander Heidrich
 */
public class MitsunagaCalibrator {

    //private double error;
    //private double[] coeff;
    private double[] a;
    private double[] b;
    private double[] x;
    private int Q;
    private int maxDegree;
    private Object[] imgPixels;

    public MitsunagaCalibrator(int maxDegree, Object[] imgPixels, int Q) {
        this.maxDegree = maxDegree;
        this.a = new double[maxDegree * maxDegree];
        this.b = new double[maxDegree];
        this.x = new double[maxDegree];
        this.imgPixels = imgPixels;
        this.Q = Q;
    }

    private void gaussJordan(int dim) {
        int i, j, k, pivot = 0;
        int n1 = dim + 1;
        double m[] = new double[dim * (dim + 1)];
        for (i = 0; i < dim; i++) {
            int in, in1;

            in = i * dim;
            in1 = i * n1;
            for (j = 0; j < dim; j++) {
                m[in1 + j] = a[in + j];
            }
            m[in1 + dim] = b[i];
        }

        for (k = 0; k < dim; k++) {
            int kn1 = k * n1;

            double pvval = 0.;
            for (i = k; i < dim; i++) {
                if (Math.abs(m[i * n1 + k]) > Math.abs(pvval)) {
                    pivot = i;
                    pvval = m[i * n1 + k];
                }
            }
            int pn1 = pivot * n1;
            for (j = 0; j < n1; j++) {
                double t = m[pn1 + j];
                m[pn1 + j] = m[kn1 + j];
                m[kn1 + j] = t;
            }

            for (j = k; j < n1; j++) {
                m[kn1 + j] /= pvval;
            }
            for (i = 0; i < dim; i++) {
                if (i != k) {
                    int in1 = i * n1;
                    pvval = m[in1 + k];
                    for (j = k; j < n1; j++) {
                        m[in1 + j] -= m[kn1 + j] * pvval;
                    }
                }
            }
        }

        for (i = 0; i < dim; i++) {
            x[i] = m[i * n1 + dim];
        }
    }

    private void makeMatrix(int od) {
        int dim = od;
        double initR = 0.5;

        int idxN = dim;

        // mfMakeMatrix
        int numR = Q;
        for (int i = 0; i < dim; i++) {
            int idxI = i;
            for (int j = 0; j < dim; j++) {
                int idxJ = j;
                double s = 0.;
                for (int r = 0; r < numR - 1; r++) { // all images
                    int numdata = ((short[]) imgPixels[r]).length;
                    for (int n = 0; n < numdata - 1; n++) { // all pixels
                        int pix1 = ((short[]) imgPixels[r])[n] & 0xffff;
                        int pix2 = ((short[]) imgPixels[r + 1])[n] & 0xffff;
                        // mfCalcBase
                        double bi = calcBase(idxI, pix1, pix2, initR);
                        double bj = calcBase(idxJ, pix1, pix2, initR);
                        double bn = calcBase(idxN, pix1, pix2, initR);
                        s += (bi - bn) * (bj - bn);
                    }
                }
                a[i * dim + j] = s;
            }

            double z = 0.;
            for (int r = 0; r < numR - 1; r++) {
                int numdata = ((short[]) imgPixels[r]).length;
                for (int n = 0; n < numdata - 1; n++) {
                    int pix1 = ((short[]) imgPixels[r])[n] & 0xffff;
                    int pix2 = ((short[]) imgPixels[r + 1])[n] & 0xffff;
                    double bi = calcBase(idxI, pix1, pix2, initR);
                    double bn = calcBase(idxN, pix1, pix2, initR);
                    z += (bi - bn) * bn;
                }
            }
            b[i] = -z;
        }

    }

    private void estimateFunc() {
        double minErr = 1.0e12;
        double[] minCoeff = new double[maxDegree+1];
        new TextWindow("huhu", "estimate", 400, 400).setVisible(true);
        for (int od = 1; od <= maxDegree; od++) {
            double coeff[] = new double[od + 1];
            coeff = updCoeff(od);
            //checkIncreasing(coeff);
            double terr = evaluateError(coeff);
            if (terr < minErr) {
                minErr = terr;
                minCoeff = coeff;
            }
        }
        System.out.println(maxDegree);
        System.out.println(minCoeff.length);
        for (int i = 0; i < minCoeff.length; i++) {
            System.out.println(minCoeff[i] + " ");
        }            
    }

    private double[] updCoeff(int od) {
        makeMatrix(od);
        gaussJordan(od);
        double coeff[] = new double[od + 1];
        coeff[od] = 1.;
        for (int i = 0; i < od; i++) {
            coeff[i] = x[i];
            coeff[od] -= x[i];
        }
        return coeff;
    }

    private boolean checkSimpleIncreasing() {
        return true;
    }

    private double evaluateError(double[] coeff) {
        double err = 0.;
        int numR = Q;
        int snum = 0;

        for (int r = 0; r < numR - 1; r++) {
            int numdata = ((short[]) imgPixels[r]).length;
            for (int i = 0; i < numdata-1; i++) {
                double e = 0.;
                int pix1 = ((short[]) imgPixels[r])[i] & 0xffff;
                int pix2 = ((short[]) imgPixels[r + 1])[i] & 0xffff;
                for (int j = 0; j < coeff.length; j++) {
                    e += coeff[j] * calcBase(j, pix1, pix2, 0.5);
                }
                err += Math.pow(e, 2);
            }
            snum += numdata;
        }
        return err /= snum;        
    }

    private double calcBase(int idx, double im0, double im1, double cR) {
        return Math.pow(im0, idx) - cR * Math.pow(im1, idx);
    }

    public void doIt() {
        estimateFunc();
    }
}
