package corecentra.qaqctool.fields;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectFieldRepository extends JpaRepository<ProjectFields,Long> {

    List<ProjectFields> findAll();

    ProjectFields save(ProjectFields entity);

    ProjectFields findById(int id);
}
