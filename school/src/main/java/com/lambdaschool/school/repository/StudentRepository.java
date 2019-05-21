package com.lambdaschool.school.repository;

import com.lambdaschool.school.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.ArrayList;
import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long>, CrudRepository<Student, Long>
{
    List<Student> findByStudnameContainingIgnoreCase(String name, Pageable pageable);

//    @Query(value = "", nativeQuery = true)
//    void addStudentwithCourse(long courseid, long studentid);
}
