package com.shopassist;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

class SceneEntity {
	static final SceneEntityState DEFAULT_ENTITY_STATE = SceneEntityState.NOT_SELECTED;
	
	ModelInstance modelInstance;
	SceneEntityState state;
	
	public SceneEntity(ModelInstance modelInstance) {
		this(modelInstance, DEFAULT_ENTITY_STATE);
	}
	
	public SceneEntity(ModelInstance modelInstance, SceneEntityState state) {
		this.modelInstance = modelInstance;
		this.state = state;
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