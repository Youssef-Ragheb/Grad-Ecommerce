package com.grad.ecommerce_ai.mappers;

import com.grad.ecommerce_ai.dto.UserDTO;
import com.grad.ecommerce_ai.enitity.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    // Convert UserDTO to User
    public static User dtoToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();

        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setGender(userDTO.getGender());
        user.setDateCreated(userDTO.getDateCreated());
        user.setLastLoginDate(userDTO.getLastLoginDate());
        user.setEnabled(userDTO.isEnabled());
        user.setUserRoles(userDTO.getUserRoles());

        return user;
    }

    // Convert User to UserDTO
    public static UserDTO userToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setCity(user.getCity());
        userDTO.setGender(user.getGender());
        userDTO.setDateCreated(user.getDateCreated());
        userDTO.setLastLoginDate(user.getLastLoginDate());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setUserRoles(user.getUserRoles());

        return userDTO;
    }

    // Convert a list of UserDTO to a list of User
    public static List<User> dtoListToUserList(List<UserDTO> userDTOList) {
        return userDTOList.stream().map(UserMapper::dtoToUser).collect(Collectors.toList());
    }

    // Convert a list of User to a list of UserDTO
    public static List<UserDTO> userListToDtoList(List<User> userList) {
        return userList.stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }
}
