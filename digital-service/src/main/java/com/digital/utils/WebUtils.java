package com.digital.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  18:58
 */
public class WebUtils {
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
