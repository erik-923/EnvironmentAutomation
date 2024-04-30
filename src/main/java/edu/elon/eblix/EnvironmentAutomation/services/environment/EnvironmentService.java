package edu.elon.eblix.EnvironmentAutomation.services.environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.elon.eblix.EnvironmentAutomation.models.environment.EnvironmentDTO;
import edu.elon.eblix.EnvironmentAutomation.models.instance.InstanceDTO;
import edu.elon.eblix.EnvironmentAutomation.models.network.VpcDTO;
import edu.elon.eblix.EnvironmentAutomation.services.instance.InstanceDTOService;
import edu.elon.eblix.EnvironmentAutomation.services.network.NetworkDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class EnvironmentService {
    @Autowired
    private NetworkDTOService networkService;
    @Autowired
    private InstanceDTOService instanceService;
    private S3Client s3Client;
    private String bucketName = "enivronment-automation-project-eblix";

    @Autowired
    public EnvironmentService(S3Client s3Client) {
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(this.bucketName)
                .build();
        this.s3Client = s3Client;
        this.s3Client.createBucket(createBucketRequest);
    }

    public EnvironmentDTO getEnvironment() {
        EnvironmentDTO environment = new EnvironmentDTO();

        environment.setNetworks(this.networkService.getVpcs());
        environment.setInstances(this.instanceService.getInstances());

        saveToBucket(environment);
        return environment;
    }

    public EnvironmentDTO createEnvironment(EnvironmentDTO environment) {
        for (VpcDTO vpc : environment.getNetworks()) {
            this.networkService.createVpc(vpc);
        }
        for (InstanceDTO instance : environment.getInstances()) {
            this.instanceService.createInstance(instance);
        }
        return getEnvironment();
    }

    private void saveToBucket(EnvironmentDTO environment) {
        try {
            String jsonString = convertEnvironmentToJson(environment);
            String key = getCurrentTimestampString() + " Environment";
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(request, RequestBody.fromString(jsonString));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentTimestampString() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return currentTime.format(formatter);
    }

    private String convertEnvironmentToJson(Object object) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        om.registerModule(new JavaTimeModule());
        return om.writeValueAsString(object);
    }
}
