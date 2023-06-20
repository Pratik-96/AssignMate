package com.example.assignmate;

public class file_model {
    String File_Name;
    String Description;
    String Url;
    public file_model(){}
    public file_model(String file_Name, String description, String url) {
        File_Name = file_Name;
        Description = description;
        Url = url;
    }

    public String getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(String file_Name) {
        File_Name = file_Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
