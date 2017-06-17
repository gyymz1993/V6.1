package com.lsjr.zizisteward.ymz.bean;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/16 10:27
 */

public class BenefitsBean {

    /**
     * error : 1
     * benefits : [{"member":[{"content":"测试测试测试测试测试测试测试测试","description":"","entityId":3,"icon":"/images?uuid=0f0bd292-24c5-433c-8814-80a0f3afe731","id":3,"image":"/images?uuid=31e3f3ce-7634-48a3-a728-49bc4322828d,/images?uuid=27b2cfc5-cbae-41c6-8d74-2bbfdd604149","is_show":0,"level_id":4,"name":"不等位","persistent":false,"sort":0,"time":"","tname":"美食","tpath":0,"tpid":86,"type_icons":"/images?uuid=23dbebc9-c7d8-4082-b61e-9a3ae9d8c199"}],"name":"不等位","tpid":86,"type_icons":"/images?uuid=23dbebc9-c7d8-4082-b61e-9a3ae9d8c199"}]
     * msg : 查询成功！
     */

    private String error;
    private String msg;
    private List<FitsBean> benefits;

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

    public List<FitsBean> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<FitsBean> benefits) {
        this.benefits = benefits;
    }

    public static class FitsBean {
        /**
         * member : [{"content":"测试测试测试测试测试测试测试测试","description":"","entityId":3,"icon":"/images?uuid=0f0bd292-24c5-433c-8814-80a0f3afe731","id":3,"image":"/images?uuid=31e3f3ce-7634-48a3-a728-49bc4322828d,/images?uuid=27b2cfc5-cbae-41c6-8d74-2bbfdd604149","is_show":0,"level_id":4,"name":"不等位","persistent":false,"sort":0,"time":"","tname":"美食","tpath":0,"tpid":86,"type_icons":"/images?uuid=23dbebc9-c7d8-4082-b61e-9a3ae9d8c199"}]
         * name : 不等位
         * tpid : 86
         * type_icons : /images?uuid=23dbebc9-c7d8-4082-b61e-9a3ae9d8c199
         */

        private String name;
        private int tpid;
        private String type_icons;
        private List<MemberBean> member;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTpid() {
            return tpid;
        }

        public void setTpid(int tpid) {
            this.tpid = tpid;
        }

        public String getType_icons() {
            return type_icons;
        }

        public void setType_icons(String type_icons) {
            this.type_icons = type_icons;
        }

        public List<MemberBean> getMember() {
            return member;
        }

        public void setMember(List<MemberBean> member) {
            this.member = member;
        }

        public static class MemberBean {
            /**
             * content : 测试测试测试测试测试测试测试测试
             * description :
             * entityId : 3
             * icon : /images?uuid=0f0bd292-24c5-433c-8814-80a0f3afe731
             * id : 3
             * image : /images?uuid=31e3f3ce-7634-48a3-a728-49bc4322828d,/images?uuid=27b2cfc5-cbae-41c6-8d74-2bbfdd604149
             * is_show : 0
             * level_id : 4
             * name : 不等位
             * persistent : false
             * sort : 0
             * time :
             * tname : 美食
             * tpath : 0
             * tpid : 86
             * type_icons : /images?uuid=23dbebc9-c7d8-4082-b61e-9a3ae9d8c199
             */

            private String content;
            private String description;
            private int entityId;
            private String icon;
            private int id;
            private String image;
            private int is_show;
            private int level_id;
            private String name;
            private boolean persistent;
            private int sort;
            private String time;
            private String tname;
            private int tpath;
            private int tpid;
            private String type_icons;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getIs_show() {
                return is_show;
            }

            public void setIs_show(int is_show) {
                this.is_show = is_show;
            }

            public int getLevel_id() {
                return level_id;
            }

            public void setLevel_id(int level_id) {
                this.level_id = level_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isPersistent() {
                return persistent;
            }

            public void setPersistent(boolean persistent) {
                this.persistent = persistent;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTname() {
                return tname;
            }

            public void setTname(String tname) {
                this.tname = tname;
            }

            public int getTpath() {
                return tpath;
            }

            public void setTpath(int tpath) {
                this.tpath = tpath;
            }

            public int getTpid() {
                return tpid;
            }

            public void setTpid(int tpid) {
                this.tpid = tpid;
            }

            public String getType_icons() {
                return type_icons;
            }

            public void setType_icons(String type_icons) {
                this.type_icons = type_icons;
            }
        }
    }
}
