package authentication_service.user;

import authentication_service.user.dto.UserData;
import authentication_service.user.location.LocationDetails;
import org.springframework.stereotype.Component;

@Component
public class mapper {


    public UserData toUserData(User user, LocationDetails location) {

        return new UserData(
                user.getId(),
                user.getPhoneNumber(),
                user.getEmail(),
                location != null ? location.getCountryCode() : null,
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                location != null ? location.getLine1() : null,
                location != null ? location.getLine2() : null,
                location != null ? location.getCity() : null,
                location != null ? location.getState() : null,
                location != null ? location.getPostalCode() : null,
                location != null ? location.getZipCode() : null
        );
    }

}
