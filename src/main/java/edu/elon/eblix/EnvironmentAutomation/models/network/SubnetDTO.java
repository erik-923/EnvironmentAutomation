package edu.elon.eblix.EnvironmentAutomation.models.network;

import edu.elon.eblix.EnvironmentAutomation.models.enums.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SubnetDTO {
    private String subnetId;
    private String subnetArn;
    private String availabilityZone;
    private String cidrBlock;
    private Visibility visibility;
    private List<RouteTableDTO> routeTables;
}
