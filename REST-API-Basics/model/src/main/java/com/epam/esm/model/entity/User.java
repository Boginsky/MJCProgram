package com.epam.esm.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Data
public class User extends ApplicationBaseEntity {

    private String firstName;
    private String lastName;

    @Builder
    public User(long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName)
                && lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = 31 * result + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }
}
