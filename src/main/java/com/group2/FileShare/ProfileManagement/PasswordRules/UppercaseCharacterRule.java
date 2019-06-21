package com.group2.FileShare.ProfileManagement.PasswordRules;

/**
 * Reference: https://stackoverflow.com/questions/16127923/checking-letter-case-upper-lower-within-a-string-in-java
 */

public class UppercaseCharacterRule implements IPasswordRule {

    public UppercaseCharacterRule(){

    }

    @Override
    public boolean isValid(String password) {

        if(password.equals(password.toLowerCase()) == false){
            return true;
        }else{
            return false;
        }
    }
}