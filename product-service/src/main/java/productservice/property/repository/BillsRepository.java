package productservice.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productservice.property.entities.PropertyBills;

import java.util.Set;

@Repository
public interface BillsRepository extends JpaRepository<PropertyBills,String> {

    Set<PropertyBills> findAllByPropertyId(String propertyId);
}
