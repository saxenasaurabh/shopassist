package com.shopassist;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class InfoUpdater extends InfoUpdateListener
		implements InputProcessor {	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		updateInfo();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

class InfoUpdateListener implements Disposable {
	Array<OnInfoNeedsUpdateListener> listeners =
			new Array<OnInfoNeedsUpdateListener>();
	
	public void updateInfo() {
		for (OnInfoNeedsUpdateListener listener : listeners) {
			listener.onInfoNeedsUpdate();
		}
	}

	public void onInfoNeedsUpdateEvent(OnInfoNeedsUpdateListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void dispose() {
		listeners.clear();
	}
}