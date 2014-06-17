class Enemy extends Entity{

	PVector hitPos;
	boolean isHit = false;
	Timer bloodTimer;
	int bleedInterval = 100;

	color bleedColor;
	
	Enemy(){
		// position = new PVector(game_width /2, game_height/2);
		// velocity = new PVector(-1, 0);
		bloodTimer = new Timer(bleedInterval,0);
		// setSize(30,10);
	}

	void display(){
	
	}

	void update(){ 
		if(keyPressed) isHit = false;
		if(isHit){
			velocity.add(gravity);
			bleed();
		}
		position.add(velocity);
	}

	void bleed(){
		if(bloodTimer.canRun())	addEntity(new Blood(hitPos, bleedColor));
	}

	void reset(){
		// position.x = -10;
		// position.y = random(20, height - 100);
	}

}
