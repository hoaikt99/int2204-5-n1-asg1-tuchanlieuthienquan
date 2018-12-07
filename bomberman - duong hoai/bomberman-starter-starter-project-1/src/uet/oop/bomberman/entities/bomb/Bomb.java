package uet.oop.bomberman.entities.bomb;

import com.sun.org.apache.xpath.internal.operations.Mod;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.entities.character.Character;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;

public class Bomb extends AnimatedEntitiy {

	protected double _timeToExplode = 120; //2 seconds
	public int _timeAfter = 20;
	
	protected Board _board;
	protected Flame[] _flames=null;
	protected boolean _exploded = false;
	protected boolean _allowedToPassThru = true;
	
	public Bomb(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;
	}

	@Override
	public void update() {
		if(_timeToExplode > 0) 
			_timeToExplode--;
		else {
			if(!_exploded) 
				explosion();
			else
				updateFlames();
			
			if(_timeAfter > 0) 
				_timeAfter--;
			else
				remove();
		}
			
		animate();
	}
	
	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite =  Sprite.bomb_exploded2;
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);
		
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		
		screen.renderEntity(xt, yt , this);
	}
	
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}
	
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}

    /**
     * Xử lý Bomb nổ
     */
	public void explode() {
		_timeToExplode = 0;
	}
	// TODO: x? l? khi Character �?ng t?i v? tr� Bomb
	protected void explosion() {
		_allowedToPassThru = true;
		_exploded = true;

		Character a = _board.getCharacterAt(_x, _y);
		if(a != null)  {
			a.kill();
		}
		
	// TODO: t?o c�c Flame
		_flames = new Flame[4];

		for (int i = 0; i < _flames.length; i++) {
			_flames[i] = new Flame((int)_x, (int)_y, i, Game.getBombRadius(), _board);
		}

	}
	
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;
		
		for (int i = 0; i < _flames.length; i++) {
			if(_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if(e != null) return e;
		}
		
		return null;
	}

	@Override
	public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
        // TODO: xử lý va chạm với Flame của Bomb khác
		if(e instanceof Character) {
			double diffX = e.getX() - Coordinates.tileToPixel(getX());
			double diffY = e.getY() - Coordinates.tileToPixel(getY());

			if(!(diffX >= -10 && diffX < 16 && diffY >= 1 && diffY <= 28)) { // differences to see if the player has moved out of the bomb, tested values
				_allowedToPassThru = false;
			}

			return _allowedToPassThru;
		}

		if(e instanceof Flame) {
			explode();
			return true;
		}
		return false;
	}
}
