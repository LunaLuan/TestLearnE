package learnenglishhou.bluebirdaward.com.learne.listclass;

/**
 * Created by asd on 01/08/2016.
 */

public class UserFacebook {

    private String userId;
    private String name;
    private String email;

    private int score;

    private boolean isCurrentUser;

    public UserFacebook() {
        isCurrentUser=false;
    }


    public void setScore(int score) {
        this.score=score;
    }

    public void setUserId(String userId) {
        this.userId= userId;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean isCurrentUser) {
        this.isCurrentUser= isCurrentUser;
    }

}
