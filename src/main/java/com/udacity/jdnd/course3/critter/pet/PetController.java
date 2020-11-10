package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired private PetService petService;
    @Autowired private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToPetEntity(petDTO);
        return convertPetEntityToPetDTO(petService.savePet(pet));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetEntityToPetDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getPets().stream()
                .map(this::convertPetEntityToPetDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.getPetsByOwner(ownerId).stream()
                .map(this::convertPetEntityToPetDTO)
                .collect(Collectors.toList());
    }


    private Pet convertPetDTOToPetEntity(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO,pet);
        Customer owner = customerService.getCustomerById(petDTO.getOwnerId());
        if(owner != null){
            pet.setOwner(owner);
            if(!owner.getPets().contains(pet)){
                owner.getPets().add(pet);
            }
        }
        return pet;
    }

    private PetDTO convertPetEntityToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet,petDTO);
        Customer owner = pet.getOwner();
        if(owner != null) {
            petDTO.setOwnerId(owner.getId());
        }
        return petDTO;
    }
}
