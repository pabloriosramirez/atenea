package online.grisk.atenea.integration.activator.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import online.grisk.atenea.domain.entity.DataIntegration;
import online.grisk.atenea.domain.entity.Microservice;
import online.grisk.atenea.domain.entity.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.*;

@Component
public class SimulatorBureauServiceActivator {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Microservice microserviceHefesto;
    @Autowired
    RestTemplate restTemplate;


    public Map<String, Object> invokeProcessDataIntegration (@Payload Map<String, Object> payload, @Headers Map<String, Object> headers) throws Exception {
        DataIntegration configuration = objectMapper.convertValue(((Map) payload.get("dataintegration")).get("configuration"), DataIntegration.class);
        if(configuration.isBureau()){
            Map<String, Object> responseBureau = invokeReportDataIntegrationBureau(payload, headers);
            payload.put("values", this.extractVariables(responseBureau, new ArrayList(configuration.getVariableCollection())));
        }
        return payload;
    }


    public List<Map> extractVariables(Map<String, Object> responseBureau, List<Variable> variableCollection){
        try {
            List<Map> values = new ArrayList<>();
            Map attributes = (Map) ((Map) ((Map) responseBureau.get("bureau")).get("applicant")).get("attribute");
            for (Variable variable : variableCollection) {
                Map<String, Object> value = new HashMap<>();
                value.put("code", variable.getCode());
                value.put("type", variable.getTypeVariable().getName());
                value.put("value", attributes.get(variable.getCode()).toString());
                value.put("isValueDefault", false);
                values.add(value);
            }
            return values;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }



    public Map<String, Object> invokeReportDataIntegrationBureau(@Payload Map<String, Object> payload, @Headers Map<String, Object> headers) throws Exception {
        HttpEntity<Object> httpEntity = this.buildHttpEntity(((Map<String, Object>)payload.get("applicant")), new HttpHeaders(), microserviceHefesto);
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
