package com.example.config;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author juncheng
 */
public class HttpUtil {

	private static final int GET = 1;
	private static final int DELETE = 2;
	private static final int PUT = 3;
	private static final int POST = 4;
	private static Logger logger = Logger.getLogger(HttpUtil.class);

	private HttpUtil(){}
	
	public static String sendForm(String param, String url){
        String result = "";

        URL u = null;
        HttpURLConnection con = null;
        String params = "Data=" + param;
        System.out.println(url);
        System.out.println(params);

        //  发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;chartset=GBK");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "GBK");
            osw.write(params);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        StringBuffer buffer = new StringBuffer();
        try {
            //一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = buffer.toString();
        System.out.println("++++++++++"+ result);
        return result;
    }
	
	/**
	 * @param url
	 *            请求的 url
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头参数
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers) {
		return requestWithNoBody(HttpUtil.GET, url, params, headers);
	}

	/**
	 * @param url
	 *            请求的 url
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头参数
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	public static String delete(String url, Map<String, String> params, Map<String, String> headers) {
		return requestWithNoBody(HttpUtil.DELETE, url, params, headers);
	}

	/**
	 * @param url
	 *            请求的 url，若需要添加 url 参数，则直接拼接在 url 后面
	 * @param headers
	 *            请求头参数
	 * @param body
	 *            Body 参数，目前仅支持字符串类型
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	public static String put(String url, Map<String, String> headers, String body) {
		return requestWithBody(HttpUtil.PUT, url, headers, body);
	}

	/**
	 * @param url
	 *            请求的 url，若需要添加 url 参数，则直接拼接在 url 后面
	 * @param headers
	 *            请求头参数
	 * @param body
	 *            Body 参数，目前仅支持字符串类型
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	public static String post(String url, Map<String, String> headers, String body) {
		return requestWithBody(HttpUtil.POST, url, headers, body);
	}

	/**
	 * @param method
	 *            请求方法，目前支持 GET(1) 或 DELETE(2)
	 * @param url
	 *            请求的 url
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头参数
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	private static String requestWithNoBody(int method, String url, Map<String, String> params,
			Map<String, String> headers) {
		List<String> paramList = null;
		String temp = url;
		if (params != null) {
			if (url.contains("?")) {
				temp = temp + "&";
			} else {
				temp = temp + "?";
			}
			paramList = new ArrayList<>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramList.add(entry.getKey() + "=" + entry.getValue());
			}
		}
		if (paramList != null) {
			temp += String.join("&", paramList);
		}
		if (method == HttpUtil.GET) {
			return request(new HttpGet(), temp, headers, null);
		} else if (method == HttpUtil.DELETE) {
			return request(new HttpDelete(), temp, headers, null);
		} else {
			return null;
		}
	}

	/**
	 * @param method
	 *            请求方法，目前支持 PUT(3) 或 POST(4)
	 * @param url
	 *            请求的 url，若需要添加 url 参数，则直接拼接在 url 后面
	 * @param headers
	 *            请求头参数
	 * @param body
	 *            Body 参数，目前仅支持字符串类型
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	private static String requestWithBody(int method, String url, Map<String, String> headers, String body) {
		if (method == HttpUtil.PUT) {
			return request(new HttpPut(), url, headers, body);
		} else if (method == HttpUtil.POST) {
			return request(new HttpPost(), url, headers, body);
		} else {
			return null;
		}
	}

	/**
	 * @param method
	 *            请求的方法，应该为 HttpRequestBase 的子类，目前只能是
	 *            HttpGet,HttpPost,HttpPut,HttpDelete 中的一种
	 * @param url
	 *            请求的 url，如果为 GET 或者 DELETE 请求且有参数需要先拼接在 url 中
	 * @param body
	 *            PUT 或者 POST 请求的 Body 参数，目前仅支持字符串类型
	 * @param headers
	 *            请求头参数
	 * @return 请求返回的字符串，若请求异常则返回值为 null
	 */
	private static String request(HttpRequestBase method, String url, Map<String, String> headers, String body) {
		CloseableHttpClient httpClient = HttpClients.createMinimal();
		HttpRequestBase paramMehtod = method;
		try {
			paramMehtod.setURI(new URI(url));
		} catch (URISyntaxException e) {
			logger.error(e);
			return null;
		}
		// 设置连接超时时间和请求超时时间均为30秒，忽略 Cookies
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30 * 1000).setConnectTimeout(30 * 1000)
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		paramMehtod.setConfig(requestConfig);
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				paramMehtod.addHeader(entry.getKey(), entry.getValue());
			}
		} else {
			// 默认接收 UTF-8 编码的 JSON 数据
			paramMehtod.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
		}
		// 如果请求方式为 PUT 或者 POST，则添加 body
		if (paramMehtod instanceof HttpPut || paramMehtod instanceof HttpPost) {
			HttpEntityEnclosingRequestBase entityMethod = (HttpEntityEnclosingRequestBase) paramMehtod;
			entityMethod.setEntity(new StringEntity(body, "utf-8"));
			paramMehtod = entityMethod;
		}
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(paramMehtod);
			if (httpResponse != null) {
				HttpEntity entity = httpResponse.getEntity();
				//if (entity.getContentLength() > 0) {
					return EntityUtils.toString(entity, "UTF-8");
				//} else {
				//	return null;
				//}
			} else {
				return null;
			}
		} catch (IOException e) {
			logger.error(e);
			return null;
		} finally {
			try {
					httpClient.close();
				if (httpResponse != null) {
					httpResponse.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

}
