package edu.elon.eblix.EnvironmentAutomation.services.instance;

import edu.elon.eblix.EnvironmentAutomation.models.instance.IpDetail;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.model.Instance;
import edu.elon.eblix.EnvironmentAutomation.models.instance.InstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.ec2.model.StartInstancesResponse;
import software.amazon.awssdk.services.ec2.model.StopInstancesResponse;
import software.amazon.awssdk.services.ec2.model.TerminateInstancesResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstanceDTOService {
    @Autowired
    private AWSInstanceService instanceService;

    private InstanceDTO convertInstanceToDTO(Instance instance) {
        InstanceDTO instanceDTO = new InstanceDTO();
        // set every field in the new dto to the corresponding field in the AWS instance
        instanceDTO.setInstanceId(instance.instanceId());
        instanceDTO.setVpcId(instance.vpcId());
        instanceDTO.setSubnetId(instance.subnetId());
        instanceDTO.setState(instance.state().nameAsString());

        IpDetail ipDetail = new IpDetail();
        ipDetail.setPrivateIPAddress(instance.privateIpAddress());
        ipDetail.setPublicIPAddress(instance.publicIpAddress());
        ipDetail.setPrivateDNSName(instance.privateDnsName());
        ipDetail.setPublicDNSName(instance.publicDnsName());

        instanceDTO.setIpDetail(ipDetail);
        instanceDTO.setPlatform(instance.platformDetails());
        instanceDTO.setInstanceType(instance.instanceTypeAsString());
        instanceDTO.setLaunchTime(instance.launchTime());


        return instanceDTO;
    }

    private List<InstanceDTO> convertInstanceList(List<Instance> instanceList) {
        List<InstanceDTO> instanceDTOList = new ArrayList<>();
        instanceList.forEach(instance -> {
            instanceDTOList.add(convertInstanceToDTO(instance));
        });
        return instanceDTOList;
    }

    public List<InstanceDTO> getInstances() {
        return convertInstanceList(this.instanceService.getInstances());
    }

    public InstanceDTO getInstance(String instanceId) {
        return convertInstanceToDTO(this.instanceService.getInstance(instanceId));
    }

    public InstanceDTO createInstance(InstanceDTO instance) {
        return convertInstanceToDTO(this.instanceService.createInstance(instance).instances().get(0));
    }

    public InstanceDTO startInstance(String instanceId) {
        StartInstancesResponse response = this.instanceService.startInstance(instanceId);
        return convertInstanceToDTO(this.instanceService.getInstance(instanceId));
    }

    public InstanceDTO stopInstance(String instanceId) {
        StopInstancesResponse response = this.instanceService.stopInstance(instanceId);
        return convertInstanceToDTO(this.instanceService.getInstance(instanceId));
    }

    public InstanceDTO terminateInstance(String instanceId) {
        TerminateInstancesResponse response = this.instanceService.terminateInstance(instanceId);
        return convertInstanceToDTO(this.instanceService.getInstance(instanceId));
    }
}
