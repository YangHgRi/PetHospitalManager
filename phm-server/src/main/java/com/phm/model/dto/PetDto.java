package com.phm.model.dto;

import com.phm.model.entity.Pet;

public class PetDto extends Pet {
    /**
     * 主人姓名
     */
    private String clientName;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
