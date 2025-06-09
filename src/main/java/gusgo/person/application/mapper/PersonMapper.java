package gusgo.person.application.mapper;

import gusgo.person.application.dto.AddressDTO;
import gusgo.person.application.dto.EmailDTO;
import gusgo.person.application.dto.PersonDTO;
import gusgo.person.application.dto.PhoneDTO;
import gusgo.person.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "isCustomer", expression = "java(person.getIsCustomer() != null && person.getIsCustomer() ? \"YES\" : \"NO\")")
    @Mapping(target = "isProvider", expression = "java(person.getIsProvider() != null && person.getIsProvider() ? \"YES\" : \"NO\")")
    @Mapping(target = "isBranch", expression = "java(person.getIsBranch() != null && person.getIsBranch() ? \"YES\" : \"NO\")")
    @Mapping(target = "type", expression = "java(\"BUSINESS\")")
    @Mapping(source = "documentCNPJ", target = "mainDocument")
    @Mapping(source = "documentIE", target = "secondaryDocument")
    PersonDTO businessPersonToDTO(BusinessPerson person);

    List<PersonDTO> businessPersonToDTO(List<BusinessPerson> people);

    @Mapping(target = "isCustomer", expression = "java(person.getIsCustomer() != null && person.getIsCustomer() ? \"YES\" : \"NO\")")
    @Mapping(target = "isProvider", expression = "java(person.getIsProvider() != null && person.getIsProvider() ? \"YES\" : \"NO\")")
    @Mapping(target = "isBranch", expression = "java(\"NO\")")
    @Mapping(target = "type", expression = "java(\"INDIVIDUAL\")")
    @Mapping(source = "documentCPF", target = "mainDocument")
    @Mapping(source = "documentRG", target = "secondaryDocument")
    PersonDTO individualPersonToDTO(IndividualPerson person);

    List<PersonDTO> individualPersonToDTO(List<IndividualPerson> people);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(target = "isCustomer", expression = "java(String.valueOf(\"YES\").equalsIgnoreCase(personDto.getIsCustomer()))")
    @Mapping(target = "isProvider", expression = "java(String.valueOf(\"YES\").equalsIgnoreCase(personDto.getIsProvider()))")
    @Mapping(target = "isBranch", expression = "java(String.valueOf(\"YES\").equalsIgnoreCase(personDto.getIsBranch()))")
    @Mapping(source = "mainDocument", target = "documentCNPJ")
    @Mapping(source = "secondaryDocument", target = "documentIE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BusinessPerson toBusinessPerson(PersonDTO personDto);

    @Mapping(target = "isCustomer", expression = "java(String.valueOf(\"YES\").equalsIgnoreCase(personDto.getIsCustomer()))")
    @Mapping(target = "isProvider", expression = "java(String.valueOf(\"YES\").equalsIgnoreCase(personDto.getIsProvider()))")
    @Mapping(source = "mainDocument", target = "documentCPF")
    @Mapping(source = "secondaryDocument", target = "documentRG")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    IndividualPerson toIndividualPerson(PersonDTO personDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address toAddress(AddressDTO addressDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Phone toPhone(PhoneDTO phoneDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Email toEmail(EmailDTO emailDto);

}