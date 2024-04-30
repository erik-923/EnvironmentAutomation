package edu.elon.eblix.EnvironmentAutomation.models.environment;

import edu.elon.eblix.EnvironmentAutomation.models.instance.InstanceDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.VpcDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class EnvironmentDTO {
    private List<VpcDTO> networks;
    private List<InstanceDTO> instances;
}
