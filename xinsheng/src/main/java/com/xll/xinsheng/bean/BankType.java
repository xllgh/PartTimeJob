package com.xll.xinsheng.bean;

public class BankType {
    private String bank_id;

    private String bank_name;

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    @Override
    public String toString() {
        return "BankType{" +
                "bank_id='" + bank_id + '\'' +
                ", bank_name='" + bank_name + '\'' +
                '}';
    }
}
