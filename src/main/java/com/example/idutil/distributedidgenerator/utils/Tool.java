package com.example.idutil.distributedidgenerator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author : gaohui
 * @Date : 2019/2/18 14:07
 * @Description :
 */
public class Tool {

    private static final Logger log = LoggerFactory.getLogger(Tool.class);

    private Tool() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int getTextLength(String text) {
        try {
            return text != null ? text.getBytes("UTF-8").length : 0;
        } catch (UnsupportedEncodingException var2) {
            return text != null ? text.getBytes().length : 0;
        }
    }

    public static Map<String, Object> resultMap(String retStatus, String retMsg) {
        Map<String, Object> map = new HashMap();
        map.put("status", retStatus);
        if (retMsg != null) {
            map.put("message", retMsg);
        }

        return map;
    }

    public static String concat(String... params) {
//        Assertions.notNull("params", params);
        StringBuffer buff = new StringBuffer();
        String[] var2 = params;
        int var3 = params.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String str = var2[var4];
            if (!StringUtils.isEmpty(str)) {
                buff.append(str);
            }
        }

        return buff.toString();
    }

    public static String getSecureTel(String tel) {
        if (tel != null && tel.length() >= 7) {
            StringBuffer buff = new StringBuffer();
            buff.append(tel.substring(0, 3));
            buff.append("****");
            buff.append(tel.substring(7));
            return buff.toString();
        } else {
            return "";
        }
    }

    public static String encodeUiSn(String uiSn) {
        if (uiSn != null && uiSn.length() >= 18) {
            StringBuffer buff = new StringBuffer();
            buff.append(uiSn.substring(0, 2));
            buff.append("************");
            buff.append(uiSn.substring(14, 18));
            return buff.toString();
        } else {
            return "";
        }
    }

    public static String encodeBankNum(String bankCardNum) {
        if (bankCardNum != null && bankCardNum.length() > 4) {
            int length = bankCardNum.length();
            StringBuffer buff = new StringBuffer();
            buff.append(bankCardNum.substring(0, 4));
            if (length == 16) {
                buff.append("*******");
            } else {
                buff.append("**********");
            }

            buff.append(bankCardNum.substring(length - 4));
            return buff.toString();
        } else {
            return "";
        }
    }

   /* public static void outJSONObject(Object object, HttpServletResponse response) {
        Assertions.notNull("object", object);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            JSON json = null;
            if (object instanceof JSONObject) {
                json = (JSONObject) object;
            } else if (object instanceof Collection) {
                JSONArray.toJSON(object);
            } else {
                json = (JSON) JSONObject.toJSON(object);
            }

            out.print(json);
        } catch (IOException var7) {
            log.error("", var7);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }

        }

    }*/

    public static String dynamicMethodName(String field, String preStr) {
        char c = field.charAt(0);
        if (Character.isUpperCase(c)) {
            return concat(preStr, field);
        } else {
            String first = String.valueOf(Character.toUpperCase(c));
            return concat(preStr, first, field.substring(1));
        }
    }

    public static String concatPath(String path1, String path2) {
        if (path1==null||path1.trim().length()==0) {
            return path2;
        } else if (path2==null||path2.trim().length()==0) {
            return path1;
        } else {
            String path = null;
            if (!path1.endsWith("/") && !path2.startsWith("/")) {
                path = concat(path1, "/", path2);
            } else if ((!path1.endsWith("/") || path2.startsWith("/")) && (path1.endsWith("/") || !path2.startsWith("/"))) {
                path = concat(path1, path2.substring(1));
            } else {
                path = concat(path1, path2);
            }

            return path;
        }
    }

    public static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            Cookie[] var3 = cookies;
            int var4 = cookies.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Cookie cookie = var3[var5];
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }
}
