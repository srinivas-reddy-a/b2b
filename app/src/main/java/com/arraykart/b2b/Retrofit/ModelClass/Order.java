package com.arraykart.b2b.Retrofit.ModelClass;

public class Order {
//    private int order_id;
    private int total;
    private int savings;
    private String payment_type;
    private Boolean is_paid;
    private String razorpay_payment_id;
    private String razorpay_order_id;
    private String razorpay_signature;

    public Order(int total, int savings, String payment_type, Boolean is_paid, String razorpay_payment_id, String razorpay_order_id, String razorpay_signature) {
//        this.order_id = order_id;
        this.total = total;
        this.savings = savings;
        this.payment_type = payment_type;
        this.is_paid = is_paid;
        this.razorpay_payment_id = razorpay_payment_id;
        this.razorpay_order_id = razorpay_order_id;
        this.razorpay_signature = razorpay_signature;
    }
//
//    public int getOrder_id() {
//        return order_id;
//    }
//
//    public void setOrder_id(int order_id) {
//        this.order_id = order_id;
//    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSavings() {
        return savings;
    }

    public void setSavings(int savings) {
        this.savings = savings;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public Boolean getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(Boolean is_paid) {
        this.is_paid = is_paid;
    }

    public String getRazorpay_payment_id() {
        return razorpay_payment_id;
    }

    public void setRazorpay_payment_id(String razorpay_payment_id) {
        this.razorpay_payment_id = razorpay_payment_id;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }

    public String getRazorpay_signature() {
        return razorpay_signature;
    }

    public void setRazorpay_signature(String razorpay_signature) {
        this.razorpay_signature = razorpay_signature;
    }
}
