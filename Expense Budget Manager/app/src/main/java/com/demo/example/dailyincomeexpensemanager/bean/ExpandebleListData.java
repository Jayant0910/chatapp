package com.demo.example.dailyincomeexpensemanager.bean;

import java.util.List;


public class ExpandebleListData {
    CategoryBean category;
    List<DataBean> categoryData;

    public CategoryBean getCategory() {
        return this.category;
    }

    public void setCategory(CategoryBean categoryBean) {
        this.category = categoryBean;
    }

    public List<DataBean> getCategoryData() {
        return this.categoryData;
    }

    public void setCategoryData(List<DataBean> list) {
        this.categoryData = list;
    }
}
