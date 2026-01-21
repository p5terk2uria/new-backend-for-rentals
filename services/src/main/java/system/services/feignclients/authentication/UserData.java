package system.services.feignclients.authentication;


import system.services.serviceproviders.enums.DomainRoles;

public record UserData(

        String id,

        String phoneNumber,
        
        String emailAddress,
        
        String countryCode,
        
        String firstName,
        
        String middleName,
        
        String lastName,
        
        String line1,
        
        String line2,
        
        String city,
        
        String state,
        
        String postalCode,
        
        String zipCode,

        DomainRoles role
        
) {
    
}
