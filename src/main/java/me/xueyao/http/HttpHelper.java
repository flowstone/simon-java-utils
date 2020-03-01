package me.xueyao.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * HTTP辅助类:
 * 
 * 获取HttpRequest的真实IP
 * 获取HttpResponse的Body
 *
 */
public class HttpHelper {

	private static final String X_FORWARDED__FOR = "x-forwarded-for";
	private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
	private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
	private static final String UNKNOWN = "unknown";
	
	/**
	 * 获取登录用户远程主机ip地址
	 * 
	 * @author   喻聪
	 * @date 2017-9-29   
	 */
	public static  String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader(X_FORWARDED__FOR);
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader(PROXY_CLIENT_IP);
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader(WL_PROXY_CLIENT_IP);
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static void setResponse(HttpServletResponse response, int value,
			Object object) {
		response.setStatus(value);
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			out.println(object);
		} catch (IOException e) {
			System.out.println("io输出异常:" +  e.getMessage());
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	/**
	 * 获取请求体中的字符串(POST) 
	 */
	public static String getBodyData(BufferedReader reader) {
		StringBuffer data = new StringBuffer();
		String line = null;
		try {
			while (null != (line = reader.readLine())) {
				data.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		
		}
		return data.toString();
	}
	
}
