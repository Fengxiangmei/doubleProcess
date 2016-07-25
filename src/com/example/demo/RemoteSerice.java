package com.example.demo;

import com.example.demo.inter.ProcessService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class RemoteSerice extends Service {
	private MyBinder binder;
	private Myconn conn;

	@Override
	public IBinder onBind(Intent intent) {
		
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		binder = new MyBinder();
		if (conn == null) {

			conn = new Myconn();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		this.bindService(new Intent(RemoteSerice.this, LocalService.class), conn, Context.BIND_IMPORTANT);
	}

	class MyBinder extends ProcessService.Stub {

		@Override
		public String queryBook(int bookNo) throws RemoteException {
			return "LocalService";
		}

	}

	class Myconn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("info", "本地进程链接成功");

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 本地服务被干掉
			Toast.makeText(RemoteSerice.this, "本地服务被杀死", Toast.LENGTH_SHORT).show();
			// 启动远程进程
			RemoteSerice.this.startService(new Intent(RemoteSerice.this, LocalService.class));
			// 绑定本地服务
			RemoteSerice.this.bindService(new Intent(RemoteSerice.this, LocalService.class), conn,
					Context.BIND_IMPORTANT);
		}

	}
}
