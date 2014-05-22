package com.butler.mozaicplayer.Model.Pieces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.Model.Puzzle;


public class Piece {
	
	/**
	 * Describing the shape of the piece
	 */
	protected Polygon polygon;
	/**
	 * Non-overlapping triangles whose union equals the entire shape
	 */
	protected Array<Polygon> triangles;
	/**
	 * The colour of the shape
	 */
	protected Color color;
	/**
	 * The puzzle this piece belongs to
	 */
	protected Puzzle puzzle;
	/**
	 * A value representing the type of piece this is within a particular puzzle
	 */
	private int id;
	/**
	 * The number of the shape is composed of
	 */
	private int size;

	/**
	 * @param puzzle	The puzzle whose overlaps() method this piece will call
	 * @param polygon	A polygon defining the shape of this piece
	 * @param triangles	Non-overlapping triangles whos union equals the entire shape
	 * @param rot		
	 * @param color
	 * @param id
	 * @param oX
	 * @param oY
	 */
	public Piece(Puzzle puzzle, Polygon polygon, Array<Polygon> triangles, float rot, Color color, int id, float oX, float oY) {
		this.puzzle = puzzle;
		this.polygon = polygon;
		this.triangles = triangles;
		polygon.setRotation(rot); // TODO rot not required?
		this.color = color;
		this.id = id;
		setOrigin(oX, oY);
		puzzle.pieces.add(this);
		size = triangles.size;
	}
	
