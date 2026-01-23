package productservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.visit.RequestVisit;
import productservice.visit.dto.VisitRequest;
import productservice.visit.dto.VisitResponse;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class VisitRequestMapper {

    private final AuthenticationClient authenticationClient;

    public RequestVisit toVisitEntity(VisitRequest request){

        UserData userData = authenticationClient.getUserById(request.userId());

        if(userData == null) {
            throw new  RuntimeException("user not found with id %s" + request.userId());
        }

        String tenantName = Stream.of(userData.firstName(), userData.lastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        return RequestVisit.builder()
                .tenantName(tenantName)
                .phoneNumber(userData.phoneNumber())
                .userId(userData.id())
                .visitingDate(request.visitingDate())
                .visitingTime(request.visitingTime())
                .noOfVisitors(request.noOfVisitors())
                .notes(request.notes())
                .build();

    }

    public VisitResponse toVisitResponse(RequestVisit visit) {

        return VisitResponse.builder()
                .visitId(visit.getId())
                .userId(visit.getUserId())
                .orderTrackingId(visit.getOrderTrackingId())
                .tenantName(visit.getTenantName())
                .visitingDate(visit.getVisitingDate().toString())
                .visitingTime(visit.getVisitingTime().toString())
                .noOfVisitors(visit.getNoOfVisitors())
                .visitStatus(visit.getStatus())
                .notes(visit.getNotes())
                .roomId(visit.getRoom().getId())
                .build();
    }

}
