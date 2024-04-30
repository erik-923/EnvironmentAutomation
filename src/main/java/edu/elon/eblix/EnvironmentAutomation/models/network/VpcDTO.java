package edu.elon.eblix.EnvironmentAutomation.models.network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class VpcDTO {
    private String vpcId;
    private String cidrBlock;
    private List<SubnetDTO> subnets;
}
