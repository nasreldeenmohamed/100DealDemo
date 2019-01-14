package app.deal.com.dealdemo.model.remote.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Misteka on 11/30/2018.
 */

public class RegisterResponse {
    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("pass")
        @Expose
        private String pass;
        @SerializedName("mail")
        @Expose
        private String mail;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("init")
        @Expose
        private String init;
        @SerializedName("roles")
        @Expose
        private Roles roles;
        @SerializedName("data")
        @Expose
        private Data data;
        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("created")
        @Expose
        private Integer created;
        @SerializedName("theme")
        @Expose
        private String theme;
        @SerializedName("signature")
        @Expose
        private String signature;
        @SerializedName("access")
        @Expose
        private Integer access;
        @SerializedName("login")
        @Expose
        private Integer login;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("picture")
        @Expose
        private Integer picture;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getInit() {
            return init;
        }

        public void setInit(String init) {
            this.init = init;
        }

        public Roles getRoles() {
            return roles;
        }

        public void setRoles(Roles roles) {
            this.roles = roles;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Integer getCreated() {
            return created;
        }

        public void setCreated(Integer created) {
            this.created = created;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public Integer getAccess() {
            return access;
        }

        public void setAccess(Integer access) {
            this.access = access;
        }

        public Integer getLogin() {
            return login;
        }

        public void setLogin(Integer login) {
            this.login = login;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Integer getPicture() {
            return picture;
        }

        public void setPicture(Integer picture) {
            this.picture = picture;
        }

    }


    public class Data {

        @SerializedName("contact")
        @Expose
        private Integer contact;

        public Integer getContact() {
            return contact;
        }

        public void setContact(Integer contact) {
            this.contact = contact;
        }

    }


    public class Roles {

        @SerializedName("2")
        @Expose
        private String _2;
        @SerializedName("7")
        @Expose
        private String _7;

        public String get2() {
            return _2;
        }

        public void set2(String _2) {
            this._2 = _2;
        }

        public String get7() {
            return _7;
        }

        public void set7(String _7) {
            this._7 = _7;
        }

    }


}
