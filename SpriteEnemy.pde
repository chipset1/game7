class SpriteEnemy extends Enemy{
	Animation spriteAni;
	// how fast the sprite change to next sprite
	float speed;
	float rScale = (int) 1.5;

	float angle = 0;
	float inc = TWO_PI/25.0;

	float sinRange = 0.01;

	SpriteEnemy(PImage[] images, float speed){
		super();
		setSize(images[0].width * rScale, images[0].height * rScale);
		this.speed = speed;
		spriteAni = new Animation(images);
		sinRange = random(0.01, 0.03);
	}

	void display(){
		noFill();
		pushMatrix();
		translate(position.x, position.y );
		if(hflip) scale(-1, 1);	
//                rect(0,0,width,height);
		spriteAni.display();
		popMatrix();
	}
	void update(){
		super.update();
		float o = map(sin(angle), -1, 1, -sinRange, sinRange);
		velocity.y += o;
		if(isHit) speed *= 2;
		spriteAni.update(speed);
		angle += inc;
	}

}
