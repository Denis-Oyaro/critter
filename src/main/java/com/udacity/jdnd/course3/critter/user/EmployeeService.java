package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee){
        Employee e = employeeRepository.save(employee);
        return e;
    }

    public Employee getEmployee(long employeeId){
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee != null){
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        }

    }

    public List<Employee> findEmployeesForService(LocalDate date,Set<EmployeeSkill> skills){
        String dayAvailable = date.getDayOfWeek().name();

        List<String> skillsList = skills.stream()
                .map(Enum::name).collect(Collectors.toList());

        Map<Long,Integer> employeeIdCountMap = new HashMap<Long,Integer>();

        // Get employees with desired set of skills
        List<Long> employeeIdsList = null;
        for(String skill: skillsList){
            employeeIdsList = employeeRepository.findEmployeeIdsBySkill(skill);
            evaluateIdCount(employeeIdCountMap,employeeIdsList);
        }


        // Get employees available
        employeeIdsList= employeeRepository.findEmployeeIdsBydayAvailable(dayAvailable);
        evaluateIdCount(employeeIdCountMap,employeeIdsList);

        //Get employees with skills who are available
        List<Employee> employees = new ArrayList<Employee>();
        for(Map.Entry<Long,Integer> idEntry: employeeIdCountMap.entrySet()){
            if(idEntry.getValue() == skills.size() + 1) {// check for all skills and available date
                employeeRepository.findById(idEntry.getKey()).ifPresent(employees::add);
            }
        }

        return employees;
    }

    private void evaluateIdCount(Map<Long,Integer> employeeIdCountMap,List<Long> employeeIdsList){

        for(Long id: employeeIdsList){
            if(!employeeIdCountMap.containsKey(id)){
                employeeIdCountMap.put(id,1);
            } else {
                employeeIdCountMap.put(id,employeeIdCountMap.get(id) + 1);
            }
        }
    }
}
