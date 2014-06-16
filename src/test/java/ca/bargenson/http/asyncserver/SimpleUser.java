package ca.bargenson.http.asyncserver;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 4:09 AM
 */
@XmlRootElement
public class SimpleUser {

    private String username;
    private Date dateOfBirth;

    public SimpleUser() {
    }

    public SimpleUser(String username, Date dateOfBirth) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "SimpleUser[" + username + "]";
    }

}
