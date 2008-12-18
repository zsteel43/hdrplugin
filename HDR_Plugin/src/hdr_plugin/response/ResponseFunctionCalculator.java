/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.response;

import hdr_plugin.Exceptions.TypeNotSupportedException;
import hdr_plugin.response.weight.WeightFunction;

/**
 *
 * @author Alexander Heidrich
 */
public interface ResponseFunctionCalculator {

    public double[] calcResponse(int channel, int lambda) throws TypeNotSupportedException;

    public WeightFunction getW();

    public ResponseFunctionCalculatorSettings getResponseFunctionCalculatorSettings();

    public String getAlgorithm();

    public String getAlgorithmReference();
}
