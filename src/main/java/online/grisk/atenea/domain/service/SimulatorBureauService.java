package online.grisk.atenea.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import online.grisk.atenea.domain.entity.Microservice;
import online.grisk.atenea.domain.entity.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulatorBureauService {

    @Autowired
    Microservice microserviceHefesto;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public List<Map> extractVariables(Map<String, Object> responseBureau, List<Variable> variableCollection) {
        List<Map> values = new ArrayList<>();
        Map attributes = (Map) ((Map) ((Map) responseBureau.get("bureau")).get("applicant")).get("attribute");
        for (Variable variable : variableCollection) {
            Map<String, Object> value = new HashMap<>();
            value.put("code", variable.getCode().toUpperCase());
            value.put("type", variable.getTypeVariable().getCode());
            try {
                value.put("value", attributes.get(variable.getCode().toLowerCase()).toString());
            } catch (Exception e) {
                System.out.println(variable.getCode().toLowerCase());
            }
            value.put("isValueDefault", false);
            values.add(value);
        }
        return values;
    }


    public Map<String, Object> invokeReportDataIntegrationBureau(@Payload Map<String, Object> payload, @Headers Map<String, Object> headers) throws Exception {
        HttpEntity<Object> httpEntity = this.buildHttpEntity(((Map<String, Object>) payload.get("applicant")), new HttpHeaders(), microserviceHefesto);
        ResponseEntity<Map<String, Object>> response = this.executeRequest(microserviceHefesto, httpEntity);
        return response.getBody();
    }


    protected ResponseEntity<Map<String, Object>> executeRequest(Microservice microservice, HttpEntity<Object> httpEntity) throws Exception {
        ResponseEntity response;
        try {
            response = this.restTemplate.exchange(microservice.getUri(), HttpMethod.POST, httpEntity, Map.class);
        } catch (RestClientResponseException e) {
            throw new Exception(this.buildErrorMessage(microservice.getServiceId(), e));
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No instances available for " + microservice.getServiceId());
        } catch (Exception e) {
            throw new Exception();
        }
        return response;
    }

    protected HttpEntity<Object> buildHttpEntity(Map<String, Object> payload, HttpHeaders headers, Microservice microservice) {
        HttpHeaders httpHeaders = createHttpHeaders(headers, microservice);
        return new HttpEntity<>(payload, httpHeaders);
    }

    private HttpHeaders createHttpHeaders(HttpHeaders headers, Microservice microservice) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers.get("action") != null) {
            httpHeaders.add("action", headers.get("action").toString());
        }
        httpHeaders.setBasicAuth(microservice.getServiceUsername(), microservice.getServicePassword());
        return httpHeaders;
    }

    private String buildErrorMessage(String nameServiceActivator, RestClientResponseException e) throws Exception {
        JsonNode jsonNode = this.objectMapper.readTree(e.getResponseBodyAsString());
        return jsonNode.get("message") != null ? String.format("An error ocurred executing %s service activator: %S (STATUS: %d)", nameServiceActivator, jsonNode.get("message").asText(), e.getRawStatusCode()) : String.format("An error ocurred executing %s service activator: %S (STATUS: %d)", nameServiceActivator, e.getMessage(), e.getRawStatusCode());
    }

    protected void addServiceResponseToResponseMap(Map<String, Object> payload, ResponseEntity<Map<String, Object>> response, String serviceId) {
        payload.put(serviceId.toLowerCase() + "_" + "response", response.getBody());
        payload.put("current_response", response.getBody());
        payload.put("status", response.getStatusCodeValue());
    }

}
