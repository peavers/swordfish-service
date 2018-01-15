package space.swordfish.user.service.repositoriy;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import space.swordfish.user.service.domain.User;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, String> {

    User findById(String id);

}
