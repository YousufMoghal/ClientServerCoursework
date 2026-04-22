/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smartcampus.model;

import java.util.Map;

public class DiscoveryResponse {

    private String name;
    private String version;
    private String description;
    private String adminContact;
    private Map<String, String> resources;

    public DiscoveryResponse() {
    }

    public DiscoveryResponse(String name, String version, String description,
                             String adminContact, Map<String, String> resources) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.adminContact = adminContact;
        this.resources = resources;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(String adminContact) {
        this.adminContact = adminContact;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }
}