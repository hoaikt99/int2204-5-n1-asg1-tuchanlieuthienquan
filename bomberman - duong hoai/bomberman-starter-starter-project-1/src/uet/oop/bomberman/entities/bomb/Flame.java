package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;

public class Flame extends Entity {

	protected Board _board;
	protected int _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments;
//	protected FlameSegment[] _flameSegments = new FlameSegment[0];

	/**
	 *
	 * @param x hoành ð? b?t ð?u c?a Flame
	 * @param y tung ð? b?t ð?u c?a Flame
	 * @param direction là hý?ng c?a Flame
	 * @param radius ð? dài c?c ð?i c?a Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = direction;
		_radius = radius;
		_board = board;
		createFlameSegments();
	}

	/**
	 * T?o các FlameSegment, m?i segment ?ng m?t ðõn v? ð? dài
	 */
	private void createFlameSegments() {
		/**
		 * tính toán ð? dài Flame, týõng ?ng v?i s? lý?ng segment
		 */
		_flameSegments = new FlameSegment[calculatePermitedDistance()];

		/**
		 * bi?n last dùng ð? ðánh d?u cho segment cu?i cùng
		 */
		boolean last = false;

		// TODO: t?o các segment dý?i ðây



		int x = (int)_x;
		int y = (int)_y;
		for (int i = 0; i < _flameSegments.length; i++) {
			last = i == _flameSegments.length -1 ? true : false;

			switch (_direction) {
				case 0: y--; break;
				case 1: x++; break;
				case 2: y++; break;
				case 3: x--; break;
			}
			_flameSegments[i] = new FlameSegment(x, y, _direction, last,_board);
		}
	}

	/**
	 * Tính toán ð? dài c?a Flame, n?u g?p v?t c?n là Brick/Wall, ð? dài s? b? c?t ng?n
	 * @return
	 */
	private int calculatePermitedDistance() {
		// TODO: th?c hi?n tính toán ð? dài c?a Flame
		int radius = 0;
		int x = (int)_x;
		int y = (int)_y;
		while(radius < _radius) {
			if(_direction == 0) y--;
			if(_direction == 1) x++;
			if(_direction == 2) y++;
			if(_direction == 3) x--;

			Entity a = _board.getEntity(x, y, null);

//			if(a instanceof Bomber) ++radius; //explosion has to be below the mob
//			if(a instanceof Enemy) ++radius;

			if(a.collide(this) == false) //cannot pass thru
				break;

			++radius;
		}
		return radius;
	}

	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {}

	@Override
	public void render(Screen screen) {
		for (int i = 0; i < _flameSegments.length; i++) {
			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: x? l? va ch?m v?i Bomber, Enemy. Chú ? ð?i tý?ng này có v? trí chính là v? trí c?a Bomb ð? n?
		if(e instanceof Character) {
			((Character) e).kill();
		}
		return true;
	}
}