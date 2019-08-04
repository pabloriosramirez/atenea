package online.grisk.atenea.presentation.controller;

import online.grisk.atenea.integration.activator.impl.SimulatorBureauServiceActivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    SimulatorBureauServiceActivator simulatorBureauServiceActivator;

    @PostMapping(value = "/api/atenea/report")
    public Map<String, Object> report(@NotEmpty @RequestBody Map<String, Object> payload, @NotNull @RequestHeader Map headers) throws Exception {
        return simulatorBureauServiceActivator.invokeProcessDataIntegration(payload, headers);
    }
}
