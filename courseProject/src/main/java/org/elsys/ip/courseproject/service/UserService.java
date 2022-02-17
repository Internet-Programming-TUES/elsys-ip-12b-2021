package org.elsys.ip.courseproject.service;

import org.elsys.ip.courseproject.error.UserAlreadyExistException;
import org.elsys.ip.courseproject.model.User;
import org.elsys.ip.courseproject.model.UserRepository;
import org.elsys.ip.courseproject.web.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repository;

    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList("ROLE_USER"));

        return repository.save(user);
    }

    private boolean emailExist(String email) {
        return repository.findByEmail(email) != null;
    }
}