	/**
	 * @param originalRot
	 * @return
	 */
	public boolean fixRotation(float originalRot) {
		if (puzzle.overlaps(this)) {
			setRotation(originalRot);
			return true;
		}
		return false;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	private void setOrigin(float x, float y) {
		polygon.setOrigin(x, y);
		for (Polygon triangle : triangles)
			triangle.setOrigin(x, y);
	}
	
	/**
	 * @return
	 */
	public float getTransY() {
		return polygon.getY();
	}
	
	/**
	 * @return
	 */
	public float getTransX() {
		return polygon.getX();
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean translate(float x, float y) {
		for (Polygon triangle : triangles)
			triangle.translate(x, y);
			
		if (puzzle.overlaps(this)) {
			for (Polygon triangle : triangles)
				triangle.translate(-x, -y);
			return false;
		}	
		
		polygon.translate(x, y);
		
		return true;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void translateWOC(float x, float y) {
		for (Polygon triangle : triangles)
			triangle.translate(x, y);
		
		polygon.translate(x, y);
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public float getAngle(float x, float y) {
		Vector2 centre = centre(getVerts());
		float rx = x - centre.x;
		float ry = y - centre.y;
		float rot = (float) Math.toDegrees(Math.atan(ry / rx));
		if (rot < 0)
			rot += 180;
		if (ry < 0)
			rot += 180;
		return rot;
	}
	
	/**
	 * @param transformedVertices
	 * @return
	 */
	private Vector2 centre(float[] transformedVertices) {
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
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean contains(float x, float y) {
		for (Polygon triangle : triangles) {
			if (triangle.contains(x, y))
				return true;
		}
		return false;
	}
	
	/**
	 * @param i
	 * @return
	 */
	public Polygon get(int i) {
		return triangles.get(i);
	}
	
	/**
	 * @return
	 */
	public Piece clonef() {
		Color c = color.cpy();
		c.a = 0.5f;
		Polygon triangle_;
		Array<Polygon> triangles_ = new Array<Polygon>(false, 4);
		for (Polygon triangle: triangles) {
			triangle_ = new Polygon(triangle.getTransformedVertices());
			triangles_.add(triangle_);
		}
		Piece piece = new Piece(puzzle, new Polygon(getVerts()), triangles_, 0, c, -1, polygon.getOriginX(), polygon.getOriginY());
		puzzle.setClone(piece);
		return piece;
	}
	
	/**
	 * @return
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * @param degrees
	 */
	public void rotate(float degrees) {	
		polygon.rotate(degrees);
		for (Polygon triangle : triangles)
			triangle.rotate(degrees);
	}
	
	/**
	 * @return
	 */
	public float getRotation() {
		return polygon.getRotation();
	}
	
	/**
	 * @param degrees
	 */
	protected void setRotation(float degrees) {
		polygon.setRotation(degrees);
		for (Polygon triangle : triangles)
			triangle.setRotation(degrees);
	}
	
	/**
	 * @return
	 */
	public float[] getVerts() {
		return polygon.getTransformedVertices();
	}
	
	/**
	 * @return
	 */
	public int getSize() {
		return size;
	}
	
	
	/**
	 * @param sr
	 */
	public void draw(ShapeRenderer sr) {
		float [] verts;
		sr.setColor(color);
		for (Polygon triangle : triangles) {
			verts = triangle.getTransformedVertices();
			sr.filledTriangle(verts[0], verts[1], verts[2], verts[3], verts[4], verts[5]);
		}
	}
	
	/**
	 * @param v0
	 * @param v1
	 * @param u0
	 * @param u1
	 */
	public void fix(Vector2 v0, Vector2 v1, Vector2 u0, Vector2 u1){
		float d0 = v0.dst(u0);
		float d1 = v1.dst(u1);
		
		
		if (d0 > d1) {
			Vector2 v = v0;
			v0 = v1;
			v1 = v;
			v = u0;
			u0 = u1;
			u1 = v;
		}
				
		//TODO
		// Could use i.e Math.abs(d0 - d1) < 0.05 instead of computing rot?
		if (d0 != d1) {
			
			float a = (float) ( Math.pow((v1.x - v0.x), 2) + Math.pow((v1.y - v0.y), 2) );
			float b = (float) ( Math.pow((u1.x - u0.x), 2) + Math.pow((u1.y - u0.y), 2) );
			float c = (float) ( Math.pow(((u1.x + v0.x - u0.x) - v1.x), 2) + Math.pow(((u1.y + v0.y - u0.y) - v1.y), 2) );
			float rot = (float) Math.toDegrees( Math.acos( (a+b-c) / (2 * Math.sqrt(a) * Math.sqrt(b)) ) );
			if ( rot > 0.5 ) {
				float dx = u1.x - u0.x;
				float dx2 = dx * dx;
				float dy = u1.y - u0.y;
				float dy2 = dy * dy;
				float ax = v0.x;
				float ay = v0.y;
				float bx = v1.x;
				float by = v1.y;
				float s_ = ( dy2 * dx * (bx - ax) - dx2 * dy * (by - ay) ) / ( dy2 - dx2 );
				float s = ( bx - ax - s_ / dx ) / dx;
				
				Vector2 x_ = new Vector2(s * dx + ax, s * dy + ay);
				
				
				if (Math.abs(dy) > Math.abs(dx)) {			
					if (dy > 0 && v1.x < x_.x)
						rot = - rot;
					if (dy < 0 && v1.x > x_.x)
						rot = - rot;
				}
				
				else {
					if (dx > 0 && v1.y > x_.y)
						rot = - rot;
					if (dx < 0 && v1.y < x_.y)
						rot = - rot;
				}
				
				rotate(rot);
			}
		}
		
		Vector2 v_ = getClosest(u1);
		translateWOC(u1.x - v_.x, u1.y - v_.y);

	}
	
	/**
	 * @param u
	 * @return
	 */
	private Vector2 getClosest(Vector2 u) {
		
		float[] coords = getVerts();
		int l = coords.length/2;
		Array<Vector2> verts = new Array<Vector2>(false, l);
		float[] dsts = new float[l];
		Vector2 v;
		float min = 1000;
		
		for (int i = 0; i < l*2; i++) {
			v = new Vector2(coords[i], coords[++i]);
			verts.add(v);
			dsts[i/2] = v.dst(u);
			min = Math.min(dsts[i/2], min);
		}

		for (int i = 0; i < l; i++) {
			if (dsts[i] == min)
				return verts.get(i);
		}
				
		return null;
	}

	/**
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		System.out.println("Should not be drawn this way");
	}

	/**
	 * @return
	 */
	public Color getColor() {
		return color.cpy();
	}

	/**
	 * @param currentColor
	 */
	public void setColor(Color currentColor) {
		color = currentColor;
	}
}
