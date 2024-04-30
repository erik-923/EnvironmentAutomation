package edu.elon.eblix.EnvironmentAutomation.models.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class InstanceDTO {
    private String instanceId;
    private String vpcId;
    private String subnetId;
    private String state;
    private IpDetail ipDetail;
    private String platform;
    private String instanceType;
    private Instant launchTime;
}
