package gusgo.person.application.factory;

import gusgo.person.application.dto.PersonDTO;
import gusgo.person.application.interfaces.AddressService;
import gusgo.person.application.interfaces.BusinessPersonRepository;
import gusgo.person.application.interfaces.EmailService;
import gusgo.person.application.interfaces.PersonFactory;
import gusgo.person.application.interfaces.PhoneService;
import gusgo.person.application.mapper.PersonMapper;
import gusgo.person.application.resources.ValidationConstants;
import gusgo.person.domain.interfaces.BusinessPersonDomainService;
import gusgo.person.domain.model.BusinessPerson;
import gusgo.person.domain.model.Person;
import gusgo.person.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BusinessPersonFactory implements PersonFactory {

    private final BusinessPersonRepository repository;
    private final BusinessPersonDomainService businessPersonDomainService;
    private final AddressService addressService;
    private final EmailService emailService;
    private final PhoneService phoneService;

    @Override
    public Person create(PersonDTO personDto) throws BusinessException {

        BusinessPerson businessPerson = PersonMapper.INSTANCE.toBusinessPerson(personDto);
        businessPerson.getAddresses().forEach(address -> address.setPerson(businessPerson));
        businessPerson.getEmails().forEach(email -> email.setPerson(businessPerson));
        businessPerson.getPhones().forEach(phone -> phone.setPerson(businessPerson));

        businessPersonDomainService.validateBusinessPerson(businessPerson);

        repository.save(businessPerson);

        return businessPerson;

    }

    @Override
    public Person update(UUID id, PersonDTO personDto) throws BusinessException {
        BusinessPerson businessPerson = repository.findById(id).orElseThrow(() -> new BusinessException(ValidationConstants.PERSON_NOT_FOUND));

        addressService.update(businessPerson, personDto.getAddresses());
        emailService.update(businessPerson, personDto.getEmails());
        phoneService.update(businessPerson, personDto.getPhones());

        businessPerson.setName(personDto.getName());
        businessPerson.setNickname(personDto.getNickname());
        businessPerson.setErpCode(personDto.getErpCode());
        businessPerson.setIsCustomer(personDto.getIsCustomer() != null && personDto.getIsCustomer().equals("YES"));
        businessPerson.setIsProvider(personDto.getIsProvider() != null && personDto.getIsProvider().equals("YES"));
        businessPerson.setDocumentCNPJ(personDto.getMainDocument());
        businessPerson.setDocumentIE(personDto.getSecondaryDocument());

        businessPersonDomainService.validateBusinessPerson(businessPerson);

        repository.save(businessPerson);

        return businessPerson;
    }
}
