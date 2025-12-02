package productservice.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productservice.property.entities.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property,String> {
}
