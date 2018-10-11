package com.giants3.report.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Collection;

/**
 * Created by david on 2016/3/15.
 */
public class CustomBeanDataSource extends JRBeanCollectionDataSource {
    public CustomBeanDataSource(Collection beanCollection) {
        super(beanCollection);
    }

    public CustomBeanDataSource(Collection beanCollection, boolean isUseFieldDescription) {
        super(beanCollection, isUseFieldDescription);
    }

    @Override
    protected String getPropertyName(JRField field) {

        final String propertyName = super.getPropertyName(field);
        return propertyName;
    }


}
