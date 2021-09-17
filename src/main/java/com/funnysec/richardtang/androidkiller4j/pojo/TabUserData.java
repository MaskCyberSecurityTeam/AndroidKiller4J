package com.funnysec.richardtang.androidkiller4j.pojo;

import lombok.Data;

/**
 * 配合{@link javafx.scene.control.Tab}中设置UserData用
 *
 * @param <T> UserData存储的具体数据类型
 */
@Data
public class TabUserData<T> {

    // TabId
    private String tabId;

    // Tab UserData
    private T data;

    /**
     * 创建TabUserData对象
     *
     * @param tabId tabId
     * @param data  userData
     */
    public TabUserData(String tabId, T data) {
        this.data  = data;
        this.tabId = tabId;
    }
}