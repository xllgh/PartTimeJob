package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("item_type_name")
    private String typeName;

    @SerializedName("item_id")
    private String id;

    @SerializedName("item_type_id")
    private String typeId;

    @SerializedName("item_name")
    private String name;


}
