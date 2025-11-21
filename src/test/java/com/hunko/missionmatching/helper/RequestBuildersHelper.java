package com.hunko.missionmatching.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class RequestBuildersHelper {

    private final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static MockMvcRequestBuilderAuthWrapper get(String uriTemplate, Object... uriVariables) {
        return new MockMvcRequestBuilderAuthWrapper(MockMvcRequestBuilders.get(uriTemplate, uriVariables));
    }

    public static MockMvcRequestBuilderAuthWrapper post(String uriTemplate, Object... uriVariables) {
        return new MockMvcRequestBuilderAuthWrapper(MockMvcRequestBuilders.post(uriTemplate, uriVariables));
    }

    public static MockMvcRequestBuilderAuthWrapper delete(String uriTemplate, Object... uriVariables) {
        return new MockMvcRequestBuilderAuthWrapper(MockMvcRequestBuilders.delete(uriTemplate, uriVariables));
    }

    public static MockMvcRequestBuilderAuthWrapper put(String uriTemplate, Object... uriVariables) {
        return new MockMvcRequestBuilderAuthWrapper(MockMvcRequestBuilders.put(uriTemplate, uriVariables));
    }

    public static class MockMvcRequestBuilderAuthWrapper {

        private final MockHttpServletRequestBuilder requestBuilder;

        public MockMvcRequestBuilderAuthWrapper(MockHttpServletRequestBuilder requestBuilder) {
            this.requestBuilder = requestBuilder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }

        public MockMvcRequestBuilderWrapper nonAuthenticated() {
            return new MockMvcRequestBuilderWrapper(requestBuilder);
        }

        public MockMvcRequestBuilderWrapper authentication(String userId, String... roles) {
            return new MockMvcRequestBuilderWrapper(this.requestBuilder
                    .header("X-USER-ID", userId)
                    .header("X-USER-ROLE", String.join(",", roles))
            );
        }
    }

    public static class MockMvcRequestBuilderWrapper implements RequestBuilder {

        private MockHttpServletRequestBuilder requestBuilder;

        public MockMvcRequestBuilderWrapper(MockHttpServletRequestBuilder requestBuilder) {
            this.requestBuilder = requestBuilder;
        }

        public MockMvcRequestBuilderWrapper header(String headerName, String headerValue) {
            this.requestBuilder = this.requestBuilder.header(headerName, headerValue);
            return this;
        }

        public MockMvcRequestBuilderWrapper content(Object content) throws JsonProcessingException {
            String requestBody = objectMapper.writeValueAsString(content);
            this.requestBuilder = this.requestBuilder.content(requestBody);
            return this;
        }

        @Override
        public MockHttpServletRequest buildRequest(ServletContext servletContext) {
            return requestBuilder.buildRequest(servletContext);
        }

        public MockMvcRequestBuilderWrapper param(String key, Object value) {
            this.requestBuilder = this.requestBuilder.param(key, value.toString());
            return this;
        }
    }
}
