package com.etc.raw_materials_app.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class File {

    private int fileId;
    private LocalDateTime creationDate;
    private String filePath;
    private Integer testSituation;
    private Integer trialId;
    private Integer departmentId;
    private Integer userId;
    private String comment;
    private Integer fileTypeId;

    // Default constructor
    public File() {
    }

}