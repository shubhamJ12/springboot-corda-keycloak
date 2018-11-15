package com.example.api;

public class Employee {

    String empName;
    String id;

    public Employee(String empName, String id) {
        this.empName = empName;
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
