package dev.jamilxt.dailyconnect.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // To associate with a user
    private String fullName;
    private String email;
    private String phone;
    private String intro; // New field for Intro section
    private String skills; // Programming Languages, Databases, Cloud, Frameworks, etc.
    private String professionalExperience; // Work history
    private String projects; // Project details
    private String education; // Education details

    // Default constructor
    public Portfolio() {}

    // Parameterized constructor
    public Portfolio(String username, String fullName, String email, String phone, String intro, String skills,
                     String professionalExperience, String projects, String education) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.intro = intro;
        this.skills = skills;
        this.professionalExperience = professionalExperience;
        this.projects = projects;
        this.education = education;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getProfessionalExperience() {
        return professionalExperience;
    }

    public void setProfessionalExperience(String professionalExperience) {
        this.professionalExperience = professionalExperience;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}