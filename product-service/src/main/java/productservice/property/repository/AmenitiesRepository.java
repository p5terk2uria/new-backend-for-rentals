package productservice.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productservice.property.entities.PropertyAmenities;
import productservice.property.entities.PropertyBills;

import java.util.Set;

@Repository
public interface AmenitiesRepository extends JpaRepository<PropertyAmenities,String> {

    Set<PropertyAmenities> findByPropertyId(String propertyId);
}
