package com.lsjr.zizisteward.ymz.bean;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/16 10:01
 */

public class DescriptionBean {

    /**
     * regulars : [{"credit_line":0,"description":"精致人生VIP1级会员","entityId":2,"id":2,"img":"/public/images/default/vip1.png","invitation_person_number":0,"is_show":0,"level":"V1","persistent":false,"points":0,"son_name_number":0,"steward_allocation":0,"upgrade_amount":2999,"upgrade_deposit":0},{"credit_line":0,"description":"贵不可言VIP2级会员","entityId":3,"id":3,"img":"/public/images/default/vip2.png","invitation_person_number":0,"is_show":0,"level":"V2","persistent":false,"points":0,"son_name_number":0,"steward_allocation":0,"upgrade_amount":29999,"upgrade_deposit":0},{"credit_line":0,"description":"臻享奢华VIP3级会员","entityId":4,"id":4,"img":"/public/images/default/vip3.png","invitation_person_number":0,"is_show":0,"level":"V3","persistent":false,"points":0,"son_name_number":0,"steward_allocation":0,"upgrade_amount":199999,"upgrade_deposit":0}]
     * error : 1
     * msg : 查询成功！
     */

    private String error;
    private String msg;
    private List<RegularsBean> regulars;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<RegularsBean> getRegulars() {
        return regulars;
    }

    public void setRegulars(List<RegularsBean> regulars) {
        this.regulars = regulars;
    }

    public static class RegularsBean {
        /**
         * credit_line : 0
         * description : 精致人生VIP1级会员
         * entityId : 2
         * id : 2
         * img : /public/images/default/vip1.png
         * invitation_person_number : 0
         * is_show : 0
         * level : V1
         * persistent : false
         * points : 0
         * son_name_number : 0
         * steward_allocation : 0
         * upgrade_amount : 2999
         * upgrade_deposit : 0
         */

        private int credit_line;
        private String description;
        private int entityId;
        private int id;
        private String img;
        private int invitation_person_number;
        private int is_show;
        private String level;
        private boolean persistent;
        private int points;
        private int son_name_number;
        private int steward_allocation;
        private int upgrade_amount;
        private int upgrade_deposit;

        public int getCredit_line() {
            return credit_line;
        }

        public void setCredit_line(int credit_line) {
            this.credit_line = credit_line;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getEntityId() {
            return entityId;
        }

        public void setEntityId(int entityId) {
            this.entityId = entityId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getInvitation_person_number() {
            return invitation_person_number;
        }

        public void setInvitation_person_number(int invitation_person_number) {
            this.invitation_person_number = invitation_person_number;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public boolean isPersistent() {
            return persistent;
        }

        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getSon_name_number() {
            return son_name_number;
        }

        public void setSon_name_number(int son_name_number) {
            this.son_name_number = son_name_number;
        }

        public int getSteward_allocation() {
            return steward_allocation;
        }

        public void setSteward_allocation(int steward_allocation) {
            this.steward_allocation = steward_allocation;
        }

        public int getUpgrade_amount() {
            return upgrade_amount;
        }

        public void setUpgrade_amount(int upgrade_amount) {
            this.upgrade_amount = upgrade_amount;
        }

        public int getUpgrade_deposit() {
            return upgrade_deposit;
        }

        public void setUpgrade_deposit(int upgrade_deposit) {
            this.upgrade_deposit = upgrade_deposit;
        }
    }
}
