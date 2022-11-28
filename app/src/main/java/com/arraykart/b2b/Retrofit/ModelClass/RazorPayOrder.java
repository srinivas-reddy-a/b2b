package com.arraykart.b2b.Retrofit.ModelClass;

public class RazorPayOrder {
    private String amount;
    private String receiptId;
    private String note;

    public RazorPayOrder(String amount, String receiptId, String note) {
        this.amount = amount;
        this.receiptId = receiptId;
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
