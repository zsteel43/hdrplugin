/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.tonemapping;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public class SimpleToneMapper {
    public double[] map(double[] in) {
        double out[] = new double[in.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[i] / (in[i] + 1);
        }
        return out;
    }
}
