package com.df.util;

import com.df.dto.OwnerDto;
import com.df.entity.Owner;
import com.df.request.OwnerRequest;

public class OwnerUtility {

    public static Owner ownerRequestToOwner(OwnerRequest ownerRequest) {
        return new Owner(ownerRequest);
    }

    public static Owner ownerDtoToOwner(OwnerDto ownerDto) {
        return new Owner(ownerDto);
    }

    public static OwnerDto ownerToOwnerDto(Owner owner) {
        return new OwnerDto(owner);
    }

    public static OwnerDto ownerToOwnerDto(OwnerRequest ownerRequest) {
        return new OwnerDto(ownerRequest);
    }

    public static OwnerRequest ownerToOwnerRequest(Owner owner) {
        return new OwnerRequest(owner);
    }

}
