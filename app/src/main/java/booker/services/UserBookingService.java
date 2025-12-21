package booker.services;

import booker.entities.User;
import booker.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService  {

    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper =new ObjectMapper();

    private final String USERS_PATH ="app/src/main/java/booker/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user=user1;
        File users=new File(USERS_PATH);
        userList= objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(){
        Optional<User> foundUser =userList.stream().filter(user1 ->
             user1.getName().equals(user.getName())&& UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword())).findFirst();
        return foundUser.isPresent();
        }


    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
            fetch(IOException ex){
                return Boolean.FALSE;
            }
        }
    }

    private saveUserListToFile() throws IOException {
        File usersFile =new File (USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

}
