package weathersource.webxml.com.cn;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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

class InternetAccess {
	private static final boolean DEBUG = false;
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
			case 1:
				klilog.i("msg get!!!");
				Bundle bundle = msg.getData();
				mRespose = bundle.getString("res");
				klilog.i("mRespose: " + mRespose);
				mRequesting = false;
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
	
	public String request(final String url){
		final String result;
		
		MyReceiver receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("internet_response");
		mContext.registerReceiver(receiver, filter);

		mRequesting = true;
		
		Intent send = new Intent();
		send.setAction("internet_request");
		send.putExtra("url", url);
		mContext.sendBroadcast(send);
		
		int retryCount = 20;
		int i = 0;

		while(mRequesting && i < 20){
			klilog.i("waiting..");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
		
		result = receiver.res;
		klilog.i("result = "+result);
		mContext.unregisterReceiver(receiver);
		return result;
	}
	
	class MyReceiver extends BroadcastReceiver{
		public String res = null;

		@Override
		public void onReceive(Context arg0, Intent intent) {
			klilog.i("response  received");
			res = intent.getStringExtra("response");
			mRequesting = false;
		}
		
	}
	/*
	synchronized public String request(final String url){
		String result = null;
		if(DEBUG){
			HttpUriRequest req = new HttpGet(url);
			klilog.i("Internet request: " +url);
			try {
				HttpResponse response = mHttpClient.execute(req);
				if(response.getStatusLine().getStatusCode() == 200){
					result = EntityUtils.toString(response.getEntity(), ENCODE);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			ComponentName cn = new ComponentName("cn.ingenic.indroidsync", 
					"cn.ingenic.indroidsync.devicemanager.DeviceLocalService");
			Intent intent = new Intent();
			intent.setComponent(cn);
			ServiceConnection sc = new ServiceConnection(){

				@Override
				public void onServiceConnected(ComponentName arg0, IBinder service) {
					Messenger msger = new Messenger(service);
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					msg.setData(bundle);
					msg.replyTo = new Messenger(mHandler);
					try {
						msger.send(msg);
						klilog.i("msg send!!!");
					} catch (RemoteException e) {
						klilog.e("internet request send error");
						e.printStackTrace();
					}
				}

				@Override
				public void onServiceDisconnected(ComponentName arg0) {
					
				}
				
			};
			
			mRequesting = true;
			
			if(!mContext.bindService(intent, sc, Context.BIND_AUTO_CREATE)){
				klilog.e("bind device local service error!!!!");
				return null;
			}
			
			while(mRequesting){
			}
			
			result = mRespose;
			
		}
		return result;
	}*/
	
}
