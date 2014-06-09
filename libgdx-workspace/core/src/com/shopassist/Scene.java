package com.shopassist;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Disposable;

class Scene implements Disposable {
	/// Every app instance should have just one scene.
	/// Keeping this static variable will help avoid
	/// passing it around to inputControllers etc.
	private static Scene currentInstance;
	
	/// List of models in the scene.
	SceneEntityStore sceneEntityStore = new SceneEntityStore();
	
	/// Models to render.
	/// This is a subset of [models]. Placing only
	/// those models in this list that are visible will
	/// help optimize rendering time. This will require another
	/// input processor that updates this list based on the camera
	/// view. By default all models are added to this list.
	/// TODO(srbs) Implement proper functionality for this.
	List<ModelInstance> modelsToRender = new ArrayList<ModelInstance>();
	
	public ModelLoader modelLoader = ModelLoader.getInstance();
	public PerspectiveCamera cam;
	public Environment lights;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	private boolean modelsLoaded = false;
	
	public static Scene getInstance() {
		return currentInstance != null ? currentInstance : new Scene();
	}
	
	private Scene() {
		assert(currentInstance == null);
		currentInstance = this;
	}
	
	void addModel(ModelInstance modelInstance) {
		sceneEntityStore.add(new SceneEntity(modelInstance));
	}
	
	void addModel(SceneEntity model) {
		sceneEntityStore.add(model);
	}
	
	void removeModel(SceneEntity sceneEntity) {
		sceneEntityStore.remove(sceneEntity);
	}
	
	void removeModel(ModelInstance modelInstance) {
		sceneEntityStore.remove(new SceneEntity(modelInstance));
	}
	
	void clearModels() {
		sceneEntityStore.clear();
	}
	
	/**
	 * Initializes camera and scene environment.
	 * Does not load data models.
	 */
	void init() {
		modelBatch = new ModelBatch();
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 150f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();
		
		camController = new CameraInputController(cam);
		InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		if (inputMultiplexer == null) {
			Gdx.input.setInputProcessor((new InputMultiplexer(camController)));
		} else {
			inputMultiplexer.addProcessor(camController);
		}
		
		clearModels();
	}
	
	void loadModels() {
		assert(modelLoader.finishedLoading());
		if (!modelsLoaded) {
			Model model = modelLoader.getModel();
			Gdx.app.log(ShopAssist.TAG, "Number of nodes: " + model.nodes.size);
			// The scene map_with_3_racks.g3dj contains three racks
			// with ids rack1, rack2 and rack3.
			for (int i = 0; i < model.nodes.size; i++) {
				String id = model.nodes.get(i).id;
				Gdx.app.log(ShopAssist.TAG, "Processing node " + id);
				ModelInstance instance = new ModelInstance(model, id);
				Node node = instance.getNode(id);
	
				instance.transform.set(node.globalTransform);
				node.translation.set(0, 0, 0);
				node.scale.set(1, 1, 1);
				node.rotation.idt();
				instance.calculateTransforms();
				addModel(instance);
				modelsToRender.add(instance);
			}
			modelsLoaded = true;
		}
	}
	
	void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		for (ModelInstance modelInstance : modelsToRender)
			modelBatch.render(modelInstance, lights);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		clearModels();
	}
}
