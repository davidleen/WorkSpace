package com.giants3.report.jasper;


import com.giants3.report.Reportable;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.Map;

/**
 * jasper 报表基类
 *
 * Created by davidleen29 on 2017/3/16.
 */
public  abstract  class JRPreviewReport extends JRReport {




    @Deprecated
    protected void reportJRType(JasperPrint   jp)
    {
        JasperViewer jasperViewer = new JasperViewer(jp,false);

        jasperViewer.setVisible(true);
    }


}
