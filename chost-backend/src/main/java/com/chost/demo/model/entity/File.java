package com.chost.demo.model.entity;

import com.chost.demo.model.dto.jsonview.View;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class File{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
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
    private List<User> users = new ArrayList<>();
    @Column
    @JsonView(View.FULLINFORMATION.class)
    private String iconPath;
}
