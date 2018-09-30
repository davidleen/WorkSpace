package com.giants3.report.jasper;

import com.giants3.report.JRReporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Created by davidleen29 on 2018/8/4.
 */
public     class JRPreviewReporter extends JRReporter {
    @Override
    public void doOutput(JasperPrint jp) {

        JasperViewer jasperViewer = new JasperViewer(jp,false);

        jasperViewer.setVisible(true);


    }
}
