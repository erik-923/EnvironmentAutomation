# AWS Environment Automation REST API using SpringBoot and AWS SDK

## Overview
This REST API provides a comprehensive solution for managing AWS environments, instances, and networks using SpringBoot and the AWS SDK. It offers a range of features for managing EC2 instances, VPCs, subnets, and environments, making it an ideal choice for automating AWS resources.

## Endpoints

### Instance Management
- Retrieve All Instances: `GET /ec2automation/instance/list`
- Retrieve a Single Instance: `GET /ec2automation/instance/{instanceId}`
- Create an Instance: `POST /ec2automation/instance`
- Start an Instance: `GET /ec2automation/instance/{instanceId}/start`
- Stop an Instance: `GET /ec2automation/instance/{instanceId}/stop`
- Terminate an Instance: `GET /ec2automation/instance/{instanceId}/terminate`

### Network Management
- Get all VPCs: `GET /ec2automation/network/list`
- Get a Single VPC: `GET /ec2automation/network/{vpcId}`
- Create a VPC: `POST /ec2automation/network`
- Create a Subnet: `POST /ec2automation/network/{vpcId}/subnet`
- Get all Subnets for VPC: `GET /ec2automation/network/{vpcId}/subnet/list`
- Get a Subnet: `GET /ec2automation/network/{vpcId}/subnet/{subnetId}`
- Delete a Subnet: `DELETE /ec2automation/network/{vpcId}/subnet/{subnetId}`

### Environment Management
- Get Environment: `GET /ec2automation/environment`
- Create Environment: `POST /ec2automation/environment`

## Usage
- Base URL: `http://localhost:8080/api` (or the URL where your API is deployed)
- Authentication: AWS credentials or IAM roles (configure in `application.properties` inside the resources folder)

## Configuration
- `application.properties`: Configure AWS credentials and region

## Deployment
- SpringBoot: Deploy as a SpringBoot application
- Containerization: Deploy using containerization tools like Docker
- Cloud: Deploy on cloud platforms like AWS, Azure, or Google Cloud
