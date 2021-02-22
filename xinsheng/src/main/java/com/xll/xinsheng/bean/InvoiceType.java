package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceType {

    @SerializedName("itemTypeList")
    private List<InvoiceItem> invoiceItems;

    @SerializedName("userid")
    private String userId;

    @SerializedName("username")
    private String userName;

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "InvoiceType{" +
                "invoiceItems=" + invoiceItems +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public static class InvoiceItem {

        @SerializedName("fees")
        private String fees;

        @SerializedName("item_type_name")
        private String itemTypeName;

        @SerializedName("item_id")
        private String itemId;

        @SerializedName("item_type_id")
        private String itemTypeId;

        @SerializedName("item_name")
        private String itemName;

        public String getFees() {
            return fees;
        }

        public void setFees(String fees) {
            this.fees = fees;
        }

        public String getItemTypeName() {
            return itemTypeName;
        }

        public void setItemTypeName(String itemTypeName) {
            this.itemTypeName = itemTypeName;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemTypeId() {
            return itemTypeId;
        }

        public void setItemTypeId(String itemTypeId) {
            this.itemTypeId = itemTypeId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        @Override
        public String toString() {
            return "InvoiceItem{" +
                    "fees=" + fees +
                    ", itemTypeName='" + itemTypeName + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", itemTypeId='" + itemTypeId + '\'' +
                    ", itemName='" + itemName + '\'' +
                    '}';
        }
    }
}
