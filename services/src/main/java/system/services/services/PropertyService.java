package system.services.services;

import system.services.services.dto.ServiceRequest;
import system.services.services.dto.ServiceResponse;

import java.util.List;


public interface PropertyService {

    void  createService (ServiceRequest request);

    List<ServiceResponse> getAllServices();




}
