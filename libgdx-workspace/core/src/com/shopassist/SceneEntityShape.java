package com.shopassist;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Every interactive SceneEntity should be associated with a
 * SceneEntityShape. This is useful for detecting if the
 * entity has been touched or for determining if the entity
 * lies in the field of view of the camera(frustum culling).
 * 
 * Methods:
 * [invisible]: For checking visibility in a camera orientation
 * [intersects]: For checking if a ray interesects this shape
 * when some transform has been applied to it.
 * 
 * @author srbs
 *
 */
abstract class SceneEntityShape {
	// For use in intersects.
	protected final static Vector3 position = new Vector3();
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    
    public SceneEntityShape(BoundingBox bounds) {
        center.set(bounds.getCenter());
        dimensions.set(bounds.getDimensions());
    }
    
    abstract boolean isVisible(Matrix4 transform, Camera cam);
    abstract float intersects(Matrix4 transform, Ray ray);
}

class Sphere extends SceneEntityShape {
	float radius;
	public Sphere(BoundingBox bounds) {
		super(bounds);
        radius = dimensions.len()/2;;
    }
	
	@Override
	public boolean isVisible(Matrix4 transform, Camera cam) {
        return cam.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
    }
    
	@Override
    public float intersects(Matrix4 transform, Ray ray) {
        transform.getTranslation(position).add(center);
        final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
        if (len < 0f)
            return -1f;
        float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
        return (dist2 <= radius * radius) ? dist2 : -1f;
    }
}

class Cuboid extends SceneEntityShape {

	public Cuboid(BoundingBox bounds) {
		super(bounds);
	}

	@Override
	boolean isVisible(Matrix4 transform, Camera cam) {
		return cam.frustum.boundsInFrustum(transform.getTranslation(position).add(center), dimensions);
	}

	@Override
	float intersects(Matrix4 transform, Ray ray) {
		transform.getTranslation(position).add(center);
        if (Intersector.intersectRayBoundsFast(ray, position, dimensions)) {
            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            return position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
        }
        return -1f;
	}
	
}
