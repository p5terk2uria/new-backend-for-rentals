package productservice.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productservice.property.entities.PropertyAmenities;

@Repository
public interface AmenitiesRepository extends JpaRepository<PropertyAmenities,String> {
}
