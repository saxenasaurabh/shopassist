package com.shopassist;

import java.util.HashSet;

/**
 * A store for all the models in the scene.
 * Maintains a HashMap of the objects for faster lookup.
 * @author srbs
 *
 */
@SuppressWarnings("serial")
class SceneEntityStore extends HashSet<SceneEntity> {
	SceneEntity[] getListOfEntities() {
		SceneEntity[] listOfEntities = new SceneEntity[0];
		return toArray(listOfEntities);
	}
}