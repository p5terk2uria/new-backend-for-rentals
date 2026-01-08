package productservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.property.repository.PropertyRepository;
import productservice.visits.RequestVisit;
import productservice.visits.dto.VisitRequest;

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
                .userId(userData.id())
                .visitingDate(request.visitingDate())
                .visitingTime(request.visitingTime())
                .noOfVisitors(request.noOfVisitors())
                .notes(request.notes())
                .build();

    }

}
