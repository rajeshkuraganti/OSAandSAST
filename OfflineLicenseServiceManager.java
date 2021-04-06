package com.cx.automation.installation.licenseGenerator;
import com.cx.automation.installer.offlineLicense.OfflineLicenseFields;
import com.cx.automation.installer.offlineLicense.OfflineLicenseServer;

/**
 * Created by tall on 25/05/2017.
 */
public interface OfflineLicenseServiceManager {

    void generateLicense(OfflineLicenseFields licenseFields, OfflineLicenseServer serverDetails);

}
