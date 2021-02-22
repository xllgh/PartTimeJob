package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

public class ItemType {

    @SerializedName("item_type_name")
    private String typeName;

    @SerializedName("item_type_id")
    private String typeId;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "ItemType{" +
                "typeName='" + typeName + '\'' +
                ", typeId='" + typeId + '\'' +
                '}';
    }
}
