package com.shopassist.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.shopassist.ShopAssist;
import com.shopassist.OnInfoNeedsUpdateListener;

public class AndroidLauncher extends AndroidApplication implements OnInfoNeedsUpdateListener {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		ShopAssist shopAssist = new ShopAssist();
		shopAssist.infoUpdater.onInfoNeedsUpdateEvent(this);
		initialize(shopAssist, config);
		
		// TODO(saurabh): This is just a proof of concept that we add layers
		// on top of the libgdx GL view. We need to define a separate view class that
		// should take care of this and handle user actions.
		ViewGroup libgdxView = (ViewGroup)super.graphics.getView().getRootView();
		LayoutInflater inflater = getLayoutInflater();
		inflater.inflate(R.layout.search_info_layer, libgdxView);
	}

	@Override
	public void onInfoNeedsUpdate(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView info = (TextView) findViewById(R.id.info);
				// TODO(srbs): This should display info about the touched object.
				info.setText(message);
				info.invalidate();
			}
		});
	}
}
