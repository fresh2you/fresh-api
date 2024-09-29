package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.address.DeliveryAddress;

public record AddressDto(
    Long deliveryAddressId,

    String recipientName,

    String address,

    String phoneNumber,

    String detailedAddress,

    String postalCode,

    boolean isDefault
) {
    public static AddressDto fromEntity(DeliveryAddress address) {
        return new AddressDto(address.getId(),address.getRecipientName(), address.getAddress(),
            address.getPhone(),address.getDetailedAddress(), address.getPostalCode(), address.isDefault());
    }
}
