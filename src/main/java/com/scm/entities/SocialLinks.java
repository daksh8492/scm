package com.scm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class SocialLinks {

@Id
public String id;
public String link;
public String title;

@ManyToOne
public Contact contact;

}
