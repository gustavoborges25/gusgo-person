package gusgo.person.unit;

import gusgo.person.application.dto.PhoneDTO;
import gusgo.person.application.interfaces.PhoneRepository;
import gusgo.person.application.service.PhoneServiceImpl;
import gusgo.person.domain.model.Phone;
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
class PhoneServiceImplTest {

    @Mock
    private PhoneRepository repository;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    @Test
    void testUpdate_EmptyPhoneList() {
        BusinessPerson person = new BusinessPerson();
        List<PhoneDTO> phoneDTOS = new ArrayList<>();

        BusinessException exception = assertThrows(
                BusinessException.class, () -> phoneService.update(person, phoneDTOS));

        assertEquals("At least one phone is mandatory.", exception.getMessage());
    }

    @Test
    void testUpdate_WithNewPhone() {
        BusinessPerson person = new BusinessPerson();
        PhoneDTO newPhoneDTO = new PhoneDTO();
        newPhoneDTO.setPhone("new Phone");
        newPhoneDTO.setNote("new note");
        newPhoneDTO.setContact("new contact");

        List<PhoneDTO> phoneDTOS = List.of(newPhoneDTO);

        phoneService.update(person, phoneDTOS);

        assertEquals(1, person.getPhones().size());
        assertEquals("new Phone", person.getPhones().getFirst().getPhone());
    }

    @Test
    void testUpdate_WithExistingPhone() {
        BusinessPerson person = new BusinessPerson();
        Phone existingPhone = new Phone();
        existingPhone.setId(UUID.fromString("5faab1b2-9efe-4d07-9da6-85b182825586"));
        existingPhone.setPhone("Old Phone");
        person.setPhones(new ArrayList<>());
        person.addPhone(existingPhone);

        PhoneDTO updatedPhoneDTO = new PhoneDTO();
        updatedPhoneDTO.setId(UUID.fromString("5faab1b2-9efe-4d07-9da6-85b182825586"));
        updatedPhoneDTO.setPhone("New Phone");

        List<PhoneDTO> phoneDTOS = List.of(updatedPhoneDTO);
        when(repository.findAll()).thenReturn(List.of(existingPhone));

        phoneService.update(person, phoneDTOS);

        assertEquals(1, person.getPhones().size());
        assertEquals("New Phone", person.getPhones().getFirst().getPhone());
    }

    @Test
    void testUpdate_WithNonExistingPhone() {
        BusinessPerson person = new BusinessPerson();
        PhoneDTO updatedPhoneDTO = new PhoneDTO();
        updatedPhoneDTO.setId(UUID.randomUUID()); // Non-existent ID
        updatedPhoneDTO.setPhone("Nonexistent Phone");

        List<PhoneDTO> phoneDTOS = List.of(updatedPhoneDTO);
        when(repository.findAll()).thenReturn(new ArrayList<>());

        phoneService.update(person, phoneDTOS);

        assertEquals(0, person.getPhones().size());
    }
}