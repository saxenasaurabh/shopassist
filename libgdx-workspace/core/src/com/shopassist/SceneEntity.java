package com.shopassist;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;

class SceneEntity {
	static final SceneEntityState DEFAULT_ENTITY_STATE = SceneEntityState.NOT_SELECTED;
	
	ModelInstance modelInstance;
	SceneEntityState state;
	SceneEntityShape shape;
	
	public SceneEntity(ModelInstance modelInstance) {
		this(modelInstance, DEFAULT_ENTITY_STATE);
	}
	
	public SceneEntity(ModelInstance modelInstance, SceneEntityState state) {
		this.modelInstance = modelInstance;
		this.state = state;
		BoundingBox boundingBox = new BoundingBox();
		// By default we are assuming all shapes to be cuboids. This should
		// work for the racks but might fail when we have more objects.
		this.shape = new Cuboid(modelInstance.calculateBoundingBox(boundingBox));
	}
	
	@Override
	public boolean equals(Object other) {
		return modelInstance.equals(other);
	}
	
	@Override
	public int hashCode() {
		return modelInstance.hashCode();
	}
}