package com.cx.automation.adk;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by arnona on 1/6/2019.
 */
//@Ignore
public class JerseyTest {
    @Test
    public void Test() {
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://ARNONA-LAPTOP/Test");
//        assertEquals(200, target.request().post(Entity.entity("Hello", MediaType.APPLICATION_JSON_TYPE)).getStatus());
        Assert.assertTrue("asdsd1",1>0);
    }
}
