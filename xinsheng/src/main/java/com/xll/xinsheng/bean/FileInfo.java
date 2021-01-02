package com.xll.xinsheng.bean;

import androidx.databinding.BaseObservable;

import com.example.xinsheng.R;
import com.google.gson.annotations.SerializedName;
import com.xll.xinsheng.tools.MyApplication;

public class FileInfo extends BaseObservable {

    @SerializedName("file_path")
    private String filePath;

    @SerializedName("dept_no")
    private String deptNo;

    @SerializedName("up_date")
    private String upDate;

    @SerializedName("real_file_name")
    private String realFileName;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("file_name")
    private String fileName;

    @SerializedName("file_type")
    private String fileType;

    @SerializedName("up_user")
    private String upUser;

    @SerializedName("file_id")
    private String fileId;

    @SerializedName("file_desc")
    private String fileDesc;

    private String type;

    private boolean isLoading;

    public String getType() {
        String t = MyApplication.getMyApplication().getString(R.string.announcementManage);
        if (fileName != null && fileName.indexOf(".") + 1 > 0) {
            t = fileName.substring(fileName.indexOf(".") + 1);
        }
        return t.toUpperCase();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUpUser() {
        return upUser;
    }

    public void setUpUser(String upUser) {
        this.upUser = upUser;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }
}
