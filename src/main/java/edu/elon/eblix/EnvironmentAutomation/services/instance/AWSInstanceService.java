package edu.elon.eblix.EnvironmentAutomation.services.instance;

import edu.elon.eblix.EnvironmentAutomation.models.instance.InstanceDTO;
import software.amazon.awssdk.services.ec2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeImagesRequest;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AWSInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(AWSInstanceService.class);
    private Ec2Client ec2Client;

    @Autowired
    public AWSInstanceService(Ec2Client ec2Client) {
        this.ec2Client = ec2Client;
    }

    public List<Instance> getInstances() throws Ec2Exception {
        List<Instance> instanceList = new ArrayList<>();
        DescribeInstancesResponse instanceListResponse = this.ec2Client.describeInstances();
        for (Reservation reservation : instanceListResponse.reservations()) {
            instanceList.addAll(reservation.instances());
        }
        return instanceList;
    }

    public Instance getInstance(String instanceId) {
        DescribeInstancesRequest describeInstanceRequest = DescribeInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        DescribeInstancesResponse describeInstancesResponse = ec2Client.describeInstances(describeInstanceRequest);

        for (Reservation reservation : describeInstancesResponse.reservations()) {
            for (Instance instance : reservation.instances()) {
                if (instance.instanceId().equals(instanceId)) {
                    return instance;
                }
            }
        }
        return null;
    }

    public RunInstancesResponse createInstance(InstanceDTO instance) {
        RunInstancesRequest runInstancesRequest = RunInstancesRequest.builder()
                .imageId("ami-051f8a213df8bc089")
                .instanceType(InstanceType.fromValue(instance.getInstanceType()))
                .subnetId(instance.getSubnetId())
                .maxCount(1)
                .minCount(1)
                .build();

        RunInstancesResponse runInstancesResponse = ec2Client.runInstances(runInstancesRequest);

        return runInstancesResponse;
    }

    public StartInstancesResponse startInstance(String instanceId) {
        StartInstancesRequest startInstancesRequest = StartInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        return ec2Client.startInstances(startInstancesRequest);
    }

    public StopInstancesResponse stopInstance(String instanceId) {
        StopInstancesRequest request = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        return ec2Client.stopInstances(request);
    }

    public TerminateInstancesResponse terminateInstance(String instanceId) {
        TerminateInstancesRequest terminateInstancesRequest = TerminateInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        return ec2Client.terminateInstances(terminateInstancesRequest);
    }
}
