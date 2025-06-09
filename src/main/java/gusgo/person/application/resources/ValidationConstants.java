package gusgo.person.application.resources;

public class ValidationConstants {

    private ValidationConstants() {}

    public static final String NAME_IS_MANDATORY = isMandatory("Name");

    public static final String ERP_CODE_IS_MANDATORY = isMandatory("ERP Code");

    public static final String ERP_CODE_ALREADY_EXISTS = "erpCode already exists";

    public static final String DOCUMENT_CPF_IS_MANDATORY = isMandatory("Document CPF");

    public static final String DOCUMENT_CNPJ_IS_MANDATORY = isMandatory("Document CNPJ");

    public static final String MAIN_DOCUMENT_ALREADY_EXISTS = "mainDocument already exists";

    public static final String BRANCH_CANNOT_BE_A_CUSTOMER = "branch cannot be a customer";

    public static final String BRANCH_CANNOT_BE_A_PROVIDER = "branch cannot be a provider";

    public static final String PHONE_IS_MANDATORY = isMandatory("At least one phone");

    public static final String EMAIL_IS_MANDATORY = isMandatory("At least one email");

    public static final String ADDRESS_IS_MANDATORY = isMandatory("At least one address");

    public static final String STREET_IS_MANDATORY = isMandatory("Street");

    public static final String NUMBER_IS_MANDATORY = isMandatory("Number");

    public static final String NEIGHBORHOOD_IS_MANDATORY = isMandatory("Neighborhood");

    public static final String ZIP_CODE_IS_MANDATORY = isMandatory("Zip Code");

    public static final String STATE_IS_MANDATORY = isMandatory("State");

    public static final String CITY_IS_MANDATORY = isMandatory("City");

    public static final String INVALID_PERSON_TYPE = "Invalid person type";

    public static final String PERSON_NOT_FOUND = "Person not found";

    public static final String INVALID_FILE = "Invalid file";

    public static String isMandatory(String field) {
        return String.format("%s is mandatory.", field);
    }
}
