package es.kiwi.user.feign;

import es.kiwi.apis.user.IUserClient;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.user.repository.ApUserRepository;
import es.kiwi.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserClient implements IUserClient {

    @Autowired
    private ApUserRepository apUserRepository;

    @Override
    @GetMapping("/api/v1/user/{id}")
    public ApUser findUserById(@PathVariable("id") Integer id) {
        Optional<ApUser> apUserOpt = apUserRepository.findById(id);
        return apUserOpt.orElse(null);
    }
}
