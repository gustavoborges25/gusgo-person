package gusgo.person.unit;

import gusgo.person.application.dto.AddressDTO;
import gusgo.person.application.dto.EmailDTO;
import gusgo.person.application.dto.PersonDTO;
import gusgo.person.application.dto.PhoneDTO;
import gusgo.person.application.factory.BusinessPersonFactory;
import gusgo.person.application.interfaces.AddressService;
import gusgo.person.application.interfaces.BusinessPersonRepository;
import gusgo.person.application.interfaces.EmailService;
import gusgo.person.application.interfaces.PhoneService;
import gusgo.person.domain.interfaces.BusinessPersonDomainService;
import gusgo.person.domain.model.BusinessPerson;
import gusgo.person.rest.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessPersonFactoryTest {

    @Mock
    private BusinessPersonRepository repository;

    @Mock
    private BusinessPersonDomainService businessPersonDomainService;

    @Mock
    private AddressService addressService;

    @Mock
    private EmailService emailService;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private BusinessPersonFactory businessPersonFactory;

    @Test
    void testCreate_Success() throws BusinessException {
        PersonDTO personDTO = getPersonDTO();

        BusinessPerson result = (BusinessPerson) businessPersonFactory.create(personDTO);

        assertNotNull(result);
        assertEquals("name", result.getName());
        assertEquals("email", result.getEmails().getFirst().getEmail());
        assertEquals("phone", result.getPhones().getFirst().getPhone());
        assertEquals("street", result.getAddresses().getFirst().getStreet());
        verify(businessPersonDomainService, times(1)).validateBusinessPerson(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testCreate_ValidationError() throws BusinessException {
        PersonDTO personDTO = getPersonDTO();
        BusinessPerson businessPerson = new BusinessPerson();

        doThrow(new BusinessException("Validation error"))
                .when(businessPersonDomainService).validateBusinessPerson(any(BusinessPerson.class));

        BusinessException exception = assertThrows(
                BusinessException.class, () -> businessPersonFactory.create(personDTO));

        assertEquals("Validation error", exception.getMessage());
        verify(repository, never()).save(businessPerson);
    }

    @Test
    void testUpdate_Success() throws BusinessException {
        UUID id = UUID.randomUUID();
        PersonDTO personDTO = getPersonDTO();
        personDTO.setName("new Name");
        personDTO.setNickname("new Nickname");
        personDTO.setErpCode("new ErpCode");
        personDTO.setMainDocument("new MainDocument");
        BusinessPerson existingBusinessPerson = new BusinessPerson();
        existingBusinessPerson.setName("old Name");
        existingBusinessPerson.setNickname("old Nickname");
        existingBusinessPerson.setErpCode("old ErpCode");
        existingBusinessPerson.setDocumentCNPJ("old DocumentCNPJ");

        when(repository.findById(id)).thenReturn(Optional.of(existingBusinessPerson));
        doNothing().when(addressService).update(existingBusinessPerson, personDTO.getAddresses());
        doNothing().when(emailService).update(existingBusinessPerson, personDTO.getEmails());
        doNothing().when(phoneService).update(existingBusinessPerson, personDTO.getPhones());
        doNothing().when(businessPersonDomainService).validateBusinessPerson(existingBusinessPerson);
        when(repository.save(existingBusinessPerson)).thenReturn(existingBusinessPerson);

        BusinessPerson result = (BusinessPerson) businessPersonFactory.update(id, personDTO);

        assertEquals("new Name", result.getName());
        assertEquals("new Nickname", result.getNickname());
        assertEquals("new ErpCode", result.getErpCode());
        assertEquals("new MainDocument", result.getDocumentCNPJ());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existingBusinessPerson);
    }

    @Test
    void testUpdate_PersonNotFound() {
        UUID id = UUID.randomUUID();
        PersonDTO personDTO = new PersonDTO();

        when(repository.findById(id)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class, () -> businessPersonFactory.update(id, personDTO));

        assertEquals("Person not found", exception.getMessage());
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdate_ValidationError() throws BusinessException {
        UUID id = UUID.randomUUID();
        PersonDTO personDTO = new PersonDTO();
        BusinessPerson existingBusinessPerson = new BusinessPerson();

        when(repository.findById(id)).thenReturn(Optional.of(existingBusinessPerson));
        doThrow(new BusinessException("Validation error"))
                .when(businessPersonDomainService).validateBusinessPerson(existingBusinessPerson);

        BusinessException exception = assertThrows(
                BusinessException.class, () -> businessPersonFactory.update(id, personDTO));

        assertEquals("Validation error", exception.getMessage());
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(existingBusinessPerson);
    }

    private static @NotNull PersonDTO getPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("name");
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("email");
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("street");
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setPhone("phone");

        personDTO.setEmails(List.of(emailDTO));
        personDTO.setPhones(List.of(phoneDTO));
        personDTO.setAddresses(List.of(addressDTO));
        return personDTO;
    }
}