package com.tanle.e_commerce.entities;


import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.CompositeKey.FollowerKey;
import com.tanle.e_commerce.entities.CompositeKey.UserRoleKey;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MyUser implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password")
    private String password;
    @Column(name = "sex")
    private boolean sex;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "avt_url")
    private String avtUrl;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "last_access")
    private LocalDateTime lastAccess;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "myUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserHasRole> roles;
    @OneToMany(mappedBy = "myUser")
    private List<Token> tokens;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Address> addresses;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follower> following;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follower> followers;

    public UserDTO convertDTO() {
        return UserDTO.builder()
                .userId(this.id)
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .avtUrl(this.avtUrl)
                .lastAccess(this.lastAccess)
                .isActive(this.isActive)
                .dateOfBirth(this.dateOfBirth)
                .createdAt(this.createdAt)
//                .following(this.following.stream()
//                        .map(Follower::converDTO)
//                        .collect(Collectors.toList()))
//                .follower(this.followers.stream()
//                        .map(Follower::converDTO)
//                        .collect(Collectors.toList()))
//                .addresses(this.addresses)
                .build();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", avtUrl='" + avtUrl + '\'' +
                ", isActive='" + isActive + '\'' +
                ", lastAccess=" + lastAccess +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = roles.stream()
                .map(UserHasRole::getRole)
                .map(Role::getRoleName)
                .map(r -> new SimpleGrantedAuthority(r))
                .collect(Collectors.toList());
        ;
        return simpleGrantedAuthorityList;
    }

    public boolean addUserRole(Role role) {
        if (roles == null) roles = new ArrayList<>();
        UserRoleKey key = new UserRoleKey(this.getId(), role.getId(), LocalDateTime.now());
        UserHasRole userHasRole = new UserHasRole();
        userHasRole.setRole(role);
        userHasRole.setMyUser(this);
        userHasRole.setId(key);

        return roles.add(userHasRole);
    }

    public boolean followUser(MyUser following) {
        if (this.following == null) this.following = new ArrayList<>();
        LocalDateTime followDate = LocalDateTime.now();
        FollowerKey followerKey = new FollowerKey(this.id, following.getId(), followDate);
        Follower follow = Follower.builder()
                .followerKey(followerKey)
                .following(following)
                .follower(this)
                .build();
        return this.following.add(follow);
    }

    public void unfollowUser(Follower follower) {
        LocalDateTime unfollowDate = LocalDateTime.now();
        follower.setUnfollowDate(unfollowDate);
    }

    public int countFollowing() {
        return (int) following.stream()
                .filter(f -> f.getUnfollowDate() == null)
                .count();
    }

    public int countFollower() {
        return (int) followers.stream()
                .filter(f -> f.getUnfollowDate() == null)
                .count();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean addAddress(Address address) {
        if (addresses == null) addresses = new ArrayList<>();
        return addresses.add(address);
    }

    public void updateLastAcess() {
        this.setLastAccess(LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
