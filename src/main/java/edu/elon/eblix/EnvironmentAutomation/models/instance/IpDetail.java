package edu.elon.eblix.EnvironmentAutomation.models.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class IpDetail {
    private String privateIPAddress;
    private String privateDNSName;
    private String publicIPAddress;
    private String publicDNSName;
}
