package weathersource.webxml.com.cn;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import cn.kli.utils.klilog;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class InternetAccess {
	
	private final static int MSG_INTERNET_RESPONSE = 1;
	
	private static Object mRequestLock = new Object();
	private static final String ENCODE="UTF-8";
	private static InternetAccess sInstance;
	private HttpClient mHttpClient;
	private boolean mRequesting = false;
	private String mRespose;
	
	private Context mContext;
	private Handler mHandler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_INTERNET_RESPONSE:
				klilog.i("msg get!!!");
				mRespose = (String)msg.obj;
				klilog.i("mRespose: " + mRespose);
				mRequesting = false;
				synchronized(mRequestLock){
					mRequestLock.notifyAll();
				}
				break;
			}
			
		}
		
	};
	
	private InternetAccess(Context context){
		mContext = context.getApplicationContext();
		mHttpClient = new DefaultHttpClient();
	}
	
	public static InternetAccess getInstance(Context context){
		if(sInstance == null){
			sInstance = new InternetAccess(context);
		}
		return sInstance;
	}
	
	synchronized public String request(final String url){
		final String result;
		
		mRequesting = true;
		
		//request
		requestByLocalNetwork(url);
		
		synchronized(mRequestLock){
			try {
				mRequestLock.wait(20*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		result = mRespose;
		return result;
	}
	
	private void requestByLocalNetwork(final String url) {
		new Thread(){

			@Override
			public void run() {
				super.run();
				String result = null;
				HttpUriRequest req = new HttpGet(url);
				klilog.i("Internet request: " + url);
				try {
					HttpResponse response = mHttpClient.execute(req);
					if (response.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(response.getEntity(), ENCODE);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message msg = mHandler.obtainMessage(MSG_INTERNET_RESPONSE);
				msg.obj = result;
				msg.sendToTarget();
			}
			
		}.start();

	}
	
}
