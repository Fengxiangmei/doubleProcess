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

public class LocalService extends Service {
	private MyBinder binder;
	private Myconn conn;

	@Override
	public IBinder onBind(Intent intent) {
		
		return binder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		binder = new MyBinder();
		if (conn == null) {

			conn = new Myconn();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		this.bindService(new Intent(LocalService.this, RemoteSerice.class), conn, Context.BIND_IMPORTANT);
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
			Log.i("info", "链接远程服务成功");

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 远程服务被干掉
			Toast.makeText(LocalService.this, "远程服务被杀死", Toast.LENGTH_SHORT).show();
			// 启动本地进程
			LocalService.this.startService(new Intent(LocalService.this, RemoteSerice.class));
			// 绑定远程服务
			LocalService.this.bindService(new Intent(LocalService.this, RemoteSerice.class), conn,
					Context.BIND_IMPORTANT);
		}

	}
}
