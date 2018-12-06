package uet.oop.bomberman.level;



import java.io.BufferedReader;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.URL;

import java.nio.file.Files;

import java.util.StringTokenizer;
import java.util.logging.Level;

import java.util.logging.Logger;

import uet.oop.bomberman.Board;

import uet.oop.bomberman.Game;

import uet.oop.bomberman.entities.LayeredEntity;

import uet.oop.bomberman.entities.character.Bomber;

import uet.oop.bomberman.entities.character.enemy.Balloon;

import uet.oop.bomberman.entities.tile.Grass;

import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;

import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;

import uet.oop.bomberman.exceptions.LoadLevelException;

import uet.oop.bomberman.graphics.Screen;

import uet.oop.bomberman.graphics.Sprite;



import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FileLevelLoader extends LevelLoader {



	/**

	 * Ma tráº­n chá»©a thÃ´ng tin báº£n Ä‘á»“, má»—i pháº§n tá»­ lÆ°u giÃ¡

	 * trá»‹ kÃ­ tá»± Ä‘á»?c Ä‘Æ°á»£c tá»« ma tráº­n báº£n Ä‘á»“ trong tá»‡p

	 * cáº¥u hÃ¬nh

	 */

	private static char[][] _map;

	private static int[] x;



	public FileLevelLoader(Board board, int level) throws LoadLevelException {

		super(board, level);

	}



	@Override



	public void loadLevel(int level) throws LoadLevelException {

		// System.getProperty("user.home");

		ClassLoader c = ClassLoader.getSystemClassLoader();

		File file = new File(c.getResource("levels/Level1.txt").getFile());

		FileInputStream input;

		BufferedReader reader = null;

		try {

			input = new FileInputStream(file);

			reader = new BufferedReader(new InputStreamReader(input));



		} catch (FileNotFoundException ex) {

			Logger.getLogger(FileLevelLoader.class.getName()).log(Level.SEVERE, null, ex);

		}
		try{
			String line = reader.readLine();
			StringTokenizer tokens = new StringTokenizer(line);
			_level = Integer.parseInt(tokens.nextToken());
			_height = Integer.parseInt(tokens.nextToken());
			_width = Integer.parseInt(tokens.nextToken());
			char[][]d = new char[_height][_width];
			for(int i=0;i<_height;i++){
				String l = reader.readLine();
				for(int j=0;j<_width;j++){
					d[i][j] = l.charAt(j);
				}
			}
			_map = new char[_height][_width];
			for(int i=0;i<_height;i++){
				for(int j=0;j<_width;j++){
					_map[i][j] = d[i][j];
				}
			}
		}
		catch(IOException e){
			throw new LoadLevelException("dfdg"+e);
		}

	}



	@Override

	public void createEntities() {

		// TODO: táº¡o cÃ¡c Entity cá»§a mÃ n chÆ¡i

		// TODO: sau khi táº¡o xong, gá»?i _board.addEntity() Ä‘á»ƒ thÃªm Entity vÃ o game



		// TODO: pháº§n code máº«u á»Ÿ dÆ°á»›i Ä‘á»ƒ hÆ°á»›ng dáº«n cÃ¡ch thÃªm cÃ¡c loáº¡i Entity vÃ o game

		// TODO: hÃ£y xÃ³a nÃ³ khi hoÃ n thÃ nh chá»©c nÄƒng load mÃ n chÆ¡i tá»« tá»‡p cáº¥u hÃ¬nh
			for(int i=0;i<_height;i++)
				for (int j = 0; j < _width; j++) {
					int pos = j + i * _width;
					if (_map[i][j] == '#') {
						_board.addEntity(pos, new Wall(j, i, Sprite.wall));
					} else if (_map[i][j] == '*') {
						_board.addEntity(pos, new LayeredEntity(j, i,
								new Grass(j, i, Sprite.grass),
								new Brick(j, i, Sprite.brick)
						));
					} else if (_map[i][j] == 'x') {
						LayeredEntity layer = new LayeredEntity(j, i,
								new Grass(j, i, Sprite.grass),
								new Brick(j, i, Sprite.brick));
						layer.addBeforeTop(new Portal(j,i,Sprite.portal,_board));
						_board.addEntity(pos, layer);

					}
					else if(_map[i][j]=='p'){
						_board.addCharacter(new Bomber(Coordinates.tileToPixel(i),Coordinates.tileToPixel(j)+Game.TILES_SIZE,_board));
						Screen.setOffset(0,0);
						_board.addEntity(pos, new Grass(j,i,Sprite.grass));
					}
					//them enemys
					else if(_map[i][j]=='1'){
						_board.addCharacter(new Balloon(Coordinates.tileToPixel(j),Coordinates.tileToPixel(i)+ Game.TILES_SIZE,_board));
						_board.addEntity(pos, new Grass(j,i,Sprite.grass));
					}
					//them powerups
					else if(_map[i][j]=='b'){
						LayeredEntity layer = new LayeredEntity(j,i,new Grass(j,i,Sprite.grass),
								new Brick(j,i,Sprite.brick));

					}
					else if(_map[i][j]=='f'){
						LayeredEntity layer = new LayeredEntity(j,i,
								new Grass(j,i,Sprite.grass),
								new Brick(j,i,Sprite.brick));
						layer.addBeforeTop(new FlameItem(j,i, Sprite.powerup_flames));
						_board.addEntity(pos, layer);
					}
					else if(_map[i][j]=='s'){
						LayeredEntity layer = new LayeredEntity(j,i,
								new Grass(j,i, Sprite.grass),
								new Brick(j,i, Sprite.portal));
						layer.addBeforeTop(new SpeedItem(j,i, Sprite.powerup_speed));
						_board.addEntity(pos, layer);
					}
					else
						_board.addEntity(pos, new Grass(j,i,Sprite.grass));


				}
		// thÃªm Wall


        /*

       for (int x = 0; x < 20; x++) {

            for (int y = 0; y < 20; y++) {

                int pos = x + y * _width;

                Sprite sprite = y == 0 || x == 0 || x == 10 || y == 10 ? Sprite.wall : Sprite.grass;

                _board.addEntity(pos, new Grass(x, y, sprite));

            }

        }

         */

		// thÃªm Bomber

        /*

        int xBomber = 1, yBomber = 1;

        _board.addCharacter(new Bomber(Coordinates.tileToPixel(xBomber), Coordinates.tileToPixel(yBomber) + Game.TILES_SIZE, _board));

        Screen.setOffset(0, 0);

        _board.addEntity(xBomber + yBomber * _width, new Grass(xBomber, yBomber, Sprite.grass));



        // thÃªm Enemy

        int xE = 2, yE = 1;

        _board.addCharacter(new Balloon(Coordinates.tileToPixel(xE), Coordinates.tileToPixel(yE) + Game.TILES_SIZE, _board));

        _board.addEntity(xE + yE * _width, new Grass(xE, yE, Sprite.grass));



        // thÃªm Brick

        int xB = 3, yB = 1;

        _board.addEntity(xB + yB * _width,

                new LayeredEntity(xB, yB,

                        new Grass(xB, yB, Sprite.grass),

                        new Brick(xB, yB, Sprite.brick)

                )

        );



        // thÃªm Item kÃ¨m Brick che phá»§ á»Ÿ trÃªn

        int xI = 1, yI = 2;

        _board.addEntity(xI + yI * _width,

                new LayeredEntity(xI, yI,

                        new Grass(xI, yI, Sprite.grass),

                        new SpeedItem(xI, yI, Sprite.powerup_flames),

                        new Brick(xI, yI, Sprite.brick)

                )

        );

         */

	}




}