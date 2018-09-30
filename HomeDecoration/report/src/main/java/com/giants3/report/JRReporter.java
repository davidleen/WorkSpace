package com.giants3.report;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * Created by davidleen29 on 2018/8/4.
 */
public abstract  class JRReporter implements Reporter {





    public abstract void doOutput(JasperPrint jp)  ;
}
