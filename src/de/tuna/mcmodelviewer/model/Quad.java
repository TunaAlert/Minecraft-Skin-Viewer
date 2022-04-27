package de.tuna.mcmodelviewer.model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Quad {
	
	public Vector3f[] v;
	public Vector2f[] t;
	public Vector3f n;
	
	public Quad() {
		v = new Vector3f[4];
		t = new Vector2f[4];
		for (int i = 0; i < 4; i++) {
			v[i] = new Vector3f();
			t[i] = new Vector2f();
		}
		n = new Vector3f();
	}
	
	public Quad(Vector3f[] v, Vector3f n) {
		this.v = v;
		t = new Vector2f[4];
		for (int i = 0; i < 4; i++) {
			t[i] = new Vector2f();
		}
		this.n = n;
	}
	
	@Override
	protected Quad clone() {
		Quad clone = new Quad();
		for (int i = 0; i < 4; i++) {
			clone.v[i].set(v[i]);
			clone.t[i].set(t[i]);
		}
		clone.n.set(n);
		return clone;
	}

	public void rotate(float pitch, float yaw, float roll) {
		for (int i = 0; i < v.length; i++) {
			rotateVector(v[i], pitch, yaw, roll);
		}
		rotateVector(n, pitch, yaw, roll);
	}
	
	private void rotateVector(Vector3f v, float pitch, float yaw, float roll) {
		final float cosp = (float)Math.cos(pitch);
		final float sinp = (float)Math.sin(pitch);
		final float cosy = (float)Math.cos(yaw);
		final float siny = (float)Math.sin(yaw);
		final float cosr = (float)Math.cos(roll);
		final float sinr = (float)Math.sin(roll);
		Vector3f vz = new Vector3f(
				v.x * cosr - v.y * sinr,
				v.x * sinr + v.y * cosr,
				v.z);
		Vector3f vx = new Vector3f(
				vz.x,
				vz.y * cosp - vz.z * sinp,
				vz.y * sinp + vz.z * cosp);
		Vector3f vy = new Vector3f(
				vx.x * cosy - vx.z * siny,
				vx.y,
				vx.x * siny + vx.z * cosy);
		v.set(vy);
	}
	
}
