package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query(nativeQuery = true,value = "select distinct e.id from Employee e join employee_skill s \n" +
            "on s.employee_id = e.id \n" +
            "where s.skill = :skill")
    List<Long> findEmployeeIdsBySkill(@Param("skill") String skill);


    @Query(nativeQuery = true,value = "select distinct e.id from Employee e join employee_day d \n" +
            "on d.employee_id = e.id \n" +
            "where d.day = :day")
    List<Long> findEmployeeIdsBydayAvailable(@Param("day") String day);
}
