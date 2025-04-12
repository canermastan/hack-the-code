package com.kodla.coz.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "languages")
public class Language {
    public Language(){

    }
    public Language(int id, String name, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }
    @Id
    private int id;

    @Column
    private String name;

    @OneToMany(mappedBy = "language")
    private Set<Task> tasks = new HashSet<Task>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Task> getContents() {
        return tasks;
    }

    public void setContents(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
