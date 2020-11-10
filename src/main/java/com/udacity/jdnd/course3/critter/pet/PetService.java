package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired private PetRepository petRepository;
    @Autowired private CustomerService customerService;

    public Pet savePet( Pet pet){
        return petRepository.save(pet);
    }

    public Pet getPet(long petId){
        return petRepository.findById(petId).orElse(null);
    }

    public List<Pet> getPets(){
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId){
        List<Pet> pets = new ArrayList<Pet>();
        Customer owner = customerService.getCustomerById(ownerId);
        if(owner != null){
            pets = owner.getPets();
        }
        return pets;
    }

}
