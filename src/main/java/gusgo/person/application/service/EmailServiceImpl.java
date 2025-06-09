package gusgo.person.application.service;

import gusgo.person.application.dto.EmailDTO;
import gusgo.person.application.interfaces.EmailRepository;
import gusgo.person.application.interfaces.EmailService;
import gusgo.person.application.resources.ValidationConstants;
import gusgo.person.domain.model.Email;
import gusgo.person.domain.model.Person;
import gusgo.person.rest.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository repository;

    @Override
    public void update(Person person, List<EmailDTO> emailDTOS) {
        if (emailDTOS.isEmpty()) {
            throw new BusinessException(ValidationConstants.EMAIL_IS_MANDATORY);
        }

        List<Email> existingEmails = repository.findAll();
        var existingEmailMap = existingEmails.stream().collect(Collectors.toMap(Email::getId, email -> email));

        if (person.getEmails() == null) {
            person.setEmails(new ArrayList<>());
        }
        person.getEmails().clear();

        emailDTOS.forEach(emailDTO -> {
            if (emailDTO.getId() == null) {
                Email newEmail = createEmailFromDTO(person, emailDTO);
                person.addEmail(newEmail);
            } else {
                Email existingEmail = existingEmailMap.get(emailDTO.getId());
                if (existingEmail != null) {
                    updateEmailFromDTO(existingEmail, emailDTO);
                    person.addEmail(existingEmail);
                }
            }
        });

    }

    private Email createEmailFromDTO(Person person, EmailDTO emailDTO) {
        Email email = new Email();
        email.setEmail(emailDTO.getEmail());
        email.setContact(emailDTO.getContact());
        email.setNote(emailDTO.getNote());
        email.setPerson(person);

        return email;
    }

    private void updateEmailFromDTO(Email existingEmail, EmailDTO emailDTO) {
        existingEmail.setEmail(emailDTO.getEmail());
        existingEmail.setContact(emailDTO.getContact());
        existingEmail.setNote(emailDTO.getNote());
    }
}
