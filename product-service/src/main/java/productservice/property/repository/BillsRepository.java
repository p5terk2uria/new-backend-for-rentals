package productservice.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productservice.property.entities.RoomBills;

@Repository
public interface BillsRepository extends JpaRepository<RoomBills,String> {

}
