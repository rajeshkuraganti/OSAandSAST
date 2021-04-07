package com.cx.automation.adk;

import com.cx.automation.adk.rest.RESTClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.glassfish.jersey.client.ClientResponse;
import net.lingala.zip4j.exception.ZipException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;



/**
 * Created by aviat.
 * Date: 18/06/2018.
 */
@Ignore
public class wireMockTest {


    @Test
    public void wireMockTestExample() throws UnirestException, IOException, ZipException {

        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8089));
        wireMockServer.start();
        wireMockServer.stubFor(any(urlPathEqualTo("/api/books"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withStatus(200)
                        .withBody("{}")));

        RESTClient restClient = new RESTClient();
        ClientResponse response = restClient.get("http://localhost:8089/api/books", MediaType.APPLICATION_JSON_TYPE);
        Assert.assertEquals(response.getStatus(), 200);
        String a = "\\\\10.45.0.108\\cxsrc";


    }

    @Test
    public void seleniumHeadless(){



    }
}

