package com.butler.mozaicplayer.Model.Pieces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.butler.mozaicplayer.Model.Puzzle;

public class SpritePiece extends Piece {
	
	private Sprite sprite;
	private Texture texture;
	private Rectangle rect;

	public SpritePiece(Puzzle puzzle, Polygon polygon, Array<Polygon> triangles, float rot,
			Color color, int id, float oX, float oY, Texture texture) {
		super(puzzle, polygon, triangles, rot, color, id, oX, oY);
		
		this.texture = texture;	// For passing to a cloned piece
		
		sprite = new Sprite(texture);
//		sprite.setOrigin(oX, oY);
		rect = polygon.getBoundingRectangle();
		sprite.setOrigin(oX - rect.x, oY - rect.y);
		sprite.setColor(1, 1, 1, color.a);
//		sprite.setX(polygon.getTransformedVertices()[0]);
//		sprite.setY(polygon.getTransformedVertices()[1]);
		sprite.setBounds(rect.x, rect.y, rect.width, rect.height);
	}
	
	private SpritePiece(Puzzle puzzle, Polygon polygon, Array<Polygon> triangles, float rot,
			Color color, int id, float oX, float oY, Texture texture, Sprite sprite_, Rectangle rect) {
		super(puzzle, polygon, triangles, rot, color, id, oX, oY);
		
		// Doesn't need saving since we never clone a clone
		sprite = new Sprite(texture);
		sprite.setColor(color);
		
		sprite.setBounds(rect.x, rect.y, rect.width, rect.height);
		
		sprite.setOrigin(sprite_.getOriginX(), sprite_.getOriginY());
		sprite.setX(sprite_.getX());
		sprite.setY(sprite_.getY());
		sprite.setRotation(sprite_.getRotation());
	}
	
	
	
	// Only called by constructor which is called before the sprite is initialised, so useless
//	@Override
//	protected void setOrigin(float x, float y) {
//		super.setOrigin(x, y);
//		sprite.setOrigin(x, y);
//	}
	
	@Override
	public Piece clonef() {
		Color c = color.cpy();
		c.a = 0.5f;
		Polygon triangle_;
		Array<Polygon> triangles_ = new Array<Polygon>(false, 4);
		for (Polygon triangle: triangles) {
			triangle_ = new Polygon(triangle.getTransformedVertices());
			triangles_.add(triangle_);
		}
		Piece piece = new SpritePiece(puzzle, new Polygon(getVerts()), triangles_, 0, c, -1, polygon.getOriginX(), polygon.getOriginY(), texture, sprite, rect);
//		clone = piece;
		puzzle.setClone(piece);
		return piece;
	}

	
	@Override
	public void translateWOC(float x, float y) {
		super.translateWOC(x, y);
		sprite.translate(x, y);
	}
	
	@Override
	public boolean translate(float x, float y) {
		if (super.translate(x, y)) {
			sprite.translate(x, y);
			return true;
		}
		else return false;	
	}

	
	@Override
	public void rotate(float degrees) {
		super.rotate(degrees);
		sprite.rotate(degrees);
	}
	
	@Override
	public void setRotation(float degrees) {
		super.setRotation(degrees);
		sprite.setRotation(degrees);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}
	
	// Overridden for clarity only
	@Override
	public void draw(ShapeRenderer sr) {
		System.out.println("Should not be drawn this way");
	}
}