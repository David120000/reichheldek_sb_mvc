package rd.reichheldek_sb_mvc.inmemory_repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import rd.reichheldek_sb_mvc.model.RegisteredUser;

@Service
public class UserRepository implements IUserRepository {

    private List<RegisteredUser> users;

    
    public UserRepository() {
        
        users = new ArrayList<>();
        this.initialData();
    }


    private void initialData() {

        RegisteredUser protectedUser = new RegisteredUser();
        protectedUser.setUsername("reichheld10");
        protectedUser.setPassword("csaladfakutato");
        protectedUser.setAuthority("USER");
        this.users.add(protectedUser);

        RegisteredUser adminUser = new RegisteredUser();
        adminUser.setUsername("reiccheldadmin572");
        adminUser.setPassword("Wsxcde531");
        adminUser.setAuthority("ADMIN");
        this.users.add(adminUser);
    }

    @Override
    public List<RegisteredUser> getRegisteredUsers() {
        return this.users;
    }

    @Override
    public void addUser(RegisteredUser user) {
        this.users.add(user);
    }

    @Override
    public void removeUser(RegisteredUser user) {

        for(int i = 0; i < users.size(); i++) {

            if(users.get(i).getUsername().equals(user.getUsername()) && users.get(i).getPassword().equals(user.getPassword())) {

                this.users.remove(i);
            }
        }
    }

    @Override
    public boolean authenticateUser(RegisteredUser user) {
        
        boolean authenticated = false;

        for(int i = 0; i < users.size(); i++) {

            if(users.get(i).getUsername().equals(user.getUsername()) && users.get(i).getPassword().equals(user.getPassword())) {
                authenticated = true;
                break;
            }
        }

        return authenticated;
    }
    
}
