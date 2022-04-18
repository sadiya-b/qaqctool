package corecentra.qaqctool.fields;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultFieldRepository extends JpaRepository<ResultFields,Long> {

    List<ResultFields> findAll();

    ResultFields save(ResultFields entity);

    ResultFields findById(int id);
}
