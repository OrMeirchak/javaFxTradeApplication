package user;

import exception.UserDidntExistException;
import exception.UserNameAlreadyExistException;
import generated.RseItem;
import stock.Stock;

import java.util.Collection;
import java.util.HashSet;

public class UsersList {

    private Collection<User> users;

    public UsersList() {
        this.users = new HashSet<User>();
    }

   public  User getUser(String name){
      for (User user:users){
          if(user.getName().equals(name)){
              return user;
          }
      }
      return null;
    }

    public Collection<Holding> getHoldingData(String name) throws UserDidntExistException {
       User user= getUser(name);
       if(user==null){
           throw new UserDidntExistException(name);
       }
       else{
           return user.getHoldingData();
       }
    }

    public HashSet<String> getUserHoldingsSymbols (String name) throws UserDidntExistException {
        User user= getUser(name);
        if(user==null){
            throw new UserDidntExistException(name);
        }
        else{
            return user.getHoldingsSymbols();
        }

    }

    public User addUser(String name) throws UserNameAlreadyExistException {
        if((getUser(name))!=null){
           throw new UserNameAlreadyExistException(name);
        }
        else{
            User newUser=new User(name);
            users.add(newUser);
            return newUser;
        }
    }

    public int getTotalValue(String name) throws UserDidntExistException {
        User user= getUser(name);
        if(user==null){
            throw new UserDidntExistException(name);
        }
        else{
            return user.getTotalValue();
        }
    }

    public Collection<String> getNames(){
        HashSet<String> res=new HashSet<String>();

        for (User user:users){
            res.add(user.getName());
        }
return res;
    }
}
