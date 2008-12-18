/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.response.weight;

import hdr_plugin.response.*;

/**
 *
 * @author Alexander Heidrich
 */
public class SimpleWeightFunction implements WeightFunction{

    private int Zmin;
    private int Zmax;

    public SimpleWeightFunction(int Zmin, int Zmax) {
        this.Zmin = Zmin;
        this.Zmax = Zmax;
    }

    public double w(int z) {
        if (z <= 0.5 * (Zmin + Zmax)) {
            return z - Zmin;
        } else {
            return Zmax - z;
        }
    }

}
