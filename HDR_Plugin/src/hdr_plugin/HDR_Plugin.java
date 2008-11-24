package hdr_plugin;

import ij.*;
import java.awt.*;
import ij.plugin.*;
import org.apache.commons.math.util.MathUtils;


public class HDR_Plugin implements PlugIn {

    public void run(String arg) {
        
        MathUtils.cosh(2.0);

        if (arg.equals("about1") || arg.equals("about2")) {
            showAbout();
            return;
        }

        int[] imgList = WindowManager.getIDList();

        if (imgList == null) {
            IJ.noImage();
            return;
        }

        if (arg.equals("stack")) {
            HDRStackBuilderFrame gui = new HDRStackBuilderFrame();
            gui.setVisible(true);
        }
        
        if (arg.equals("create")) {
            HDRResponseFunctionCalculatorFrame gui = new HDRResponseFunctionCalculatorFrame();
            gui.setVisible(true);            
        }
    }

    void showAbout() {
        IJ.showMessage("About Inverter_...",
                "This sample plugin filter inverts 8-bit images. Look\n" +
                "at the 'Inverter_.java' source file to see how easy it is\n" +
                "in ImageJ to process non-rectangular ROIs, to process\n" +
                "all the slices in a stack, and to display an About box.");
    }

    private Panel createExplanationPanel() {
        Panel panelExplanation = new Panel();
        Label labelExplanation = new Label();
        labelExplanation.setText("Select the desired stacks or images for building a new stack for HDR creation.\n" +
                "Sort the selected items in ascending order regarding their exposure values.\n" +
                "The new stack will be created in a new window and can be used for creating HDR images.");
        panelExplanation.add(labelExplanation);
        return panelExplanation;
    }
}
