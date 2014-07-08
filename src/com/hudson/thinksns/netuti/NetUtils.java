package com.hudson.thinksns.netuti;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.hudson.thinksns.application.ThinkSNSApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 
 * @author 王代银
 * 
 */
public class NetUtils{
	private static NetUtils instance=null;
	public static NetUtils getInstance(){
		if(instance==null){
			instance=new NetUtils();
		}
		return instance;
	}
	public String sendGet(String url , String param)  
	{  
		String result = "";  
		BufferedReader in = null;  
		try
		{
			String urlName = url;
			if( param != null && param != "" && param.length() > 0 )
				urlName += "?" + param;  
			URL realUrl = new URL(urlName);  
			//打开和URL之间的连接  
			URLConnection conn = realUrl.openConnection();  

			//设置通用的请求属性  
			if(ThinkSNSApplication.strUserSession != null)
				conn.setRequestProperty("Cookie", ThinkSNSApplication.strUserSession);
			
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("accept", "*/*");  
			conn.setRequestProperty("connection", "Keep-Alive");  
			conn.setRequestProperty("user-agent",  
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
			//建立实际的连接  
			conn.connect(); 
			
			//定义BufferedReader输入流来读取URL的响应  
			in = new BufferedReader(  
			new InputStreamReader(conn.getInputStream()));  
			String line;  
			while ((line = in.readLine())!= null)  
			{  
				result += line;  
			}  
		}
		catch(Exception e)  
		{  
			System.out.println("发送GET请求出现异常！" + e);  
			e.printStackTrace();
		}  
		//使用finally块来关闭输入流  
		finally
		{
			try
			{
				if (in != null)  
				{
					in.close();  
				}
			}
			catch (IOException ex)  
			{
				ex.printStackTrace();  
			}
		}
		return result;  
	}
	
	public String sendHttpsGet(String url , String param)  
	{  
		String result = "";  
		BufferedReader in = null;  
		try
		{
			
			SSLContext sc = SSLContext.getInstance("TLS"); 
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
            
			String urlName = url + "?" + param;  
			URL realUrl = new URL(urlName);  
			//打开和URL之间的连接  
			URLConnection conn = realUrl.openConnection();
			
			//设置通用的请求属性  
			if(ThinkSNSApplication.strUserSession != null)
				conn.setRequestProperty("Cookie", ThinkSNSApplication.strUserSession);
			
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("accept", "*/*");  
			conn.setRequestProperty("connection", "Keep-Alive");  
			conn.setRequestProperty("user-agent",  
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
			//建立实际的连接  
			conn.connect(); 
			
			//定义BufferedReader输入流来读取URL的响应  
			in = new BufferedReader(  
			new InputStreamReader(conn.getInputStream()));  
			String line;  
			while ((line = in.readLine())!= null)  
			{  
				result += line;  
			}  
		}
		catch(Exception e)  
		{  
			System.out.println("发送GET请求出现异常！" + e);  
			e.printStackTrace();
		}  
		//使用finally块来关闭输入流  
		finally
		{
			try
			{
				if (in != null)  
				{
					in.close();  
				}
			}
			catch (IOException ex)  
			{
				ex.printStackTrace();  
			}
		}
		return result;  
	}
	
	private class MyHostnameVerifier implements HostnameVerifier{
        @Override
        public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
         }

	}
	
	private class MyTrustManager implements X509TrustManager{
	
		@Override
	    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub
	    }

		@Override
	    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	    }

