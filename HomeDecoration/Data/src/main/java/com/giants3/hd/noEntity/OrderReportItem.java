package com.giants3.hd.noEntity;

/**订单报表项目数据
 * Created by davidleen29 on 2016/8/9.
 */
public class OrderReportItem  {

    public String os_no;
    public String cus_no;
    public String prd_no;
    public String thumbnail;
    public String url;

    public String saleName;
    /**
     * 客号
     */
    public String cus_prd_no;
    public String unit;
    public int qty;
    public String  verifyDate;
    /**
     *  装柜日期
     */
    public String  sendDate;

    public String id_no;

    public OrderReportItem() {
    }


    public OrderReportItem(Object sqlListResult)
    {

        if(sqlListResult!=null&&sqlListResult instanceof Object[])
        {
            Object[] coloumValues= (Object[]) sqlListResult;
            os_no=String.valueOf(coloumValues[0]);
            cus_no=String.valueOf(coloumValues[1]);
            prd_no=String.valueOf(coloumValues[2]);
            saleName=String.valueOf(coloumValues[3]);
            cus_prd_no=String.valueOf(coloumValues[4]);
            unit=String.valueOf(coloumValues[5]);

            try {
                qty=Integer.valueOf(coloumValues[6].toString());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            verifyDate=String.valueOf(coloumValues[7]);
            sendDate=String.valueOf(coloumValues[8]);
            id_no=String.valueOf(coloumValues[9]);




        }





    }

    public String getOs_no() {
        return os_no;
    }

    public void setOs_no(String os_no) {
        this.os_no = os_no;
    }

    public String getCus_no() {
        return cus_no;
    }

    public void setCus_no(String cus_no) {
        this.cus_no = cus_no;
    }

    public String getPrd_no() {
        return prd_no;
    }

    public void setPrd_no(String prd_no) {
        this.prd_no = prd_no;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getCus_prd_no() {
        return cus_prd_no;
    }

    public void setCus_prd_no(String cus_prd_no) {
        this.cus_prd_no = cus_prd_no;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(String verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }
}
