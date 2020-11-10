package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired private EmployeeService employeeService;
    @Autowired private CustomerService customerService;
    @Autowired private PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertCustomerDTOToCustomerEntity(customerDTO);
        return convertCustomerEntityToCustomerDTO(customerService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers().stream()
                .map(this::convertCustomerEntityToCustomerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        CustomerDTO customerDTO = null;
        Customer owner = customerService.getOwnerByPet(petId);
        if(owner != null){
            customerDTO = convertCustomerEntityToCustomerDTO(owner);
        }
        return customerDTO;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEmployeeEntity(employeeDTO);
        return convertEmployeeEntityToEmployeeDTO(employeeService.saveEmployee(employee));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        EmployeeDTO employeeDTO = null;
        Employee employee = employeeService.getEmployee(employeeId);
        if(employee != null) {
            employeeDTO = convertEmployeeEntityToEmployeeDTO(employee);
        }
        return employeeDTO;
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(daysAvailable,employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeRequestDTO.getDate(),
                employeeRequestDTO.getSkills());
         return employees.stream()
                .map(this::convertEmployeeEntityToEmployeeDTO).collect(Collectors.toList());
    }

    private Customer convertCustomerDTOToCustomerEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        if(customerDTO.getPetIds() != null) {
            customerDTO.getPetIds()
                    .forEach(petId -> {
                        Pet pet = petService.getPet(petId);
                        if (pet != null) {
                            customer.getPets().add(pet);
                            pet.setOwner(customer);
                        }
                    });
        }

        return customer;
    }

    private CustomerDTO convertCustomerEntityToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        customerDTO.setPetIds(new ArrayList<Long>());
        customer.getPets().forEach(pet -> {
            customerDTO.getPetIds().add(pet.getId());
        });

        return customerDTO;
    }

    private Employee convertEmployeeDTOToEmployeeEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        return employee;
    }

    private EmployeeDTO convertEmployeeEntityToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }

}
