package online.grisk.atenea.integration.activator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.grisk.atenea.domain.entity.DataIntegration;
import online.grisk.atenea.domain.service.SimulatorBureauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class SimulatorBureauServiceActivator {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SimulatorBureauService simulatorBureauService;


    public Map<String, Object> invokeProcessDataIntegration(@Payload Map<String, Object> payload, @Headers Map<String, Object> headers) throws Exception {
        DataIntegration configuration = objectMapper.convertValue(((Map) payload.get("dataintegration")).get("configuration"), DataIntegration.class);
        if (configuration.isBureau()) {
            Map<String, Object> responseBureau = simulatorBureauService.invokeReportDataIntegrationBureau(payload, headers);
            ((Map) payload.get("dataintegration")).put("values", simulatorBureauService.extractVariables(responseBureau, new ArrayList(configuration.getVariableCollection())));
        }
        return payload;
    }

}
