package edu.elon.eblix.EnvironmentAutomation.controllers;

import edu.elon.eblix.EnvironmentAutomation.models.environment.EnvironmentDTO;
import edu.elon.eblix.EnvironmentAutomation.services.environment.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ec2automation/environment")
public class AWSEnvironmentController {
    @Autowired
    private EnvironmentService environmentService;

    @GetMapping("")
    public EnvironmentDTO getEnvironment() {
        return this.environmentService.getEnvironment();
    }

    @PostMapping("")
    public EnvironmentDTO createEnvironment(@RequestBody EnvironmentDTO environment) {
        return this.environmentService.createEnvironment(environment);
    }
}
