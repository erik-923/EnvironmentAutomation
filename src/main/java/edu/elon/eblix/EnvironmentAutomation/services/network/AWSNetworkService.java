package edu.elon.eblix.EnvironmentAutomation.services.network;

import edu.elon.eblix.EnvironmentAutomation.models.enums.Visibility;
import edu.elon.eblix.EnvironmentAutomation.models.network.SubnetDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.VpcDTO;
import edu.elon.eblix.EnvironmentAutomation.services.instance.AWSInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;

@Service
public class AWSNetworkService {
    private static final Logger logger = LoggerFactory.getLogger(AWSNetworkService.class);
    private Ec2Client ec2Client;

    @Autowired
    public AWSNetworkService(Ec2Client ec2Client) {
        this.ec2Client = ec2Client;
    }

    public List<Vpc> getVpcs() {
        DescribeVpcsRequest describeVpcsRequest = DescribeVpcsRequest.builder()
                .build();

        return this.ec2Client.describeVpcs(describeVpcsRequest).vpcs();
    }

    public Vpc getVpc(String vpcId) {
        List<Vpc> vpcList = getVpcs();
        for (Vpc vpc: vpcList) {
            if (vpc.vpcId().equals(vpcId)) {
                return vpc;
            }
        }
        return null;
    }

    public Vpc createVpc(VpcDTO vpc) {
        CreateVpcRequest createVpcRequest = CreateVpcRequest.builder()
                .cidrBlock(vpc.getCidrBlock())
                .build();
        CreateVpcResponse createVpcResponse = ec2Client.createVpc(createVpcRequest);
        return createVpcResponse.vpc();
    }

    public Subnet createSubnet(SubnetDTO subnet, String vpcId) {
        CreateSubnetRequest createSubnetRequest = CreateSubnetRequest.builder()
                .cidrBlock(subnet.getCidrBlock())
                .vpcId(vpcId)
                .build();
        CreateSubnetResponse createSubnetResponse = ec2Client.createSubnet(createSubnetRequest);
        String gatewayId;
        if (subnet.getVisibility() == Visibility.Public) {
            DescribeInternetGatewaysRequest request = DescribeInternetGatewaysRequest.builder()
                    .filters(f -> f.name("attachment.vpc-id").values(vpcId))
                    .build();
            DescribeInternetGatewaysResponse response = ec2Client.describeInternetGateways(request);

            if (response.internetGateways().size() > 0) {
                gatewayId = response.internetGateways().get(0).internetGatewayId();
            } else {
                CreateInternetGatewayRequest createInternetGatewayRequest = CreateInternetGatewayRequest.builder()
                        .build();
                gatewayId = ec2Client.createInternetGateway(createInternetGatewayRequest).internetGateway().internetGatewayId();
                AttachInternetGatewayRequest attachInternetGatewayRequest = AttachInternetGatewayRequest.builder()
                        .vpcId(vpcId)
                        .internetGatewayId(gatewayId)
                        .build();
                ec2Client.attachInternetGateway(attachInternetGatewayRequest);
            }
        } else {
            DescribeNatGatewaysRequest request = DescribeNatGatewaysRequest.builder()
                    .filter(f -> f.name("vpc-id").values(vpcId))
                    .build();
            DescribeNatGatewaysResponse response = ec2Client.describeNatGateways(request);
            if (response.natGateways().size() > 0) {
                gatewayId = response.natGateways().get(0).natGatewayId();
            } else {
                CreateNatGatewayRequest createNatGatewayRequest = CreateNatGatewayRequest.builder()
                        .connectivityType("private")
                        .subnetId(createSubnetResponse.subnet().subnetId())
                        .build();
                gatewayId = ec2Client.createNatGateway(createNatGatewayRequest).natGateway().natGatewayId();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        String routeTableId = createRouteTable(vpcId);
        createRoute(routeTableId, gatewayId);
        AssociateRouteTableRequest associateRouteTableRequest = AssociateRouteTableRequest.builder()
                .routeTableId(routeTableId)
                .subnetId(createSubnetResponse.subnet().subnetId())
                .build();
        ec2Client.associateRouteTable(associateRouteTableRequest);
        return createSubnetResponse.subnet();
    }

    public Subnet getSubnet(String vpcId, String subnetId) {
        List<Subnet> subnetList = getVpcSubnets(vpcId);
        for (Subnet subnet: subnetList) {
            if (subnet.subnetId().equals(subnetId)) {
                return subnet;
            }
        }
        return null;
    }

    public Vpc deleteSubnet(String vpcId, String subnetId) {
        DeleteSubnetRequest deleteSubnetRequest = DeleteSubnetRequest.builder()
                .subnetId(subnetId)
                .build();
        ec2Client.deleteSubnet(deleteSubnetRequest);
        return getVpc(vpcId);
    }

    private String createRouteTable(String vpcId) {
        CreateRouteTableRequest request = CreateRouteTableRequest.builder()
                .vpcId(vpcId)
                .build();
        return ec2Client.createRouteTable(request).routeTable().routeTableId();
    }

    private void createRoute(String routeTableId, String gatewayId) {
        CreateRouteRequest request = CreateRouteRequest.builder()
                .routeTableId(routeTableId)
                .destinationCidrBlock("0.0.0.0/0")
                .gatewayId(gatewayId)
                .build();
        ec2Client.createRoute(request);
    }

    public Visibility getSubnetVisibility(Subnet subnet) {
        List<RouteTable> routeTables = getSubnetRouteTables(subnet.subnetId());
        for (RouteTable routeTable : routeTables) {
            List<Route> routes = routeTable.routes();
            for (Route route : routes) {
                if (route.destinationCidrBlock().equals("0.0.0.0/0") && route.gatewayId() != null) {
                    return Visibility.Public;
                }
            }
        }
        return Visibility.Private;
    }

    public List<RouteTable> getSubnetRouteTables(String subnetId) {
        DescribeRouteTablesRequest request = DescribeRouteTablesRequest.builder()
                .maxResults(10)
                .filters(Filter.builder().name("association.subnet-id").values(subnetId).build())
                .build();
        DescribeRouteTablesResponse response = ec2Client.describeRouteTables(request);
        return response.routeTables();
    }

    public List<Subnet> getVpcSubnets(String vpcId) {
        DescribeSubnetsRequest request = DescribeSubnetsRequest.builder()
                .filters(Filter.builder().name("vpc-id").values(vpcId).build())
                .build();
        DescribeSubnetsResponse response = ec2Client.describeSubnets(request);
        return response.subnets();
    }
}
