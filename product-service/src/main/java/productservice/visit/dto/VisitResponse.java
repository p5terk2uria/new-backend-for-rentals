package productservice.visit.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record VisitResponse(

        String visitId,

        String userId
) {
}
