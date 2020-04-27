package in.notyouraveragedev.authenticator.domain;

import java.io.Serializable;

/**
 * POJO class for user data
 * <p>
 * Created by A Anand on 26-04-2020
 */
public class User implements Serializable {

    private String fullName;
    private String emailAddress;
    private String mobileNumber;
    private String profileUrl;

    public User(String fullName, String emailAddress, String mobileNumber, String profileUrl) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.profileUrl = profileUrl;
    }

    public User() {
        super();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }
}
