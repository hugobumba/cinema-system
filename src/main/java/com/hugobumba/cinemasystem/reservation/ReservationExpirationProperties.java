package com.hugobumba.cinemasystem.reservation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "reservation.expiration")
public class ReservationExpirationProperties {
    private long minutes = 10;

}
