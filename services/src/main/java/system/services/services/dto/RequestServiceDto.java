package system.services.services.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record RequestServiceDto(

        String serviceId,

        String serviceProviderId,

        LocalDate dateRequested,

        String location


) {
}
