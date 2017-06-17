package com.lsjr.zizisteward.bean;

import com.lsjr.zizisteward.R;

/**
 * Created by Administrator on 2017/6/5.
 */

public class NoteLoginBean {

    private String msg;
    private String error;
    private RepMap repMap;

    public RepMap getRepMap() {
        return repMap;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setRepMap(RepMap repMap) {
        this.repMap = repMap;
    }

    public String getError() {
        return error;

    }

    public String getMsg() {
        return msg;
    }

    public static class RepMap {
        /*生日*/
        private String birthday;
        /*性别*/
        private String sex;

        private String certificate_numer;

        private String certificate_photo;
        /**
         * 等级
         */
        private String credit;
        /**
         * 信用额度
         */
        private String credit_line;
        private String gmid;
        /**
         * 管家帐号
         */
        private String gname;
        /**
         * 管家头像
         */
        private String gphoto;
        /**
         * 管家昵称
         */
        private String guser_name;
        /**
         * 用户头像
         */
        private String headImg;
        /**
         * 用户ID
         */
        private String id;
        /**
         * 身份
         */
        private String identity_type;
        /**
         * 身份证
         */
        private String idno;
        /**
         * 是否实名认证
         */
        private String isIdNumberVerified;
        /**
         * 是否身份认证
         */
        private String isPassCertificate;
        /**
         * 用户登录帐号
         */
        private String name;
        /**
         * 实名
         */
        private String reality_name;
        /**
         * 分数
         */
        private String score;
        /**
         * 昵称
         */
        private String username;
        /**
         * 密码
         */
        private String defaultPwd;
        /**
         * 邮箱
         */
        private String email;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDefaultPwd() {
            return defaultPwd;
        }

        public void setDefaultPwd(String defaultPwd) {
            this.defaultPwd = defaultPwd;
        }

        public String getCertificate_numer() {
            return certificate_numer;
        }

        public void setCertificate_numer(String certificate_numer) {
            this.certificate_numer = certificate_numer;
        }

        public String getCertificate_photo() {
            return certificate_photo;
        }

        public void setCertificate_photo(String certificate_photo) {
            this.certificate_photo = certificate_photo;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getCredit_line() {
            return credit_line;
        }

        public void setCredit_line(String credit_line) {
            this.credit_line = credit_line;
        }

        public String getGmid() {
            return gmid;
        }

        public void setGmid(String gmid) {
            this.gmid = gmid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGphoto() {
            return gphoto;
        }

        public void setGphoto(String gphoto) {
            this.gphoto = gphoto;
        }

        public String getGuser_name() {
            return guser_name;
        }

        public void setGuser_name(String guser_name) {
            this.guser_name = guser_name;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdentity_type() {
            return identity_type;
        }

        public void setIdentity_type(String identity_type) {
            this.identity_type = identity_type;
        }

        public String getIdno() {
            return idno;
        }

        public void setIdno(String idno) {
            this.idno = idno;
        }

        public String getIsIdNumberVerified() {
            return isIdNumberVerified;
        }

        public void setIsIdNumberVerified(String isIdNumberVerified) {
            this.isIdNumberVerified = isIdNumberVerified;
        }

        public String getIsPassCertificate() {
            return isPassCertificate;
        }

        public void setIsPassCertificate(String isPassCertificate) {
            this.isPassCertificate = isPassCertificate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReality_name() {
            return reality_name;
        }

        public void setReality_name(String reality_name) {
            this.reality_name = reality_name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
