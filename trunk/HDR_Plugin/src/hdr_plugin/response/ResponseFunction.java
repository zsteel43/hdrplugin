/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.response;

import hdr_plugin.response.weight.WeightFunction;
import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class ResponseFunction implements Serializable {
    private double[][] g;
    private int type;
    private WeightFunction w;

    public ResponseFunction(double[][] g, int type, WeightFunction w) {
        this.g = g;
        this.type = type;
        this.w = w;
    }

    /**
     * @return the g
     */
    public double[][] getG() {
        return g;
    }

    public int getType() {
        return type;
    }

    /**
     * @return the w
     */
    public WeightFunction getW() {
        return w;
    }

}
