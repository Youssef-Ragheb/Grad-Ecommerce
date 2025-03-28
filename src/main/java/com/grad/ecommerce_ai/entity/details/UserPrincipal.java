package com.grad.ecommerce_ai.entity.details;

import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.dto.enums.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


//        if (user.getUserRoles() != null) {
//            for (Role role : user.getUserRoles()) {
//                authorities.add(new SimpleGrantedAuthority( "ROLE_"+role.getName()));
//                System.out.println(authorities);
//            }
//        }
        return List.of(new SimpleGrantedAuthority(user.getUserRoles().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public Long getUserId() {
        return user.getId();
    }
    public UserRoles getUserRole(){
        return user.getUserRoles();
    }
    public boolean getCompanyRegistration(){
        return user.isCompanyRegistrationCompleted();
    }
}
