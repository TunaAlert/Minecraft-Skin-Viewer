package de.tuna.mcmodelviewer.model;

import org.lwjgl.util.vector.Vector3f;

public class Cuboid {
	
	public static final String FACE0 = "right";
	public static final String FACE1 = "left";
	public static final String FACE2 = "bottom";
	public static final String FACE3 = "top";
	public static final String FACE4 = "back";
	public static final String FACE5 = "front";
	
	public Vector3f origin;
	public Quad[] quads = new Quad[] {
			//right
			new Quad(new Vector3f[] {
					new Vector3f(0, 1, 0),
					new Vector3f(0, 1, 1),
					new Vector3f(0, 0, 1),
					new Vector3f(0, 0, 0)
			}, new Vector3f(-1, 0, 0)),
			//left
			new Quad(new Vector3f[] {
					new Vector3f(1, 1, 1),
					new Vector3f(1, 1, 0),
					new Vector3f(1, 0, 0),
					new Vector3f(1, 0, 1),
			}, new Vector3f(1, 0, 0)),
			//bottom
			new Quad(new Vector3f[] {
					new Vector3f(1, 0, 0),
					new Vector3f(0, 0, 0),
					new Vector3f(0, 0, 1),
					new Vector3f(1, 0, 1)
			}, new Vector3f(0, -1, 0)),
			//top
			new Quad(new Vector3f[] {
					new Vector3f(0, 1, 0),
					new Vector3f(1, 1, 0),
					new Vector3f(1, 1, 1),
					new Vector3f(0, 1, 1)
			}, new Vector3f(0, 1, 0)),
			//back
			new Quad(new Vector3f[] {
					new Vector3f(1, 1, 0),
					new Vector3f(0, 1, 0),
					new Vector3f(0, 0, 0),
					new Vector3f(1, 0, 0)
			}, new Vector3f(0, 0, -1)),
			//front
			new Quad(new Vector3f[] {
					new Vector3f(0, 1, 1),
					new Vector3f(1, 1, 1),
					new Vector3f(1, 0, 1),
					new Vector3f(0, 0, 1)
			}, new Vector3f(0, 0, 1)),
	};

	public Cuboid() {
		origin = new Vector3f();
	}

	public Cuboid(float x, float y, float z, float dx, float dy, float dz) {
		this();
		scale(dx, dy, dz);
		translate(x, y, z);
	}

	public void scale(float sx, float sy, float sz) {
		for (int i = 0; i < quads.length; i++) {
			Quad q = quads[i];
			for (int j = 0; j < q.v.length; j++) {
				q.v[j].x *= sx;
				q.v[j].y *= sy;
				q.v[j].z *= sz;
			}
		}
	}

	public void translate(float x, float y, float z) {
		for (int i = 0; i < quads.length; i++) {
			Quad q = quads[i];
			for (int j = 0; j < q.v.length; j++) {
				q.v[j].translate(x, y, z);
			}
		}
	}
	
	@Override
	public Cuboid clone() {
		Cuboid clone = new Cuboid();
		for (int i = 0; i < quads.length; i++) {
			clone.quads[i] = quads[i].clone();
		}
		clone.origin = new Vector3f(origin);
		return clone;
	}

	public void rotate(float pitch, float yaw, float roll) {
		for (int i = 0; i < quads.length; i++) {
			quads[i].rotate(pitch, yaw, roll);
		}
	}

	public void translateToOrigin() {
		translate(origin.x, origin.y, origin.z);
		origin.set(0, 0, 0);
	}
	
}
