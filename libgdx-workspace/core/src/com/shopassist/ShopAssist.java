package com.shopassist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ApplicationListener;

public class ShopAssist implements ApplicationListener {
//	static final String RACK_PREFIX = "rack";
	public static final String TAG = "LOGGING_TAG";
	private Scene scene = Scene.getInstance();
	

	public ModelLoader modelLoader = ModelLoader.getInstance();
	boolean loading = false;

	// Updates the info panel.
	public InfoUpdater infoUpdater = new InfoUpdater();
	
	@Override
	public void create () {
		// Load map.
		loading = true;
		modelLoader.load();
		// Initialize camera, environment etc. in the scene.
		scene.init();

//		InputMultiplexer inputMultiplexer = new InputMultiplexer();
//		inputMultiplexer.addProcessor(scene.camController);
//		inputMultiplexer.addProcessor(infoUpdater);
//		Gdx.input.setInputProcessor(inputMultiplexer);
		
		InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		if (inputMultiplexer == null) {
			Gdx.input.setInputProcessor((new InputMultiplexer(infoUpdater)));
		} else {
			inputMultiplexer.addProcessor(infoUpdater);
		}
	}

	private void doneLoading () {
		scene.loadModels();
		loading = false;
	}

	@Override
	public void render () {
		if (loading && modelLoader.finishedLoading()) {
			doneLoading();
		}
		if (!loading) {
			scene.render();
		}
	}

	@Override
	public void dispose () {
		scene.dispose();
		modelLoader.dispose();
		infoUpdater.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
