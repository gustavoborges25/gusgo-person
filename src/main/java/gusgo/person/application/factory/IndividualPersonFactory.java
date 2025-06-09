package gusgo.person.application.factory;

import gusgo.person.application.dto.PersonDTO;
import gusgo.person.application.interfaces.AddressService;
import gusgo.person.application.interfaces.EmailService;
import gusgo.person.application.interfaces.IndividualPersonRepository;
import gusgo.person.application.interfaces.PersonFactory;
import gusgo.person.application.interfaces.PhoneService;
import gusgo.person.application.mapper.PersonMapper;
import gusgo.person.application.resources.ValidationConstants;
import gusgo.person.domain.interfaces.IndividualPersonDomainService;
import gusgo.person.domain.model.IndividualPerson;
import gusgo.person.domain.model.Person;
import gusgo.person.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class IndividualPersonFactory implements PersonFactory {

    private final IndividualPersonRepository repository;
    private final IndividualPersonDomainService individualPersonDomainService;
    private final AddressService addressService;
    private final EmailService emailService;
    private final PhoneService phoneService;

    @Override
    public Person create(PersonDTO personDto) throws BusinessException {

        IndividualPerson individualPerson = PersonMapper.INSTANCE.toIndividualPerson(personDto);
        individualPerson.getAddresses().forEach(address -> address.setPerson(individualPerson));
        individualPerson.getEmails().forEach(email -> email.setPerson(individualPerson));
        individualPerson.getPhones().forEach(phone -> phone.setPerson(individualPerson));

        individualPersonDomainService.validateIndividualPerson(individualPerson);

        repository.save(individualPerson);

        return individualPerson;
    }

    @Override
    public Person update(UUID id, PersonDTO personDto) throws BusinessException {
        IndividualPerson individualPerson = repository.findById(id).orElseThrow(() -> new BusinessException(ValidationConstants.PERSON_NOT_FOUND));

        addressService.update(individualPerson, personDto.getAddresses());
        emailService.update(individualPerson, personDto.getEmails());
        phoneService.update(individualPerson, personDto.getPhones());

        individualPerson.setName(personDto.getName());
        individualPerson.setNickname(personDto.getNickname());
        individualPerson.setErpCode(personDto.getErpCode());
        individualPerson.setIsCustomer(personDto.getIsCustomer() != null && personDto.getIsCustomer().equals("YES"));
        individualPerson.setIsProvider(personDto.getIsProvider() != null && personDto.getIsProvider().equals("YES"));
        individualPerson.setDocumentCPF(personDto.getMainDocument());
        individualPerson.setDocumentRG(personDto.getSecondaryDocument());

        individualPersonDomainService.validateIndividualPerson(individualPerson);

        repository.save(individualPerson);

        return individualPerson;
    }
}
