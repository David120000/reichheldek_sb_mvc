package rd.reichheldek_sb_mvc.inmemory_repository;

import java.util.List;

import rd.reichheldek_sb_mvc.model.RegisteredUser;

public interface IUserRepository {
    
    List<RegisteredUser> getRegisteredUsers();
    void addUser(RegisteredUser user);
    void removeUser(RegisteredUser user);
    boolean authenticateUser(RegisteredUser user);
    
}
