package com.cx.automation.installation.licenseGenerator;

/**
 * Created by tall on 22/05/2017.
 */
public enum CXLicenseImages {

    CREATE_BUTTON("createOfflineLicenseButton.png"),
    HID_BUTTON("HIDTextField.png"),
    OSA_ENABLED("OsaDefaultValue.png "),
    LICENSE_OK("licenseWasGeneratedOk.png"),
    LICENSE_WAS_GENERATED("licenseWasGeneratedMessage.png"),
    LICENSE_CLOSE_BUTTON("LicenseCloseButton.png"),
    NUMBER_OF_USERS("numberOfUsers.png"),
    AUDIT_USERS("audit_users.png"),
    LICENSE_EDITION("license_edition.png"),
    LICENSE_EDITION2("license_edition2.png"),
    PROJECT_NUMBER("project_number.png"),
    ALLOWED_LOC("allowed_loc.png"),
    NUMBER_OF_ENGINES("numberOfEngines.png");


    private static final String PATH_PREFIX = "com/cx/automation/CX-Installation/licenseImages/";
    private String licenseFileName;
    private final String relativePath;

    CXLicenseImages(String fileName){
        this.relativePath = PATH_PREFIX + fileName;
        this.licenseFileName=fileName;
    }

    public static String getPathPrefix() {
        return PATH_PREFIX;
    }

    public String getLicensefileName() {
        return licenseFileName;
    }

    public String getRelativePath() {
        return relativePath;
    }
}
