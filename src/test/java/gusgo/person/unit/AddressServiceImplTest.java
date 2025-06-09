package gusgo.person.unit;

import gusgo.person.application.dto.AddressDTO;
import gusgo.person.application.interfaces.AddressRepository;
import gusgo.person.application.service.AddressServiceImpl;
import gusgo.person.domain.model.Address;
import gusgo.person.domain.model.BusinessPerson;
import gusgo.person.rest.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository repository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void testUpdate_EmptyAddressList() {
        BusinessPerson person = new BusinessPerson();
        List<AddressDTO> addressDTOS = new ArrayList<>();

        BusinessException exception = assertThrows(
                BusinessException.class, () -> addressService.update(person, addressDTOS));

        assertEquals("At least one address is mandatory.", exception.getMessage());
    }

    @Test
    void testUpdate_WithNewAddress() {
        BusinessPerson person = new BusinessPerson();
        AddressDTO newAddressDTO = new AddressDTO();
        newAddressDTO.setStreet("Main St");
        newAddressDTO.setNumber("123");
        newAddressDTO.setCity("City A");
        newAddressDTO.setState("State A");
        newAddressDTO.setNeighborhood("Neighborhood A");
        newAddressDTO.setZipCode("11111");

        List<AddressDTO> addressDTOS = List.of(newAddressDTO);

        addressService.update(person, addressDTOS);

        assertEquals(1, person.getAddresses().size());
        assertEquals("Main St", person.getAddresses().get(0).getStreet());
    }

    @Test
    void testUpdate_WithExistingAddress() {
        BusinessPerson person = new BusinessPerson();
        Address existingAddress = new Address();
        existingAddress.setId(UUID.fromString("5faab1b2-9efe-4d07-9da6-85b182825586"));
        existingAddress.setStreet("Old St");
        person.setAddresses(new ArrayList<>());
        person.addAddress(existingAddress);

        AddressDTO updatedAddressDTO = new AddressDTO();
        updatedAddressDTO.setId(UUID.fromString("5faab1b2-9efe-4d07-9da6-85b182825586"));
        updatedAddressDTO.setStreet("New St");
        updatedAddressDTO.setNumber("456");
        updatedAddressDTO.setCity("City B");
        updatedAddressDTO.setState("State B");
        updatedAddressDTO.setNeighborhood("Neighborhood B");
        updatedAddressDTO.setZipCode("22222");

        List<AddressDTO> addressDTOS = List.of(updatedAddressDTO);
        when(repository.findAll()).thenReturn(List.of(existingAddress));

        addressService.update(person, addressDTOS);

        assertEquals(1, person.getAddresses().size());
        assertEquals("New St", person.getAddresses().getFirst().getStreet());
    }

    @Test
    void testUpdate_WithNonExistingAddress() {
        BusinessPerson person = new BusinessPerson();
        AddressDTO updatedAddressDTO = new AddressDTO();
        updatedAddressDTO.setId(UUID.randomUUID()); // Non-existent ID
        updatedAddressDTO.setStreet("Nonexistent St");

        List<AddressDTO> addressDTOS = List.of(updatedAddressDTO);
        when(repository.findAll()).thenReturn(new ArrayList<>());

        addressService.update(person, addressDTOS);

        assertEquals(0, person.getAddresses().size());
    }
}