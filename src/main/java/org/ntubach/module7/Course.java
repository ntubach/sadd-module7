package org.ntubach.module7;

// Model for Course object
public class Course {
    private Integer courseNumber;
    private String courseTitle;

    Course(Integer courseNumber, String courseTitle) {
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
        System.out.println("Call Course constructor with args");
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
