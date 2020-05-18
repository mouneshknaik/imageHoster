package tmpapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmpapp.model.User;
import tmpapp.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User login(User user){
        User existingUser=userRepository.checkUser(user.getUsername(),user.getPassword());
        System.out.println(existingUser);
        System.out.println("existingUser");
        if (existingUser != null) {
            return existingUser;
        } else {
            return null;
        }
    }
    public void registerUser(User newUser) {
        userRepository.registerUser(newUser);
    }
}