		@Override
	    public X509Certificate[] getAcceptedIssuers() {
	    	return null;
	    }
	}  
	/**  
	* 向指定URL发送POST方法的请求  
	* @param url 发送请求的URL  
	* @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。  
	* @return URL所代表远程资源的响应  
	*/  
	public String sendPost(String url,String param)  
	{  
		PrintWriter out = null;  
		BufferedReader in = null;  
		String result = "";  
		try  
		{  
			URL realUrl = new URL(url);  
			//打开和URL之间的连接  
			URLConnection conn = realUrl.openConnection();  
			
			if(ThinkSNSApplication.strUserSession != null)
				conn.setRequestProperty("Cookie", ThinkSNSApplication.strUserSession);
			
			//设置通用的请求属性  
			conn.setRequestProperty("accept", "*/*");  
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
			
			//发送POST请求必须设置如下两行  
			conn.setDoOutput(true);  
			conn.setDoInput(true);  
			// 获取URLConnection对象对应的输出流  
			out = new PrintWriter(conn.getOutputStream());  
			//发送请求参数  
			out.print(param);
			//flush 输出流的缓冲  
			out.flush();  
			//定义BufferedReader输入流来读取URL的响应  
			in = new BufferedReader(  
			new InputStreamReader(conn.getInputStream()));  
			String line;  
			while ((line = in.readLine())!= null)  
			{  
				result += line;  
			}
		}
		catch(Exception e)  
		{  
			System.out.println("发送POST 请求出现异常！" + e);  
			e.printStackTrace();  
		}  
		//使用finally块来关闭输出流、输入流  
		finally  
		{  
			try  
			{  
				if (out != null)  
				{  
					out.close();  
				}  
				if (in != null)  
				{  
					in.close();  
				}  
			}  
			catch (IOException ex)  
			{  
				ex.printStackTrace();  
			}  
		}  
		return result;  
	}
	
	public String sendPost(String url,String param,int iLogin)  
	{  
		PrintWriter out = null;  
		BufferedReader in = null;  
		String result = "";  
		try  
		{  
			URL realUrl = new URL(url);  
			//打开和URL之间的连接  
			URLConnection conn = realUrl.openConnection();  
			//设置通用的请求属性  
			conn.setRequestProperty("accept", "*/*");  
			conn.setRequestProperty("connection", "Keep-Alive");  
			conn.setRequestProperty("user-agent",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
			//发送POST请求必须设置如下两行  
			conn.setDoOutput(true);  
			conn.setDoInput(true);  
			// 获取URLConnection对象对应的输出流  
			out = new PrintWriter(conn.getOutputStream());  
			//发送请求参数  
			out.print(param);  
			//flush 输出流的缓冲  
			out.flush();  
			//定义BufferedReader输入流来读取URL的响应  
			in = new BufferedReader(  
			new InputStreamReader(conn.getInputStream()));  
			String line;  
			while ((line = in.readLine())!= null)  
			{  
				result += line;  
			}
			
			if(iLogin == 1)		//登录
			{
				String strNewSession = conn.getHeaderField("Set-Cookie");
				String[] sessionId = strNewSession.split(";");
				ThinkSNSApplication.updateSession(sessionId[0]);
				//System.out.println("Session:"+sessionId[0]);
			}else if(iLogin == 0)		//注销
			{
				ThinkSNSApplication.updateSession(null);
			}
		}  
		catch(Exception e)  
		{  
			System.out.println("发送POST 请求出现异常！" + e);  
			e.printStackTrace();  
		}  
		//使用finally块来关闭输出流、输入流  
		finally  
		{  
			try  
			{  
				if (out != null)  
				{  
					out.close();  
				}  
				if (in != null)  
				{  
					in.close();  
				}  
			}  
			catch (IOException ex)  
			{  
				ex.printStackTrace();  
			}
		}  
		return result;  
	}
	
	public String sendGetBitmap(String url , String param)  
	{  
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory()+"/SmartCity","Tianyi");
        else
            cacheDir=ThinkSNSApplication.getInstance().getCacheDir();
		if(!cacheDir.exists())
		{
			cacheDir.mkdir();
		}
		
		String strFileName = String.valueOf((url).hashCode());
		File file = new File(cacheDir,strFileName);
		
		Bitmap mBitmap;
		BufferedReader in = null;  
		try
		{
			String urlName = url + "?" + param;  
			URL realUrl = new URL(urlName);  
			//打开和URL之间的连接  
			URLConnection conn = realUrl.openConnection();  
			//设置通用的请求属性  
			conn.setRequestProperty("accept", "*/*");  
			conn.setRequestProperty("connection", "Keep-Alive");  
			conn.setRequestProperty("user-agent",  
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
			//建立实际的连接  
			conn.connect();  

			//定义BufferedReader输入流来读取URL的响应  
			InputStream is=conn.getInputStream();
			mBitmap = BitmapFactory.decodeStream(is);
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			mBitmap.compress(Bitmap.CompressFormat.JPEG,80, bos);
			bos.flush();
			bos.close();
			
	        return file.getAbsolutePath();
		}  
		catch(Exception e)  
		{  
			System.out.println("发送GET请求出现异常！" + e);  
			e.printStackTrace();  
		}  
		//使用finally块来关闭输入流  
		finally
		{
			try
			{
				if (in != null)  
				{
					in.close();  
				}
			}
			catch (IOException ex)  
			{
				ex.printStackTrace();  
			}
		}
		return null;  
	}
}