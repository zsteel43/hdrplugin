/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.response.weight;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public class GaussianWeightFunction implements WeightFunction {

    private int Zmin;
    private int Zmax;
    private double sigma;
    private static double MIN_WEIGHT = 1e-3;

    public GaussianWeightFunction(int Zmin, int Zmax, double sigma) {
        this.Zmin = Zmin;
        this.Zmax = Zmax;
        this.sigma = sigma;
    }

    public double w(int value) {
        double mid = Zmin + (Zmax - Zmin) / 2.0f - 0.5f;
        double mid2 = (mid - Zmin) * (mid - Zmin);
        if (value < Zmin || value > Zmax) {
            return 0.0;
        } else {
            double weight = Math.exp(-sigma * (value - mid) * (value - mid) / mid2);
            if (weight < MIN_WEIGHT) {
                return 0.;
            } else {
                return weight;
            }
        }
    }
}

