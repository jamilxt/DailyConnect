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

    private String username;
    private String fullName;
    private String title; // e.g., Sr. Software Engineer
    private String email;
    private String phone;
    private String address;
    private String githubLink;
    private String linkedinLink;
    private String photoUrl; // New field for profile photo
    private String intro;
    private String skills;
    private String professionalExperience; // JSON string: [{"company": "Brain Station 23 Ltd.", "role": "Sr. Software Engineer", "startDate": "Jul 2023", "endDate": "present"}]
    private String projects; // JSON string: [{"title": "Fleet Management System", "startDate": "10/2020", "endDate": "present", "description": "..."}]
    private String education;

    // Default constructor
    public Portfolio() {}

    // Parameterized constructor
    public Portfolio(String username, String fullName, String title, String email, String phone, String address,
                     String githubLink, String linkedinLink, String photoUrl, String intro, String skills,
                     String professionalExperience, String projects, String education) {
        this.username = username;
        this.fullName = fullName;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.githubLink = githubLink;
        this.linkedinLink = linkedinLink;
        this.photoUrl = photoUrl;
        this.intro = intro;
        this.skills = skills;
        this.professionalExperience = professionalExperience;
        this.projects = projects;
        this.education = education;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getGithubLink() { return githubLink; }
    public void setGithubLink(String githubLink) { this.githubLink = githubLink; }
    public String getLinkedinLink() { return linkedinLink; }
    public void setLinkedinLink(String linkedinLink) { this.linkedinLink = linkedinLink; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getProfessionalExperience() { return professionalExperience; }
    public void setProfessionalExperience(String professionalExperience) { this.professionalExperience = professionalExperience; }
    public String getProjects() { return projects; }
    public void setProjects(String projects) { this.projects = projects; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
}