package com.blueshit.model;

import java.io.Serializable;

/**
 * Created by Jayden on 2015/3/4.
 */
public class BaseUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
