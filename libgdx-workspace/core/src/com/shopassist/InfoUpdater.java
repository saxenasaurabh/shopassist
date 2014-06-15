package com.shopassist;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * An [InputProcessor] that listens for touch events
 * and updated the info panel.
 * The intended use is to be able to view info related
 * to the object under focus when it is touched by the
 * user.
 * @author srbs
 *
 */
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
		// TODO(srbs): Implement the logic for figuring out the
		// object that should be focused from the point of touch
		// and send an appropriate message to the callback.
		Scene scene = Scene.getInstance();
		scene.unselectAllEntities();
		SceneEntity touchedEntity = scene.getTouchedEntity(screenX, screenY);
		if (touchedEntity != null) {
			scene.markSelected(touchedEntity);
			updateInfo(touchedEntity.modelInstance.nodes.get(0).id + " selected");
		} else {
			updateInfo("None selected");
		}
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
	
	public void updateInfo(String message) {
		for (OnInfoNeedsUpdateListener listener : listeners) {
			listener.onInfoNeedsUpdate(message);
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