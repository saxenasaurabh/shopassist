package com.shopassist;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;

public class ModelLoader implements Disposable {
	// TODO(srbs) Move this to common resources file
	private final String MAP_FILE_NAME = "map_with_3_racks.g3dj";
	private static ModelLoader currentInstance;
	private AssetManager assetManager = new AssetManager();
	private boolean loadingDone = true;
	
	private ModelLoader() {
		assert(currentInstance == null);
		currentInstance = this;
	}
	
	public static ModelLoader getInstance() {
		return currentInstance != null ? currentInstance :
			new ModelLoader();
	}
	
	public void load() {
		loadingDone = false;
		assetManager.load(MAP_FILE_NAME, Model.class);
	}
	
	public Model getModel() {
		assert(finishedLoading());
		return assetManager.get(MAP_FILE_NAME, Model.class);
	}
	
	public boolean finishedLoading() {
		return loadingDone || (loadingDone = assetManager.update());
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}
}