package space.swordfish.instance.service.service;

import space.swordfish.instance.service.domain.Instance;

public interface EC2Reboot {

    void process(Instance instance);
}
