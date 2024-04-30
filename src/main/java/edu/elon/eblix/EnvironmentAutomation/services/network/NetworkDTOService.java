package edu.elon.eblix.EnvironmentAutomation.services.network;

import edu.elon.eblix.EnvironmentAutomation.models.network.RouteDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.RouteTableDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.SubnetDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.VpcDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class NetworkDTOService {
    @Autowired
    private AWSNetworkService networkService;

    public List<VpcDTO> getVpcs() {
        return convertVpcList(this.networkService.getVpcs());
    }

    public VpcDTO getVpc(String vpcId) {
        return convertVpcToDTO(this.networkService.getVpc(vpcId));
    }

    public VpcDTO createVpc(VpcDTO vpc) {
        return convertVpcToDTO(this.networkService.createVpc(vpc));
    }

    public List<SubnetDTO> getSubnets(String vpcId) {
        return convertSubnetsList(this.networkService.getVpcSubnets(vpcId));
    }

    public SubnetDTO createSubnet(SubnetDTO subnet, String vpcId) {
        return convertSubnetToDTO(this.networkService.createSubnet(subnet, vpcId));
    }

    public SubnetDTO getSubnet(String vpcId, String subnetId) {
        return convertSubnetToDTO(this.networkService.getSubnet(vpcId, subnetId));
    }

    public VpcDTO deleteSubnet(String vpcId, String subnetId) {
        return convertVpcToDTO(this.networkService.deleteSubnet(vpcId, subnetId));
    }

    private VpcDTO convertVpcToDTO(Vpc vpc) {
        VpcDTO vpcDTO = new VpcDTO();

        vpcDTO.setVpcId(vpc.vpcId());
        vpcDTO.setCidrBlock(vpc.cidrBlock());
        vpcDTO.setSubnets(convertSubnetsList(this.networkService.getVpcSubnets(vpc.vpcId())));

        return vpcDTO;
    }

    private List<VpcDTO> convertVpcList(List<Vpc> vpcList) {
        List<VpcDTO> vpcDTOList = new ArrayList<>();
        vpcList.forEach(vpc -> {
            vpcDTOList.add(convertVpcToDTO(vpc));
        });
        return vpcDTOList;
    }

    private SubnetDTO convertSubnetToDTO(Subnet subnet) {
        SubnetDTO subnetDTO = new SubnetDTO();

        subnetDTO.setSubnetId(subnet.subnetId());
        subnetDTO.setSubnetArn(subnet.subnetArn());
        subnetDTO.setAvailabilityZone(subnet.availabilityZone());
        subnetDTO.setCidrBlock(subnet.cidrBlock());
        subnetDTO.setVisibility(this.networkService.getSubnetVisibility(subnet));
        subnetDTO.setRouteTables(convertRouteTableList(this.networkService.getSubnetRouteTables(subnet.subnetId())));
        return subnetDTO;
    }

    private List<SubnetDTO> convertSubnetsList(List<Subnet> subnetList) {
        List<SubnetDTO> subnetDTOList = new ArrayList<>();
        subnetList.forEach(subnet -> {
            subnetDTOList.add(convertSubnetToDTO(subnet));
        });
        return subnetDTOList;
    }

    private RouteTableDTO convertRouteTableToDTO(RouteTable routeTable) {
        RouteTableDTO routeTableDTO = new RouteTableDTO();

        routeTableDTO.setRouteTableId(routeTable.routeTableId());
        routeTableDTO.setRoutes(convertRouteList(routeTable.routes()));
        return routeTableDTO;
    }

    private List<RouteTableDTO> convertRouteTableList(List<RouteTable> routeTableList) {
        List<RouteTableDTO> routeTableDTOList = new ArrayList<>();
        routeTableList.forEach(routeTable -> {
            routeTableDTOList.add(convertRouteTableToDTO(routeTable));
        });
        return routeTableDTOList;
    }

    private RouteDTO convertRouteToDTO(Route route) {
        RouteDTO routeDTO = new RouteDTO();

        routeDTO.setDestinationCidrBlock(route.destinationCidrBlock());
        routeDTO.setGatewayId(route.gatewayId());
        routeDTO.setNatGatewayId(route.natGatewayId());
        return routeDTO;
    }

    private List<RouteDTO> convertRouteList(List<Route> routeList) {
        List<RouteDTO> routeDTOList = new ArrayList<>();
        routeList.forEach(route -> {
            routeDTOList.add(convertRouteToDTO(route));
        });
        return routeDTOList;
    }
}
