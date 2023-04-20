package com.eluolang.physical.util;

import io.swagger.models.auth.In;

public class BmiTestResultUtil {
    public static String BmiTestResult(Integer sex, String grade, float num){
        String result = null;
        if (grade==null){
            if (num <= 18.5) {
                result = "瘦";
            } else if (num >= 18.5 && num <= 23.9) {
                result = "正常";
            } else if (num >= 24.0 && num <= 32.0) {
                result = "偏胖";
            } else if (num > 32.0) {
                result = "胖";
            }
            return result;
        }
        if (grade.equals("一年级") && sex == 1) {
            if (num <= 13.4) {
                result = "瘦";
            } else if (num >= 13.5 && num <= 18.1) {
                result = "正常";
            } else if (num >= 18.2 && num <= 20.3) {
                result = "偏胖";
            } else if (num >= 20.4) {
                result = "胖";
            }
        }else if (grade.equals("一年级") && sex == 2) {
            if (num <= 13.2) {
                result = "瘦";
            } else if (num >= 13.3 && num <= 17.3) {
                result = "正常";
            } else if (num >= 17.4 && num <= 19.2) {
                result = "偏胖";
            } else if (num >= 19.3) {
                result = "胖";
            }
        }else if (grade.equals("二年级") && sex == 1) {
            if (num <= 13.6) {
                result = "瘦";
            } else if (num >= 13.7 && num <= 18.4) {
                result = "正常";
            } else if (num >= 18.5 && num <= 20.4) {
                result = "偏胖";
            } else if (num >= 20.5) {
                result = "胖";
            }
        }else if (grade.equals("二年级") && sex == 2) {
            if (num <= 13.4) {
                result = "瘦";
            } else if (num >= 13.5 && num <= 17.8) {
                result = "正常";
            } else if (num >= 17.9 && num <= 20.2) {
                result = "偏胖";
            } else if (num >= 20.3) {
                result = "胖";
            }
        }else if (grade.equals("三年级") && sex == 1) {
            if (num <= 13.8) {
                result = "瘦";
            } else if (num >= 13.9 && num <= 19.4) {
                result = "正常";
            } else if (num >= 19.5 && num <= 22.1) {
                result = "偏胖";
            } else if (num >= 22.2) {
                result = "胖";
            }
        }else if (grade.equals("三年级") && sex == 2) {
            if (num <= 13.5) {
                result = "瘦";
            } else if (num >= 13.6 && num <= 18.6) {
                result = "正常";
            } else if (num >= 18.7 && num <= 21.1) {
                result = "偏胖";
            } else if (num >= 21.2) {
                result = "胖";
            }
        }else if (grade.equals("四年级") && sex == 1) {
            if (num <= 14.1) {
                result = "瘦";
            } else if (num >= 14.2 && num <= 20.1) {
                result = "正常";
            } else if (num >= 20.2 && num <= 22.6) {
                result = "偏胖";
            } else if (num >= 22.7) {
                result = "胖";
            }
        }else if (grade.equals("四年级") && sex == 2) {
            if (num <= 13.6) {
                result = "瘦";
            } else if (num >= 13.7 && num <= 19.4) {
                result = "正常";
            } else if (num >= 19.5 && num <= 22.0) {
                result = "偏胖";
            } else if (num >= 22.1) {
                result = "胖";
            }
        }else if (grade.equals("五年级") && sex == 1) {
            if (num <= 14.3) {
                result = "瘦";
            } else if (num >= 14.4 && num <= 21.4) {
                result = "正常";
            } else if (num >= 21.5 && num <= 24.1) {
                result = "偏胖";
            } else if (num >= 24.2) {
                result = "胖";
            }
        }else if (grade.equals("五年级") && sex == 2) {
            if (num <= 13.7) {
                result = "瘦";
            } else if (num >= 13.8 && num <= 20.5) {
                result = "正常";
            } else if (num >= 20.6 && num <= 22.9) {
                result = "偏胖";
            } else if (num >= 23.0) {
                result = "胖";
            }
        }else if (grade.equals("六年级") && sex == 1) {
            if (num <= 14.6) {
                result = "瘦";
            } else if (num >= 14.7 && num <= 21.8) {
                result = "正常";
            } else if (num >= 21.9 && num <= 24.5) {
                result = "偏胖";
            } else if (num >= 24.6) {
                result = "胖";
            }
        }else if (grade.equals("六年级") && sex == 2) {
            if (num <= 14.1) {
                result = "瘦";
            } else if (num >= 14.2 && num <= 20.8) {
                result = "正常";
            } else if (num >= 20.9 && num <= 23.6) {
                result = "偏胖";
            } else if (num >= 23.7) {
                result = "胖";
            }
        }else if (grade.equals("初一") && sex == 1) {
            if (num <= 15.4) {
                result = "瘦";
            } else if (num >= 15.5 && num <= 22.1) {
                result = "正常";
            } else if (num >= 22.2 && num <= 24.9) {
                result = "偏胖";
            } else if (num >= 25.0) {
                result = "胖";
            }
        }else if (grade.equals("初一") && sex == 2) {
            if (num <= 14.7) {
                result = "瘦";
            } else if (num >= 14.8 && num <= 21.7) {
                result = "正常";
            } else if (num >= 21.8 && num <= 24.4) {
                result = "偏胖";
            } else if (num >= 24.5) {
                result = "胖";
            }
        }else if (grade.equals("初二") && sex == 1) {
            if (num <= 15.6) {
                result = "瘦";
            } else if (num >= 15.7 && num <= 22.5) {
                result = "正常";
            } else if (num >= 22.6 && num <= 25.2) {
                result = "偏胖";
            } else if (num >= 25.3) {
                result = "胖";
            }
        }else if (grade.equals("初二") && sex == 2) {
            if (num <= 15.2) {
                result = "瘦";
            } else if (num >= 15.3 && num <= 22.2) {
                result = "正常";
            } else if (num >= 22.3 && num <= 24.8) {
                result = "偏胖";
            } else if (num >= 24.9) {
                result = "胖";
            }
        }else if (grade.equals("初三") && sex == 1) {
            if (num <= 15.7) {
                result = "瘦";
            } else if (num >= 15.8 && num <= 22.8) {
                result = "正常";
            } else if (num >= 22.9 && num <= 26.0) {
                result = "偏胖";
            } else if (num >= 26.1) {
                result = "胖";
            }
        }else if (grade.equals("初三") && sex == 2) {
            if (num <=15.9) {
                result = "瘦";
            } else if (num >= 16.0 && num <= 22.6) {
                result = "正常";
            } else if (num >= 22.7 && num <= 25.1) {
                result = "偏胖";
            } else if (num >= 25.2) {
                result = "胖";
            }
        }else if (grade.equals("高一") && sex == 1) {
            if (num <= 16.4) {
                result = "瘦";
            } else if (num >= 16.5 && num <= 23.2) {
                result = "正常";
            } else if (num >= 23.3 && num <= 26.3) {
                result = "偏胖";
            } else if (num >= 26.4) {
                result = "胖";
            }
        }else if (grade.equals("高一") && sex == 2) {
            if (num <= 16.4) {
                result = "瘦";
            } else if (num >= 16.5 && num <= 22.7) {
                result = "正常";
            } else if (num >= 22.8 && num <= 25.2) {
                result = "偏胖";
            } else if (num >= 25.3) {
                result = "胖";
            }
        }else if (grade.equals("高二") && sex == 1) {
            if (num <= 16.7) {
                result = "瘦";
            } else if (num >= 16.8 && num <= 23.7) {
                result = "正常";
            } else if (num >= 23.8 && num <= 26.5) {
                result = "偏胖";
            } else if (num >= 26.6) {
                result = "胖";
            }
        }else if (grade.equals("高二") && sex == 2) {
            if (num <= 16.8) {
                result = "瘦";
            } else if (num >= 16.9 && num <= 23.2) {
                result = "正常";
            } else if (num >= 23.3 && num <= 25.4) {
                result = "偏胖";
            } else if (num >= 25.5) {
                result = "胖";
            }
        }else if (grade.equals("高三") && sex == 1) {
            if (num <= 17.2) {
                result = "瘦";
            } else if (num >= 17.3 && num <= 23.8) {
                result = "正常";
            } else if (num >= 23.9 && num <= 27.3) {
                result = "偏胖";
            } else if (num >= 27.4) {
                result = "胖";
            }
        }else if (grade.equals("高三") && sex == 2) {
            if (num <= 17.0) {
                result = "瘦";
            } else if (num >= 17.1 && num <= 23.3) {
                result = "正常";
            } else if (num >= 23.4 && num <= 25.7) {
                result = "偏胖";
            } else if (num >= 25.8) {
                result = "胖";
            }
        }else if (grade.equals("大一") || grade.equals("大二") || grade.equals("大三") || grade.equals("大四")) {
            if (sex == 1){
                if (num < 17.8) {
                    result = "瘦";
                } else if (num >= 17.9 && num <= 23.9) {
                    result = "正常";
                } else if (num >= 24.0 && num <= 27.9) {
                    result = "偏胖";
                } else if (num >= 28.0) {
                    result = "胖";
                }
            }else {
                if (num <= 17.1) {
                    result = "瘦";
                } else if (num >= 17.2 && num <= 23.9) {
                    result = "正常";
                } else if (num >= 24.0 && num <= 27.9) {
                    result = "偏胖";
                } else if (num >= 28.0) {
                    result = "胖";
                }
            }
        }else {
            if (num <= 18.5) {
                result = "瘦";
            } else if (num >= 18.5 && num <= 23.9) {
                result = "正常";
            } else if (num >= 24.0 && num <= 32.0) {
                result = "偏胖";
            } else if (num > 32.0) {
                result = "胖";
            }
        }
        return result;
    }
}
