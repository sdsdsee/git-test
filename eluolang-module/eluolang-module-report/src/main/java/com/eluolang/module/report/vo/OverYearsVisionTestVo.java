package com.eluolang.module.report.vo;

public class OverYearsVisionTestVo {
    /** 年份 */
    private Integer dateYear;
    private DataBean dataBean;

    public Integer getDateYear() {
        return dateYear;
    }

    public void setDateYear(Integer dateYear) {
        this.dateYear = dateYear;
    }

    public DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean{
        /** 近视 */
        private String myopia;
        /** 远视 */
        private String hyperopia;
        /** 正常 */
        private String normal;

        public String getMyopia() {
            return myopia;
        }

        public void setMyopia(String myopia) {
            this.myopia = myopia;
        }

        public String getHyperopia() {
            return hyperopia;
        }

        public void setHyperopia(String hyperopia) {
            this.hyperopia = hyperopia;
        }

        public String getNormal() {
            return normal;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }
    }
}
