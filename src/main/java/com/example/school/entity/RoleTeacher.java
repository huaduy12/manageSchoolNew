package com.example.school.entity;

public enum RoleTeacher {
    GiaoVien("Giáo viên"), TapVu("Tạp vụ"),
    PhoHieuTruong("Phó hiệu trưởng"), HieuTruong("Hiệu trưởng"),Admin("Quản trị viên");

    private final String displayName;

    RoleTeacher(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
