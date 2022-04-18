package corecentra.qaqctool.fields;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonorFieldRepository extends JpaRepository<DonorFields,Long> {

    List<DonorFields> findAll();

    DonorFields save(DonorFields entity);

    DonorFields findById(int id);
}
