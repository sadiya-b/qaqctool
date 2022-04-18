package corecentra.qaqctool.fields;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContributionFieldRepository extends JpaRepository<ContributionFields,Long> {

    List<ContributionFields> findAll();

    ContributionFields save(ContributionFields entity);

    ContributionFields findById(int id);
}
