package gusgo.person.application.service;

import gusgo.person.application.dto.AddressDTO;
import gusgo.person.application.interfaces.AddressRepository;
import gusgo.person.application.interfaces.AddressService;
import gusgo.person.application.resources.ValidationConstants;
import gusgo.person.domain.model.Address;
import gusgo.person.domain.model.Person;
import gusgo.person.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    @Override
    public List<Address> update(Person person, List<AddressDTO> addressDTOS) {
        if (addressDTOS.isEmpty()) {
            throw new BusinessException(ValidationConstants.ADDRESS_IS_MANDATORY);
        }

        List<Address> updatedAddresses = new ArrayList<>();

        List<Address> existingAddresses = repository.findAll();
        var existingAddressMap = existingAddresses.stream().collect(Collectors.toMap(Address::getId, address -> address));

        addressDTOS.forEach(addressDTO -> {
            if (addressDTO.getId() == null) {
                Address newAddress = createAddressFromDTO(person, addressDTO);
                updatedAddresses.add(repository.save(newAddress));
            } else {
                Address existingAddress = existingAddressMap.get(addressDTO.getId());
                if (existingAddress != null) {
                    updateAddressFromDTO(existingAddress, addressDTO);
                    updatedAddresses.add(repository.save(existingAddress));
                }
            }
        });

        List<UUID> updatedIds = addressDTOS.stream()
                .map(AddressDTO::getId)
                .filter(Objects::nonNull)
                .toList();

        existingAddresses.stream()
                .filter(address -> !updatedIds.contains(address.getId()))
                .forEach(repository::delete);

        return updatedAddresses;
    }

    private Address createAddressFromDTO(Person person, AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setNumber(addressDTO.getNumber());
        address.setNeighborhood(addressDTO.getNeighborhood());
        address.setZipCode(addressDTO.getZipCode());
        address.setState(addressDTO.getState());
        address.setCity(addressDTO.getCity());
        address.setPerson(person);
        return address;
    }

    private void updateAddressFromDTO(Address existingAddress, AddressDTO addressDTO) {
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setNumber(addressDTO.getNumber());
        existingAddress.setNeighborhood(addressDTO.getNeighborhood());
        existingAddress.setZipCode(addressDTO.getZipCode());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setCity(addressDTO.getCity());
    }
}
