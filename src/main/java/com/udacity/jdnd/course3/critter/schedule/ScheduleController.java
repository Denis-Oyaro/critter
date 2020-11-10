package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired private ScheduleService scheduleService;
    @Autowired private EmployeeService employeeService;
    @Autowired private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleEntityToScheduleDTO(scheduleService.createSchedule(
                convertScheduleDTOToScheduleEntity(scheduleDTO)
        ));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        return scheduleService.getAllSchedules().stream()
                .map(this::convertScheduleEntityToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> scheduleDTOS = null;
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);
        if(schedules != null){
            scheduleDTOS = schedules.stream()
                    .map(this::convertScheduleEntityToScheduleDTO).collect(Collectors.toList());
        }

        return scheduleDTOS;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> scheduleDTOS = null;
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
        if(schedules != null){
            scheduleDTOS = schedules.stream()
                    .map(this::convertScheduleEntityToScheduleDTO).collect(Collectors.toList());
        }

        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleDTO> scheduleDTOS = null;
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        if(schedules != null){
            scheduleDTOS = schedules.stream()
                    .map(this::convertScheduleEntityToScheduleDTO).collect(Collectors.toList());
        }

        return scheduleDTOS;
    }

    private Schedule convertScheduleDTOToScheduleEntity(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO,schedule);
        if(scheduleDTO.getEmployeeIds() != null){
            scheduleDTO.getEmployeeIds()
                    .forEach(employeeId -> {
                        Employee employee = employeeService.getEmployee(employeeId);
                        if(employee != null){
                            schedule.getEmployees().add(employee);
                            employee.getSchedules().add(schedule);
                        }
                    });
        }

        if(scheduleDTO.getPetIds() != null){
            scheduleDTO.getPetIds()
                    .forEach(petId -> {
                        Pet pet = petService.getPet(petId);
                        if(pet != null){
                            schedule.getPets().add(pet);
                            pet.getSchedules().add(schedule);
                        }
                    });
        }

        return schedule;
    }

    private ScheduleDTO convertScheduleEntityToScheduleDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule,scheduleDTO);

        scheduleDTO.setEmployeeIds(new ArrayList<Long>());
        schedule.getEmployees()
                .forEach(employee -> {
                    scheduleDTO.getEmployeeIds().add(employee.getId());
                });

        scheduleDTO.setPetIds(new ArrayList<Long>());
        schedule.getPets()
                .forEach(pet -> {
                    scheduleDTO.getPetIds().add(pet.getId());
                });

        return scheduleDTO;
    }
}
