package com.swmarastro.mykkumiserver.post.dto;

import com.swmarastro.mykkumiserver.post.domain.Pin;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PinDto {

    private Double positionX;
    private Double positionY;
    private ProductInfoDto productInfo;

    public static PinDto of(Pin pin) {
        ProductInfoDto productInfoDto = ProductInfoDto.of(pin.getProduct());

        return PinDto.builder()
                .positionX(pin.getPositionX())
                .positionY(pin.getPositionY())
                .productInfo(productInfoDto)
                .build();
    }
}
