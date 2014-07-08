package com.hudson.thinksns.netuti;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ResponseCache;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
/**
 * 
 * @author ������
 * 
 */
public class MHttpClient {
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient mHttpClient;
	private static final String TAG = "CustomerHttpClient";

	private MHttpClient() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == mHttpClient) {
			// UA:Mozilla/5.0 (Linux; U;Android 2.3.5;zh-cn;P331Build/GRJ22)
			// AppleWebKit/533.1 (KHTML,like Gecko) Version/4.0 Mobile
			// Safari/533.1
			// UA:Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83)
			// AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile
			// Safari/533.1
			HttpParams params = new BasicHttpParams();
			// ����һЩ��������
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/533.1 (KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// ��ʱ����
			/* �����ӳ���ȡ���ӵĳ�ʱʱ�� */
			ConnManagerParams.setTimeout(params, 10000);
			/* ���ӳ�ʱ */
			HttpConnectionParams.setConnectionTimeout(params, 2000);
			/* ����ʱ */
			HttpConnectionParams.setSoTimeout(params, 4000);

			// �������ǵ�HttpClient֧��HTTP��HTTPS����ģʽ
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// ʹ���̰߳�ȫ�����ӹ���������HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			mHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return mHttpClient;
	}

	public static String post(String url, NameValuePair... params)
			 {
		HttpResponse response = null;
		try {
			// �������
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // �������
			for (NameValuePair p : params) {
				formparams.add(p);
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
					CHARSET);
			// ����POST����
			HttpPost request = new HttpPost(url);
			request.setEntity(entity);
			// ��������
			HttpClient client = getHttpClient();
			 response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return "��ȡ����ʧ��";
				//throw new RuntimeException("����ʧ��");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET);
		}
		 catch (UnsupportedEncodingException e) {
		 Log.w(TAG, e.getMessage());
		 return "��ȡ����ʧ��";
		// return null;
		 } catch (ClientProtocolException e) {
		 Log.w(TAG, e.getMessage());
		 return "��ȡ����ʧ��";
		// return null;
		 } catch (IOException e) {
			 return "��ȡ����ʧ��";
		 //return null;
		// throw new RuntimeException("����ʧ��", e);
		
		 }
//		 finally {
//			 if (response != null) {
//                 try {
//					response.getEntity().getContent().close();
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//             }
//
//		}

	}

	public static String get(String url, NameValuePair... params)
			 {
		HttpResponse response = null;
		try {
			// �������
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // �������
			for (NameValuePair p : params) {
				formparams.add(p);
			}
			String param = URLEncodedUtils.format(formparams, CHARSET);
			Log.e("url", url + "?" + param);
			// ����GET����
			HttpGet request;
			if (params.length == 0) {
				request = new HttpGet(url);
			} else {
				request = new HttpGet(url + "?" + param);
			}
			Log.e("requset",request.toString());
			// request.setEntity(entity);
			// ��������
			HttpClient client = getHttpClient();
			 response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return "��ȡ����ʧ��";
				//throw new RuntimeException("����ʧ��");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET);
		} 	
		 catch (UnsupportedEncodingException e) {
		 Log.w(TAG, e.getMessage());
		 return "��ȡ����ʧ��";
		 } catch (ClientProtocolException e) {
		 Log.w(TAG, e.getMessage());
		 return "��ȡ����ʧ��";
		 } catch (IOException e) {
			 Log.e(TAG, e.getMessage());
			 return "��ȡ����ʧ��";
		// throw new RuntimeException("����ʧ��", e);
		 } 
//		finally {
//			 if (response != null) {
//                 try {
//					response.getEntity().getContent().close();
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//             }
//
//		}

	

	}

}
