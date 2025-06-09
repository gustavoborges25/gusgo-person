package gusgo.person.application.service;

import gusgo.person.application.dto.PhoneDTO;
import gusgo.person.application.interfaces.PhoneRepository;
import gusgo.person.application.interfaces.PhoneService;
import gusgo.person.application.resources.ValidationConstants;
import gusgo.person.domain.model.Person;
import gusgo.person.domain.model.Phone;
import gusgo.person.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository repository;

    @Override
    public void update(Person person, List<PhoneDTO> phoneDTOS) {
        if (phoneDTOS.isEmpty()) {
            throw new BusinessException(ValidationConstants.PHONE_IS_MANDATORY);
        }

        List<Phone> existingPhones = repository.findAll();
        var existingPhoneMap = existingPhones.stream().collect(Collectors.toMap(Phone::getId, phone -> phone));

        if (person.getPhones() == null) {
            person.setPhones(new ArrayList<>());
        }
        person.getPhones().clear();

        phoneDTOS.forEach(phoneDTO -> {
            if (phoneDTO.getId() == null) {
                Phone newPhone = createPhoneFromDTO(person, phoneDTO);
                person.addPhone(newPhone);
            } else {
                Phone existingPhone = existingPhoneMap.get(phoneDTO.getId());
                if (existingPhone != null) {
                    updatePhoneFromDTO(existingPhone, phoneDTO);
                    person.addPhone(existingPhone);
                }
            }
        });

    }

    private Phone createPhoneFromDTO(Person person, PhoneDTO phoneDTO) {
        Phone email = new Phone();
        email.setPhone(phoneDTO.getPhone());
        email.setContact(phoneDTO.getContact());
        email.setNote(phoneDTO.getNote());
        email.setPerson(person);

        return email;
    }

    private void updatePhoneFromDTO(Phone existingPhone, PhoneDTO emailDTO) {
        existingPhone.setPhone(emailDTO.getPhone());
        existingPhone.setContact(emailDTO.getContact());
        existingPhone.setNote(emailDTO.getNote());
    }
}
