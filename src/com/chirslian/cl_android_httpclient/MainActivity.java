package com.chirslian.cl_android_httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private Button button;
	private TextView textView;
	private Handler handler;
	static int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		count = 0;
		button = (Button)findViewById(R.id.button);
		textView = (TextView)findViewById(R.id.textView);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NetworkThread nt = new NetworkThread();
				nt.start();
			}
		});
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				String str = (String)msg.obj.toString();
				textView.setText(str + (++count));
				System.out.println("Message: "+str);
			}
		};
	}

	class NetworkThread extends Thread {
		@Override
		public void run() {
			// create HttpClient object
			HttpClient httpClient = new DefaultHttpClient();
			// create request object
			HttpGet httpGet = new HttpGet("http://www.marschen/data1.html");
			// excute request
			try {
				HttpResponse httpRsp = httpClient.execute(httpGet);
				int code = httpRsp.getStatusLine().getStatusCode();
				// check return status ,code == 200 is request OK
				if (code == 200) {
					// get return data
					HttpEntity entity = httpRsp.getEntity();
					InputStream in = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String str = reader.readLine();
					System.out.println("str:"+str);
					
					Message msg = handler.obtainMessage();
					msg.obj = str;
					handler.sendMessage(msg);
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
