package model;

import java.time.LocalDate;

public abstract class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private Role role;
}
