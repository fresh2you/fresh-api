package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import java.util.List;

public record GetAllAddressResponse(
    List<AddressDto> addressList
) {

    public static GetAllAddressResponse fromEntities(List<DeliveryAddress> list){
        return new GetAllAddressResponse(list.stream().map(AddressDto::fromEntity).toList());
    }


}
