package space.swordfish.edge.service.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import space.swordfish.edge.service.service.AuthenticationService;

@Slf4j
@Api(tags = "Instances Query")
@RestController
public class InstanceQueryGatewayRestController {

    @LoadBalanced
    private final RestTemplate restTemplate;

    private final AuthenticationService authenticationService;

    private final static String SERVICE = "http://instance-service/instances";

    @Autowired
    public InstanceQueryGatewayRestController(RestTemplate restTemplate, AuthenticationService authenticationService) {
        this.restTemplate = restTemplate;
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "List all instances and their properties.")
    @GetMapping("/instances")
    public ResponseEntity<String> instances() {
        ParameterizedTypeReference<String> reference = new ParameterizedTypeReference<String>() {
        };

        return restTemplate.exchange(SERVICE, HttpMethod.GET, authenticationService.addAuthenticationHeader(), reference);
    }

    @ApiOperation(value = "List a single instance and it's properties by it's MongoDb ID")
    @GetMapping("/instances/{id}")
    public ResponseEntity<String> viewInstanceById(@PathVariable String id) {
        ParameterizedTypeReference<String> reference = new ParameterizedTypeReference<String>() {
        };

        return restTemplate.exchange(SERVICE + "/{id}", HttpMethod.GET, authenticationService.addAuthenticationHeader(), reference, id);
    }
}