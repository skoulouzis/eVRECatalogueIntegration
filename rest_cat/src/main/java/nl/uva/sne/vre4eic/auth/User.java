/*
 * Copyright 2017 S. Koulouzis, Wang Junchao, Huan Zhou, Yang Hu 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.uva.sne.vre4eic.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This class represents a user.
 *
 * @author S. Koulouzis
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

    private String id;
    private Collection<String> roles;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    /**
     * The unique id of this object. This value is auto-generated when the DAO
     * saves this object
     *
     * @return the id
     */
    @DocumentationExample("58e3946e0fb4f562d84ba1ad")
    public String getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> athorities = new HashSet<>();
        if (roles != null) {
            for (String role : roles) {
                String addedRole = role;
                if (!role.startsWith("ROLE_")) {
                    addedRole = "ROLE_" + role;
                }
                athorities.add(new SimpleGrantedAuthority(addedRole));
            }
        }
        return athorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * The username
     *
     * @return
     */
    @Override
    @DocumentationExample("user")
    public String getUsername() {
        return this.username;
    }

    /**
     * If the account is not expired
     *
     * @return
     */
    @Override
    @DocumentationExample("true")
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    /**
     * If the account is not locked
     *
     * @return
     */
    @Override
    @DocumentationExample("true")
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    /**
     * If the credentials are not expired
     *
     * @return
     */
    @Override
    @DocumentationExample("true")
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    /**
     * If the account is enabled
     *
     * @return
     */
    @Override
    @DocumentationExample("true")
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param accountNonExpired the accountNonExpired to set
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * @param accountNonLocked the accountNonLocked to set
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * @param credentialsNonExpired the credentialsNonExpired to set
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * The roles assigned to this user.
     *
     * @return the roles
     */
    @DocumentationExample("[\"ADMIN\",\"USER\"]")
    public Collection<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
