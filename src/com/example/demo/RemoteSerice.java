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
			Log.i("info", "���ؽ������ӳɹ�");

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// ���ط��񱻸ɵ�
			Toast.makeText(RemoteSerice.this, "���ط���ɱ��", Toast.LENGTH_SHORT).show();
			// ����Զ�̽���
			RemoteSerice.this.startService(new Intent(RemoteSerice.this, LocalService.class));
			// �󶨱��ط���
			RemoteSerice.this.bindService(new Intent(RemoteSerice.this, LocalService.class), conn,
					Context.BIND_IMPORTANT);
		}

	}
}
