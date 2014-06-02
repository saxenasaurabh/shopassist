package com.shopassist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;

public class ShopAssist implements ApplicationListener {
	static final String RACK_PREFIX = "rack";
	public static final String TAG = "LOGGING_TAG";
	
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Environment lights;
	public boolean loading;
	public Array<ModelInstance> racks = new Array<ModelInstance>();

	public InfoUpdater infoUpdater = new InfoUpdater();
	
	@Override
	public void create () {
		modelBatch = new ModelBatch();
		lights = new Environment();
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 150f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();

		InputMultiplexer multiplexer = new InputMultiplexer();
		camController = new CameraInputController(cam);
		multiplexer.addProcessor(camController);
		multiplexer.addProcessor(infoUpdater);
		Gdx.input.setInputProcessor(multiplexer);

		assets = new AssetManager();
		assets.load("map_with_3_racks.g3dj", Model.class);
		loading = true;
	}

	private void doneLoading () {
		Model model = assets.get("map_with_3_racks.g3dj", Model.class);
		Gdx.app.log(TAG, "Number of nodes: " + model.nodes.size);
		for (int i = 0; i < model.nodes.size; i++) {
			String id = model.nodes.get(i).id;
			Gdx.app.log(TAG, "Processing node " + id);
			ModelInstance instance = new ModelInstance(model, id);
			Node node = instance.getNode(id);

			instance.transform.set(node.globalTransform);
			node.translation.set(0, 0, 0);
			node.scale.set(1, 1, 1);
			node.rotation.idt();
			instance.calculateTransforms();

			if (id.startsWith(RACK_PREFIX)) {
				racks.add(instance);
				continue;
			} else {
				// Handle other types of nodes
				Gdx.app.error(TAG, "Invalid node id " + id);
			}
		}
		loading = false;
	}

	@Override
	public void render () {
		if (loading && assets.update()) doneLoading();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		for (ModelInstance rack : racks)
			modelBatch.render(rack, lights);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		racks.clear();
		assets.dispose();
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
