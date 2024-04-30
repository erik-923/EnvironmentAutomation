package edu.elon.eblix.EnvironmentAutomation.controllers;

import edu.elon.eblix.EnvironmentAutomation.models.network.SubnetDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.VpcDTO;
import edu.elon.eblix.EnvironmentAutomation.services.network.NetworkDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ec2automation/network")
public class AWSNetworkController {
    @Autowired
    private NetworkDTOService networkService;

    @GetMapping("/list")
    public List<VpcDTO> getVpcs() {
        return this.networkService.getVpcs();
    }

    @GetMapping("/{vpcId}")
    public VpcDTO getVpc(@PathVariable String vpcId) {
        return this.networkService.getVpc(vpcId);
    }

    @PostMapping("")
    public VpcDTO createVpc(@RequestBody VpcDTO vpc) {
        return this.networkService.createVpc(vpc);
    }

    @GetMapping("/{vpcId}/subnet/list")
    public List<SubnetDTO> getSubnets(@PathVariable String vpcId) {
        return this.networkService.getSubnets(vpcId);
    }

    @PostMapping("/{vpcId}/subnet")
    public SubnetDTO createSubnet(@PathVariable String vpcId, @RequestBody SubnetDTO subnet) {
        return this.networkService.createSubnet(subnet, vpcId);
    }

    @GetMapping("/{vpcId}/subnet/{subnetId}")
    public SubnetDTO getSubnet(@PathVariable("vpcId") String vpcId, @PathVariable("subnetId") String subnetId) {
        return this.networkService.getSubnet(vpcId, subnetId);
    }

    @DeleteMapping("/{vpcId}/subnet/{subnetId}")
    public VpcDTO deleteSubnet(@PathVariable("vpcId") String vpcId, @PathVariable("subnetId") String subnetId) {
        return this.networkService.deleteSubnet(vpcId, subnetId);
    }
}
