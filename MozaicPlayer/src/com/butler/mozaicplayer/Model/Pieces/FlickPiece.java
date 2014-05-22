package com.butler.mozaicplayer.Model.Pieces;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.Model.FlickPuzzle;
import com.butler.mozaicplayer.Model.Puzzle;


public class FlickPiece extends Piece {

	public FlickPiece(Puzzle puzzle, Polygon polygon, Array<Polygon> triangles, float rot, Color color, int id, float oX, float oY, float mass) {
		super(puzzle, polygon, triangles, rot, color, id, oX, oY);
		this.mass = mass;
	}

	private Vector2 velocity;
	private float speed;
	private float rotationSpeed;
	private float mass;
	private float angle;
	private boolean stationary = true;
	private boolean rotating = false;
	private boolean outsideBoundsVert = true;
	private boolean outsideBoundsHor = false;
	private boolean isOverlapping = false;
	private boolean initOutsideBounds;
	private int counter = 1000;
	private FlickPiece overlapping;
	
	public void update() {
		
		if (initOutsideBounds) {
			initOutsideBounds = outsideBoundsHor || outsideBoundsVert;
			if (counter > 0)
				counter--;
			else {
				puzzle.delete(this);
				return;
			}
		}
		
		if (rotating)
			rotate(rotationSpeed);

		if (stationary)
			return;
	
		translateWOC(velocity.x, velocity.y);
		
		float x = getTransX();
		float y = getTransY();
		
		
		
		
		if (!outsideBoundsVert) {
			if (((FlickPuzzle) puzzle).overlapsVert(this)) {
				outsideBoundsVert = true;
				velocity.y = -velocity.y;
				angle = velocity.angle();
			}
		}
		if (!outsideBoundsHor) {
			if (((FlickPuzzle) puzzle).overlapsHor(this)) {
				outsideBoundsHor = true;
				velocity.x = -velocity.x;
				angle = velocity.angle();
			}
		}
		
		if (outsideBoundsHor) {
			if (!((FlickPuzzle) puzzle).overlapsHor(this))
				outsideBoundsHor = false;
		}
		if (outsideBoundsVert) {
			if (!((FlickPuzzle) puzzle).overlapsVert(this))
				outsideBoundsVert = false;
		}
		
		if (!outsideBoundsHor && !outsideBoundsVert) {

			FlickPiece overlapsFlick = ((FlickPuzzle) puzzle).overlapsFlick(this);
			
			if (overlapsFlick == overlapping)
				return;
			
			if (overlapsFlick != null) {
				if (overlapsFlick.outsideBoundsHor || overlapsFlick.outsideBoundsVert || overlapsFlick.stationary || overlapsFlick.isOverlapping) { //TODO Calculations correct??
//					System.out.println("derp");
					float s1 = speed;
					float a1 = angle;
					float contactAngle = getContactAngle(overlapsFlick);
					float v = (float) (s1*Math.cos(a1 - contactAngle));
					velocity.x = (float) (v * Math.cos(contactAngle) + s1 * Math.sin(a1 - contactAngle) * Math.cos(contactAngle + Math.PI/2));
					velocity.y = (float) (v * Math.sin(contactAngle) + s1 * Math.sin(a1 - contactAngle) * Math.sin(contactAngle + Math.PI/2));
				}
				else {
					float m1 = mass;
					float m2 = overlapsFlick.mass;
					float s1 = speed;
					float s2 = overlapsFlick.speed;
					float a1 = angle;
					float a2 = overlapsFlick.angle;
					float contactAngle = getContactAngle(overlapsFlick);
					float v = (float) (s1*Math.cos(a1 - contactAngle) * (m1 - m2) + 2*m2*s2*Math.cos(a2 - contactAngle)) / (m1 + m2);
					velocity.x = (float) (v * Math.cos(contactAngle) + s1 * Math.sin(a1 - contactAngle) * Math.cos(contactAngle + Math.PI/2));
					velocity.y = (float) (v * Math.sin(contactAngle) + s1 * Math.sin(a1 - contactAngle) * Math.sin(contactAngle + Math.PI/2));
		
					v = (float) (s2*Math.cos(a2 - contactAngle) * (m2 - m1) + 2*m1*s1*Math.cos(a1 - contactAngle)) / (m1 + m2);
					overlapsFlick.velocity.x = (float) (v * Math.cos(contactAngle) + s2 * Math.sin(a2 - contactAngle) * Math.cos(contactAngle + Math.PI/2));
					overlapsFlick.velocity.y = (float) (v * Math.sin(contactAngle) + s2 * Math.sin(a2 - contactAngle) * Math.sin(contactAngle + Math.PI/2));
					
					updateSpeedAndAngle();
					overlapsFlick.updateSpeedAndAngle();
					isOverlapping = true;
					overlapping = overlapsFlick;
					overlapsFlick.overlapping = this;
				}
			}
			else {
				isOverlapping = false;
				overlapping = null;
			}
			
			float originalRotation = getRotation();
			
			if (puzzle.snap(this)) {
				if (puzzle.overlaps(this)) { //TODO incorrect behaviour?
					System.out.println("snapderp");
					float x_ = getTransX();
					float y_ = getTransY();
					translate(x - x_, y - y_);
					fixRotation(originalRotation);
				}
				else {
					setVelocity(0, 0);
					setRotationSpeed(0);
				}
			}
		}
	}
	
	private float getContactAngle(FlickPiece overlapsFlick) {
		Vector2 v = overlapsFlick.getCentre().sub(getCentre());
		return (float) Math.atan2(v.y, v.x);
	}

	private Vector2 getCentre() {
		float [] transformedVertices = getVerts();
		int n = transformedVertices.length;
		float x = 0, y = 0;
		for (int i = 0; i < n; i+=2) {
			x += transformedVertices[i];
			y += transformedVertices[i+1];
		}
		x /= (n/2);
		y /= (n/2);
		return new Vector2(x, y);
	}

	private void updateSpeedAndAngle() {
		speed = velocity.len(); // TODO correct?
		angle = velocity.angle();
	}
	
	public void setRotationSpeed(float rotSpeed) {
		rotationSpeed = rotSpeed;
		rotating = (rotSpeed != 0);
	}
	
	public void setVelocity(float x, float y) {
		velocity = new Vector2(x, y);
		stationary = (x == 0 && y == 0);
		updateSpeedAndAngle();
	}
	
	public void init() {
		FlickPuzzle p = (FlickPuzzle) puzzle;
		outsideBoundsVert = p.overlapsVert(this);
		outsideBoundsHor = p.overlapsHor(this);
		initOutsideBounds = outsideBoundsHor || outsideBoundsVert;
	}
	
//	@Override
//	public Piece clonef() {
//		Color c = color.cpy();
//		c.a = 0.5f;
//		Polygon triangle_;
//		Array<Polygon> triangles_ = new Array<Polygon>(false, 4);
//		for (Polygon triangle: triangles) {
//			triangle_ = new Polygon(triangle.getTransformedVertices());
//			triangles_.add(triangle_);
//		}
//		FlickPiece piece = new FlickPiece(puzzle, new Polygon(getVerts()), triangles_, 0, c, -1, polygon.getOriginX(), polygon.getOriginY(), mass);
//		clone = piece;
//		return piece;
//	}
}
