package corecentra.qaqctool.fields;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskFieldRepository extends JpaRepository<TaskFields,Long> {

    List<TaskFields> findAll();

    TaskFields save(TaskFields entity);

    TaskFields findById(int id);
}
