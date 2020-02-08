package com.chost.demo.model.entity;

import com.chost.demo.model.dto.jsonview.View;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class File{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({View.FULLINFORMATION.class})
    private int id;

    @Column
    @JsonView(View.FULLINFORMATION.class)
    private BigInteger price;
    @Column(nullable = false)
    @JsonView(View.FULLINFORMATION.class)
    private String name;
    @Column(length = 1000)
    @JsonView(View.FULLINFORMATION.class)
    private String description;
    @ElementCollection
    @JsonView(View.FULLINFORMATION.class)
    private List<String> filesNames = new ArrayList<>();
    @ManyToMany(mappedBy = "files")
    @JsonBackReference
    @JsonView({View.ME.class,View.FULLINFORMATION.class})
    private List<User> users = new ArrayList<>();
    @Column
    @JsonView(View.FULLINFORMATION.class)
    private String iconPath;
    @ManyToMany(mappedBy = "openToBuyFiles")
    @JsonManagedReference
    @JsonView({View.FULLINFORMATION.class})
    private List<User> whiteList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="owner_id")
    @JsonView(View.FULLINFORMATION.class)
    @JsonBackReference
    private User owner;
}
