
package com.fapin.helper;

public class ClassDetailsList {
    private int classId;

    private String className;

    private String classTeacher;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public ClassDetailsList(int classId, String className, String classTeacher) {
        setClassId(classId);
        setClassName(className);
        setClassTeacher(classTeacher);
    }

    public String toString() {
        return getClassId() + " " + getClassName() + " " + getClassTeacher();
    }

}
