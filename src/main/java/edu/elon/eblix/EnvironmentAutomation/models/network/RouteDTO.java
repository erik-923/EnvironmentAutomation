package edu.elon.eblix.EnvironmentAutomation.models.network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RouteDTO {
    private String destinationCidrBlock;
    private String gatewayId;
    private String natGatewayId;
}

