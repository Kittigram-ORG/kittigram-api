package org.ciscoadiz.user.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.ciscoadiz.user.dto.UserCreateRequest;
import org.ciscoadiz.user.dto.UserResponse;
import org.ciscoadiz.user.dto.UserUpdateRequest;
import org.ciscoadiz.user.entity.User;
import org.ciscoadiz.user.entity.UserStatus;

@ApplicationScoped
public class UserMapper {
    public User toEntity(UserCreateRequest request, String passwordHash) {
        User user = new User();
        user.email = request.email();
        user.passwordHash = passwordHash;
        user.name = request.name();
        user.surname = request.surname();
        user.birthdate = request.birthdate();
        user.status = UserStatus.Active;
        return user;
    }

    public void updateEntity(User user, UserUpdateRequest request) {
        user.name = request.name();
        user.surname = request.surname();
        user.birthdate = request.birthdate();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.id,
                user.email,
                user.name,
                user.surname,
                user.status,
                user.birthdate,
                user.createdAt,
                user.updatedAt
        );
    }
}
