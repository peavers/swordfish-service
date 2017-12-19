package space.swordfish.instance.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import space.swordfish.common.json.services.JsonTransformService;
import space.swordfish.instance.service.domain.Instance;
import space.swordfish.instance.service.repository.InstanceRepository;

@Slf4j
@RestController
@RequestMapping("/instances")
public class InstanceController {

	@Autowired
	private InstanceRepository instanceRepository;

	@Autowired
	private JsonTransformService jsonTransformService;

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Value("${queues.instanceEvents}")
	private String queue;

	@GetMapping()
	public String findAll() {
		Iterable<Instance> instances = instanceRepository.findAll();

		log.info("Returning instances {}", instances);

		return jsonTransformService.writeList(instances);
	}

	@GetMapping("{id}")
	public String findById(@PathVariable String id) {
		Instance instance = instanceRepository.findById(id);

		return jsonTransformService.write(instance);
	}

	@PostMapping()
	public String post(@RequestBody String payload) {
		this.queueMessagingTemplate.send(queue,
				MessageBuilder.withPayload(payload).build());
		Instance instance = jsonTransformService.read(Instance.class, payload);

		return jsonTransformService.write(instance);
	}

	@PatchMapping("{id}")
	@DeleteMapping("{id}")
	public String patch(@RequestBody String payload, @PathVariable("id") String id) {
		this.queueMessagingTemplate.send(queue,
				MessageBuilder.withPayload(payload).build());
		Instance instance = jsonTransformService.read(Instance.class, payload);

		return jsonTransformService.write(instance);
	}
}