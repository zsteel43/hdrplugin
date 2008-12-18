/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.helper;

/**
 *
 * @author Alexander Heidrich <alexander.heidrich@hki-jena.de>
 */
public class ResponseFunctionTools {

    public static double[] responseLinear(int M) {
        double out[] = new double[M];
        for (int i = 0; i < M; i++) {
            out[i] = (double) i / ((double) M - 1);
        }
        return out;
    }
}
