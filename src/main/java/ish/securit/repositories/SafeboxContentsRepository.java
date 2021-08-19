package ish.securit.repositories;

import ish.securit.models.Safebox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SafeboxContentsRepository extends JpaRepository<Safebox, String> {

}
