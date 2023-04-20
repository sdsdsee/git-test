package com.eluolang.module.report.vo;

public class NearSevenDaysTestVo {
    /** 日期 */
    private String dateDay;
    private DataBean dataBean;

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
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
