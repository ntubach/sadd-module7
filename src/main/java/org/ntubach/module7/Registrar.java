package org.ntubach.module7;

import java.util.List;

// Model for Registrar object
public class Registrar {
    final Integer maxStudents = 15;
    private Integer courseNumber;
    private List<Integer> studentIdList;

    Registrar(Integer courseNumber, List<Integer> studentIdList) {
        this.studentIdList = studentIdList;
        this.courseNumber = courseNumber;
        System.out.println("Call Registrar constructor with args");
    }

    public List<Integer> getStudentIdList() {
        return studentIdList;
    }

    public Boolean setStudentIdList(List<Integer> studentIdList) {
        // Limit setting of student list to first 15 elements
        this.studentIdList = studentIdList.subList(0, maxStudents);
        // Let the caller know that we clipped the set if it happened (False if clipped)
        return studentIdList.size() <= 15;
    }

    public Boolean removeStudentId(Integer studentId) {
        return this.studentIdList.removeIf(e -> e.equals(studentId));
    }

    public Boolean addStudentId(Integer studentId) {
        // Check if size limit is exceeded, return false if so
        if (this.studentIdList.size() > 15)
            return false;

        this.studentIdList.add(studentId);
        return true;
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }
}
