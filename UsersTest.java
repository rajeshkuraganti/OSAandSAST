//package com.cx.automation.dbtest;
//
//import com.cx.automation.adk.systemtest.runner.E2ETest;
//import com.cx.automation.adk.systemtest.runner.E2ETestSpringRunner;
//import com.cx.automation.persistence.appdatabase.configuration.ConfigurationDAOService;
//import com.cx.automation.persistence.appdatabase.configuration.dto.EngineConfigurationKeysMeta;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.IfProfileValue;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import static com.cx.automation.topology.EnvironmentConstants.ENVIRONMENT;
//import static com.cx.automation.topology.EnvironmentConstants.EXTERNAL_ENV;
//
///**
// * Created by Dima Masis
// * Date 18/10/2016
// */
//
//@RunWith(E2ETestSpringRunner.class)
//@ContextConfiguration(locations = {"classpath:com/cx/automation/persistence/cx-persistence-context.xml"})
//@TransactionConfiguration(defaultRollback = false)
//public class UsersTest {
//
//    @Autowired
//    private ConfigurationDAOService configurationDAOService;
//
//
//    @Transactional
//    @Before
//    public void doBeforeTest() {
//
//
//        EngineConfigurationKeysMeta before_update = configurationDAOService.getEngineConfigurationValueByKey("MULTI_LANGUAGE_MODE");
//        System.err.println("BEFORE UPDATE: " + before_update.getDefaultValue() + " key: " + before_update.getKeyName());
//
//        String changedVal;
//        if (before_update.getDefaultValue().equals("1")) {
//            changedVal = "2";
//        } else {
//            changedVal = "1";
//        }
//        configurationDAOService.updateEngineConfigurationKeysMeta("MULTI_LANGUAGE_MODE", changedVal);
//
//        EngineConfigurationKeysMeta after_update = configurationDAOService.getEngineConfigurationValueByKey("MULTI_LANGUAGE_MODE");
//        System.err.println("AFTER UPDATE: " + after_update.getDefaultValue() + " key: " + after_update.getKeyName());
//
//    }
//
//    @Test
//    @E2ETest
//    @IfProfileValue(name = ENVIRONMENT, values = {EXTERNAL_ENV})
//    public void usersDAOTest() {
//        System.out.println("Test 1");
//
//    }
//
//    @Test
//    @E2ETest(dependsOn = "usersDAOTest")
//    @IfProfileValue(name = ENVIRONMENT, values = {EXTERNAL_ENV})
//    public void usersDAOTest_2() {
//        System.out.println("Test 2");
//    }
//
//}
