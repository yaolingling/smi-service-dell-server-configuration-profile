package com.dell.isg.smi.service.configuration;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dell.isg.smi.service.server.configuration.Application;

import io.github.robwin.swagger2markup.Swagger2MarkupConverter;

//@WebAppConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = Application.class)
public class Swagger2MarkupTest {

    private static final String API_URI = "http://localhost:46018/v2/api-docs?group=server-configuration";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    // @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }


    // @Test
    public void convertSwaggerToAsciiDoc() throws IOException {

        Swagger2MarkupConverter.from(API_URI).build().intoFolder("src/docs/asciidoc/generated");

        // Swagger2MarkupResultHandler.Builder builder = Swagger2MarkupResultHandler
        // .outputDirectory(outputDirForFormat("asciidoc"));
        // mockMvc.perform(get(API_URI).accept(MediaType.APPLICATION_JSON))
        // .andDo(builder.build())
        // .andExpect(status().isOk());

    }

}