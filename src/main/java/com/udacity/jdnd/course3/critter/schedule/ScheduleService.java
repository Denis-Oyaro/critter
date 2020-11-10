package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ScheduleService {

    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private EmployeeService employeeService;
    @Autowired private PetService petService;
    @Autowired private CustomerService customerService;

    public Schedule createSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId){
        List<Schedule> schedules = null;
        Pet pet = petService.getPet(petId);
        if(pet != null){
            schedules = pet.getSchedules();
        }
        return schedules;
    }

    public List<Schedule> getScheduleForEmployee(long employeeId){
        List<Schedule> schedules = null;
        Employee employee = employeeService.getEmployee(employeeId);
        if(employee != null){
            schedules = employee.getSchedules();
        }
        return schedules;
    }

    public List<Schedule> getScheduleForCustomer(long customerId){
        List<Schedule> schedules = null;
        Customer customer = customerService.getCustomerById(customerId);
        Set<Schedule> petScheduleSet = new HashSet<Schedule>();
        if(customer != null){
            customer.getPets()
                    .forEach(pet -> {
                        petScheduleSet.addAll(pet.getSchedules());
                    });
            schedules = new ArrayList<Schedule>(petScheduleSet);
        }

        return schedules;
    }
}
