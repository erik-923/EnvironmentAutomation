package edu.elon.eblix.EnvironmentAutomation.models.network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RouteTableDTO {
    private String routeTableId;
    private List<RouteDTO> routes;
}
