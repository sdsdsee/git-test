package com.eluolang.module.report.vo;

public class OverYearsTestScoreVo {
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
        /** 瘦 */
        private String thin;
        /** 正常 */
        private String normal;
        /** 偏胖 */
        private String overweight;
        /** 胖 */
        private String fat;

        public String getThin() {
            return thin;
        }

        public void setThin(String thin) {
            this.thin = thin;
        }

        public String getNormal() {
            return normal;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }

        public String getOverweight() {
            return overweight;
        }

        public void setOverweight(String overweight) {
            this.overweight = overweight;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }
    }
}
