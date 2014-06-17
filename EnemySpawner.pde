class EnemySpawner {
	int maxEnemies;
	Timer spawnTimer;

	int activeLimit = 10;

	PImage[] invaderImages;
	PImage[] marioImages;
	PImage zeldaSwim1;

	float enemy_speed = 1;

	// spawn points for
	int enemy_num =2;

	EnemySpawner(){
		spawnTimer = new Timer(2000, 0);
		loadImages();
	}

	void update(){
		if(spawnTimer.canRun() && enemiesOnScreen() < 5){
			addEnemy();
		}
	}

	int enemiesOnScreen(){
		int count = 0;
		for(Enemy e: enemies){
			if(e.isDead) continue;
			if(e.isOnScreen()){
				count++;
			}
		}
		fill(0);
		return count;
	}

	void addEnemy(){
		// left side spawn point
		PVector leftSide = new PVector(game_width+ 10, random(game_height / 2));
		// right side spawn point
		PVector rightSide = new PVector(-10, random(0, game_height/2));

		switch ((int)random(0, enemy_num * 2)) {
			case 1 :
				addFlipMario(leftSide.x, leftSide.y, -enemy_speed);
				break;		
			case 2:
				addMario(rightSide.x, rightSide.y , enemy_speed);
				break;
			case 3:
				addFlipInvader(leftSide.x, leftSide.y, -enemy_speed);
				break;
			case 4:
				addInvader(leftSide.x, leftSide.y, enemy_speed);
				break;
		}
	}

	void loadImages(){
		invaderImages = new PImage[2];
		invaderImages[0] = loadImage("invader1.png");	
		invaderImages[1] = loadImage("invader2.png");	
		
		//http://www.videogamesprites.net/SuperMarioBros1/Characters/Mario/
		marioImages = new PImage[4];
		marioImages[0] = loadImage("Mario - Swim1.gif");
		marioImages[1] = loadImage("Mario - Swim2.gif");
		marioImages[2] = loadImage("Mario - Swim3.gif");
		marioImages[3] = loadImage("Mario - Swim4.gif");
	}

	void addInvader(float x, float y, float speed){
		Enemy e = new SpriteEnemy(invaderImages, 1/240f);
		e.position = new PVector(x, y);
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(#89A6DB);
		enemies.add(e);		
	}

	void addFlipInvader(float x, float y, float speed){
		Enemy e = new SpriteEnemy(invaderImages, 1/240f);
		e.position = new PVector(x, y);
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(#89A6DB);
		e.filpHorizontal();
		enemies.add(e);		
	}

	void addMario(float x, float y,float speed){
		Enemy e = new SpriteEnemy(marioImages, 1/120f);
		e.position = new PVector(x, y);
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(200,0,0);
		enemies.add(e);		
	}

	void addFlipMario(float x, float y,float speed){
		Enemy e = new SpriteEnemy(marioImages, 1/120f);
		e.position = new PVector(x, y);
		// speed should be negative
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(200,0,0);
		e.filpHorizontal();
		enemies.add(e);	
	}

}
