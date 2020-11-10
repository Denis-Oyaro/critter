package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private PetService petService;

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getOwnerByPet(long petId){
        Pet pet = petService.getPet(petId);
        if(pet == null){
            return null;
        }
        return pet.getOwner();
    }

    public Customer getCustomerById(long customerId){
        return customerRepository.findById(customerId).orElse(null);
    }


}
