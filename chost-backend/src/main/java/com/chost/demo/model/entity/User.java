package com.chost.demo.model.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chost.demo.model.dto.jsonview.View;


@Entity
public class User {

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    @JsonView({View.ME.class,View.FULLINFORMATION.class})
    private String login;
    @Column
    private String password;
    @Column(unique = true)
    @JsonView({View.ME.class,View.FULLINFORMATION.class})
    private String email;

    @JsonManagedReference
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_files",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "file_id") }
    )
    @JsonView(View.FULLINFORMATION.class)
    private List<File> files = new ArrayList<>();

    @Column
    @JsonView({View.ME.class,View.FULLINFORMATION.class})
    private String name;
    @Column
    @JsonView({View.ME.class,View.FULLINFORMATION.class})
    private String imageUrl;
    @Column
    private String providerId;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @JsonView({View.ME.class,View.FULLINFORMATION.class})
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Collection<Role> roles;

    public int getId() {
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


    public String getPassword() {
        return password;
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
