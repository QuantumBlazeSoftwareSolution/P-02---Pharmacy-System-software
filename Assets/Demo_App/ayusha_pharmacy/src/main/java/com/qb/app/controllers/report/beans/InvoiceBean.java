
package com.qb.app.controllers.report.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ravis
 */
public class InvoiceBean {

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<InvoiceItemsBean> getInvoiceItemList() {
        return invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceItemsBean> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Map<String, Object> getSub_report_params() {
        return sub_report_params;
    }

    public void setSub_report_params(Map<String, Object> sub_report_params) {
        this.sub_report_params = sub_report_params;
    }
    
    private String invoiceNumber;
    private List<InvoiceItemsBean> invoiceItemList;
    private String dateTime;
    private String invoiceAmount;
    private String paidAmount;
    private String balanceAmount;
    private Map<String, Object> sub_report_params;

    public InvoiceBean(
            String invNumb, List<InvoiceItemsBean> invoiceItems, 
            String invdateTime, String invAmount, String invPaid,String invBalance,String TQ, String TD,
            String TA,String TP
    ) {
        this.invoiceNumber =invNumb ;
        this.invoiceItemList =invoiceItems ;
        this.dateTime =invdateTime ;
        this.invoiceAmount =invAmount ;
        this.paidAmount = invPaid;
        this.balanceAmount =invBalance ;
        


        Map<String, Object> params = new HashMap<>();
        params.put("TotalQuantity", TQ);
        params.put("TotalDiscount", TD);
        params.put("TotalAmount", TA);
        params.put("TotalProfit", TP);

        setSub_report_params(params);
    }
    
}
