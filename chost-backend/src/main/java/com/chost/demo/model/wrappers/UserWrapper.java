package com.chost.demo.model.wrappers;

import com.chost.demo.model.entity.Role;
import com.chost.demo.model.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class UserWrapper implements UserDetails, OAuth2User {

    private int id;
    private String login;
    private String password;
    private String email;
    private String name;
    private Map<String,Object> attrs;
    private String imageUrl;
    private String provideId;
    public UserWrapper(int id,String name, String login, String email, String password,String provideId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.provideId = provideId;
        this.roles = authorities;
    }
    public static UserWrapper create(User user){
        List<GrantedAuthority> authorities = Collections.
                singletonList(Role.ROLE_USER);

        return new UserWrapper(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getProviderId(),
                authorities
        );
    }
    public static UserWrapper create(User user, Map<String, Object> attributes) {
        UserWrapper userPrincipal = UserWrapper.create(user);
        userPrincipal.setAttrs(attributes);
        return userPrincipal;
    }


    private Collection<? extends GrantedAuthority> roles;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attrs;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
