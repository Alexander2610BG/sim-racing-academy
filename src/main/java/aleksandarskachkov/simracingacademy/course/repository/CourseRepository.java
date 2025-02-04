package aleksandarskachkov.simracingacademy.course.repository;

import aleksandarskachkov.simracingacademy.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
}
