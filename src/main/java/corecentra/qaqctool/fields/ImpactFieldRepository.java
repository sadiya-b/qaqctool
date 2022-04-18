package corecentra.qaqctool.fields;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImpactFieldRepository extends JpaRepository<ImpactFields,Long> {

    List<ImpactFields> findAll();

    ImpactFields save(ImpactFields entity);

    ImpactFields findById(int id);
}
