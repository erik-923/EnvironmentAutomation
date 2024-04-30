package edu.elon.eblix.EnvironmentAutomation.controllers;

import edu.elon.eblix.EnvironmentAutomation.models.instance.InstanceDTO;
import edu.elon.eblix.EnvironmentAutomation.services.instance.InstanceDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ec2automation/instance")
public class AWSInstanceController {
    @Autowired
    private InstanceDTOService instanceService;

    @GetMapping("/list")
    public List<InstanceDTO> listInstances() {
        return this.instanceService.getInstances();
    }

    @GetMapping("/{instanceId}")
    public InstanceDTO getInstance(@PathVariable String instanceId) {
        return this.instanceService.getInstance(instanceId);
    }

    @PostMapping("")
    public InstanceDTO createInstance(@RequestBody InstanceDTO instance) {
        return this.instanceService.createInstance(instance);
    }

    @GetMapping("/{instanceId}/start")
    public InstanceDTO startInstance(@PathVariable String instanceId) {
        return this.instanceService.startInstance(instanceId);
    }

    @GetMapping("/{instanceId}/stop")
    public InstanceDTO stopInstance(@PathVariable String instanceId) {
        return this.instanceService.stopInstance(instanceId);
    }

    @GetMapping("/{instanceId}/terminate")
    public InstanceDTO terminateInstance(@PathVariable String instanceId) {
        return this.instanceService.terminateInstance(instanceId);
    }
}
