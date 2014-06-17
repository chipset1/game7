import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class game extends PApplet {

/* @pjs preload="Link_ALttP.png, Mario - Swim1.gif, Mario - Swim2.gif, Mario - Swim3.gif, Mario - Swim4.gif, invader1.png, invader2.png, zeldaSwim.png"; 
 */
ArrayList<Entity> entities = new ArrayList<Entity>();
ArrayList<Arrow> arrows = new ArrayList<Arrow>();
ArrayList<Enemy> enemies = new ArrayList<Enemy>();

EnemySpawner enemySpawner;

PVector gravity = new PVector(0, 0.5f);

final int game_width = 1000;
final int game_height = 650;

Ground ground;

Player player;

int killCount = 0; 

public void setup() {
	size(1000, 650);

	ground = new Ground(new PVector(0, game_height -100), new PVector(game_width, game_height -100));

	enemySpawner = new EnemySpawner();
	player = new Player();

//	soundManager = new SoundManager(this);
	// soundManager.addSound("data/audio/arrow_hit.mp3");
        smooth();
}


public void draw() {
	background(255);



	fill(0);
	if(killCount >= 15){
		text("YAY YOU WIN", game_width/2, game_height/2 );
	}
	text("Enemies killed: "+ killCount, 10, 40);
	text(frameRate, 10, 20);

	player.display();
	player.update();

	ground.display();

	enemySpawner.update();

	gameUpdate();

}

public void jsGameUpdate(){
	for(int i = 0; i < entities.size(); i++){
    	Entity e = entities.get(i);
    	e.display();
	    if(e.isDead) continue;
    	e.update();
    	groundCol(e, e.width, 0, random(3, 30));  
    }
    for(int i = 0; i < arrows.size(); i++){
    	Arrow a = arrows.get(i);
    	a.display();
	    if(a.isDead) continue;
	    a.update();
    	arrowToEnemyCol(a);
    	if(a.stickPos == null) groundCol(a, 0, 0, random( 0, a.width/2));
    }
    for(int i =0; i < enemies.size(); i++){
	    Enemy e = enemies.get(i);
	    e.display();
	    // if(mouseToRectCol(e)) println("ok");//ellipse(mouseX, mouseY, 2, 2);
	    if(e.isDead) continue;
	    e.update();  
	    groundCol(e, 0,0, random(-e.height/2, e.height));    	
    }
}

public void gameUpdate(){
   for(Entity e: entities){
   		e.display();
	    if(e.isDead) continue;
    	e.update();
    	groundCol(e, e.width, 0, random(3, 30)); 
  }

  for(Arrow a : arrows){
    a.display();
    if(a.isDead) continue;
    a.update();
    arrowToEnemyCol(a);
    if(a.stickPos == null) groundCol(a, 0, 0, random( 0, a.width/2));
  }

  for (Enemy e : enemies) {
  	if(!e.isOnScreen()) continue;
    e.display();
    // if(mouseToRectCol(e)) println("ok");//ellipse(mouseX, mouseY, 2, 2);
    if(e.isDead) continue;
    e.update();  
    groundCol(e, 0,0, random(-e.height/2, e.height));
  } 
  
}

public void groundCol(Entity e, float scaleW, float offsetX, float offsetY){
		PVector norm = scalarProjection(e.position, ground.start, ground.end);
			if(e.isDead) return;
	  		if(ground.checkHit(e.position)){
	  		// scale width to look like it splats on the ground
	  		e.width += scaleW; 
			// set pos to where it hit the ground and offset to avoid stacking
	  		e.position.set(PVector.add(norm, new PVector(offsetX, offsetY))); 
	  		e.isDead = true;
	  	}
}

public void arrowToEnemyCol(Arrow a){
	PVector offset = new PVector();
	for (Enemy e : enemies) {
		if(e.isHit) continue;
		// if(mouseToRectCol(a.position.x, a.position.y, e)){
		if(e.rectToPointCol(a)){
			e.hitPos = a.position;
			// // e.bleedInterval =(int) map(a.velocity.mag(), 0,25,10, 50);
			offset = PVector.sub(a.position, e.position);
			a.stickTo(e, offset);
			// // bump up ememy when hit
			e.velocity.add(PVector.mult(a.velocity, 0.8f));
			e.isHit = true;
			
			killCount++;
			// 
			enemySpawner.enemy_speed += map(killCount, 0, 20, 0, 0.5f);
			// println(killCount);
		}
	}		
}

public boolean mouseToRectCol(float x ,float y, Entity e){
		PVector pos = e.position;
		float w = e.width;
		float h = e.height;
		// rect(pos.x - w, pos.y, w, h);
		return x > (pos.x - w) && x  < pos.x && y > pos.y && y  < pos.y + h;
}

public PVector scalarProjection(PVector p, PVector a, PVector b) {
// for Nature of Code Simple scalar projection example
  PVector ap = PVector.sub(p, a);
  PVector ab = PVector.sub(b, a);
  ab.normalize(); // Normalize the line
  ab.mult(ap.dot(ab));
  PVector normalPoint = PVector.add(a, ab);
  return normalPoint;
}

public void addEntity(Entity e){
	entities.add(e);
}

public void mousePressed(){
	player.mousePressed();
}

public void mouseDragged(){
	player.mouseDragged();
}

public void mouseReleased() {
	player.mouseReleased();
}

// Class for animating a sequence of GIFs
class Animation {
    float FRAME_TIME = 1/24f;
    float _time = 0;

    // for sprite sheet
    int framesPerRow;
    int startX = 0;
    int startY = 0;

    int totalFrames;
    int _currentFrame = 0;
    
    int w, h;

    PImage[] images;

    Animation(PImage img,int w, int h,int framesPerRow,int totalFrames){
       // this.img = img;
        this.w = w;
        this.h = h;
        this.framesPerRow = framesPerRow;
        this.totalFrames = totalFrames;
        images = new PImage[totalFrames];
        for (int i = 0; i < images.length; ++i) {
            int x =  i % framesPerRow * w;
            int y = floor(  i / framesPerRow) * h;
            images[i] = img.get(x + startX, y + startY + (8 * i), w, h);   
        }
    }

    Animation(PImage[] images){
    	this.images = images; 
    	totalFrames = images.length;
    }

    public void update(float elapsedTime){
        _time += elapsedTime;

        if(_time >= FRAME_TIME){
            _currentFrame = (_currentFrame + 1) % totalFrames;
            _time -= FRAME_TIME;
        }
    }
    // resize width and height
    public void display(){
//        noSmooth();
         image(images[_currentFrame], 0, 0);
    }
  
}
class Arrow extends Entity{

	Entity stickPos;
	PVector stickOff;
	Arrow(PVector pos, PVector vel){
		position = pos.get();
		velocity = vel;
		setSize(40, 0);
	}

	public void display(){
		stroke(0);
		pushMatrix();
		translate(position.x, position.y);
		rotate(velocity.heading2D());
		ellipse(0,0,2,2);
		line( -width, 0, 0, 0);
		popMatrix();
	}

	public void update(){
		if(stickPos != null){
			PVector t = PVector.add(stickPos.position, stickOff);
			position.set(t);
			position.add(velocity);
			return;
		}
		velocity.add(gravity);
		velocity.limit(25);
		position.add(velocity);
	}

	public void stickTo(Entity e, PVector o){
		stickOff = o;
		stickPos = e;

	}

	// void checkHit(PVector pos, float w, float h){
	// 	if(position.x > pos.x && position.x  < pos.x + w && position.y > pos.y && position.y  < pos.y + ){
	// 		println("hit");
	// 	}
	// }	

	

}
class Blood extends Entity{
	int c;

	Blood(PVector pos,int bloodColor){
		position = pos.get();
		velocity = new PVector(random(-2,2), random(-10, 0));
		float size = random(2, 4);
		setSize(size, size);
		c = bloodColor;
	}

	public void display(){
		noStroke();
		fill(c);
		ellipse(position.x, position.y, width, height);
	}

	public void update(){
		if(isDead) return;
		velocity.add(gravity);
		position.add(velocity);
	}
}
class Enemy extends Entity{

	PVector hitPos;
	boolean isHit = false;
	Timer bloodTimer;
	int bleedInterval = 100;

	int bleedColor;
	
	Enemy(){
		// position = new PVector(game_width /2, game_height/2);
		// velocity = new PVector(-1, 0);
		bloodTimer = new Timer(bleedInterval,0);
		// setSize(30,10);
	}

	public void display(){
	
	}

	public void update(){ 
		if(keyPressed) isHit = false;
		if(isHit){
			velocity.add(gravity);
			bleed();
		}
		position.add(velocity);
	}

	public void bleed(){
		if(bloodTimer.canRun())	addEntity(new Blood(hitPos, bleedColor));
	}

	public void reset(){
		// position.x = -10;
		// position.y = random(20, height - 100);
	}

}
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

	public void update(){
		if(spawnTimer.canRun() && enemiesOnScreen() < 5){
			addEnemy();
		}
	}

	public int enemiesOnScreen(){
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

	public void addEnemy(){
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

	public void loadImages(){
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

	public void addInvader(float x, float y, float speed){
		Enemy e = new SpriteEnemy(invaderImages, 1/240f);
		e.position = new PVector(x, y);
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(0xff89A6DB);
		enemies.add(e);		
	}

	public void addFlipInvader(float x, float y, float speed){
		Enemy e = new SpriteEnemy(invaderImages, 1/240f);
		e.position = new PVector(x, y);
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(0xff89A6DB);
		e.filpHorizontal();
		enemies.add(e);		
	}

	public void addMario(float x, float y,float speed){
		Enemy e = new SpriteEnemy(marioImages, 1/120f);
		e.position = new PVector(x, y);
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(200,0,0);
		enemies.add(e);		
	}

	public void addFlipMario(float x, float y,float speed){
		Enemy e = new SpriteEnemy(marioImages, 1/120f);
		e.position = new PVector(x, y);
		// speed should be negative
		e.velocity = new PVector(speed,0);
		e.bleedColor = color(200,0,0);
		e.filpHorizontal();
		enemies.add(e);	
	}

}
abstract class Entity{
	PVector position, velocity;

	float width = -1;
	float height = -1;
	boolean isDead = false;

	// draw horizontall flipped?
	boolean hflip = false;

	public abstract void display();
	public abstract void update();

	public void setSize(float w, float h){
		width = w;
		height = h;
	}

	public boolean pointToRectCol(Entity e){
		// var for rect 
		PVector pos = e.position;
		float w = e.width;
		float h = e.height;

		return position.x > pos.x && position.x  < pos.x + w && position.y > pos.y && position.y  < pos.y + h;
	}

	public boolean rectToPointCol(Entity e){
		// arrow position 
		PVector pos = e.position;

		float w = width;
		float h = height;
		if(hflip){
			return pos.x > (position.x - w) && pos.x  < position.x && pos.y > position.y && pos.y  < position.y + h;
		}
		return pos.x > position.x && pos.x  < position.x + w && pos.y > position.y && pos.y  < position.y + h;
	}	

	public void filpHorizontal(){
		hflip = !hflip;
	}


	public boolean isOnScreen(){ 
	    float buffer = width;
	    if(position.x < game_width + buffer && position.x > - buffer && position.y < game_height + buffer && position.y > - buffer) return true;
	    return false;
	}

}
class Ground{
	PVector start;
	PVector end; 
	Ground(PVector start, PVector end){
		this.start = start;
		this.end = end;
	}

	public void display(){
		stroke(0);
		line(start.x, start.y, end.x, end.y);
	}
	//checkHit(e.position, ground.start, game_width, 150 )
	//(PVector target, PVector pos, float w, float h)
	public boolean checkHit(PVector target){
		return target.x > start.x && target.x  < start.x + end.x && target.y > start.y && target.y  < start.y + end.y;
	}
}
class Player extends Entity {
	PVector pMouse;
	PVector mouse;

	boolean drag = false;
	float angle = 0;
	float arrowForce = 0;

	Player(){
		position = new PVector(game_width/2, game_height-125);	
		pMouse = new PVector();
		mouse = new PVector();
		setSize(40,40);
	}


	public void display(){
		angle = PVector.sub( pMouse, mouse).heading2D();
		// fill(0,0,200);
		fill(255);
                stroke(0);
		pushMatrix();
		translate(position.x, position.y);
		rotate(angle);
		ellipse(0, 0, width, height);;
		line(0,0, width/2,0);
		popMatrix();
	}

	public void update(){
		if(drag){
			stroke(0);
			drawStats();
			line(pMouse.x, pMouse.y, mouse.x, mouse.y);
		} 
	}

	public void drawStats(){
		fill(0);
		float af = PVector.sub(mouse, pMouse).mag(); 
		arrowForce = map(af, 0, 25, 0, 100);
		text((int)degrees(angle * -1) + " \u00b0", pMouse.x, pMouse.y);
		text((int)arrowForce, mouse.x, mouse.y);	
	}

	public void fire(PVector vel){
		arrows.add(new Arrow(position, vel));
	}

	public void mousePressed(){
		pMouse.x = mouseX;
		pMouse.y = mouseY;
	}

	public void mouseDragged(){
		drag = true;
		mouse.x = mouseX;
		mouse.y = mouseY;
	}

	public void mouseReleased() {
		drag = false;
		PVector vel = PVector.sub( pMouse, mouse);
		vel.mult(0.05f);
		fire(vel);
	}

}
class SpriteEnemy extends Enemy{
	Animation spriteAni;
	// how fast the sprite change to next sprite
	float speed;
	float rScale = (int) 1.5f;

	float angle = 0;
	float inc = TWO_PI/25.0f;

	float sinRange = 0.01f;

	SpriteEnemy(PImage[] images, float speed){
		super();
		setSize(images[0].width * rScale, images[0].height * rScale);
		this.speed = speed;
		spriteAni = new Animation(images);
		sinRange = random(0.01f, 0.03f);
	}

	public void display(){
		noFill();
		pushMatrix();
		translate(position.x, position.y );
		if(hflip) scale(-1, 1);	
//                rect(0,0,width,height);
		spriteAni.display();
		popMatrix();
	}
	public void update(){
		super.update();
		float o = map(sin(angle), -1, 1, -sinRange, sinRange);
		velocity.y += o;
		if(isHit) speed *= 2;
		spriteAni.update(speed);
		angle += inc;
	}

}
//HTIMER 
class Timer {
	private int lastInterval, interval, cycleCounter, numCycles;
	private boolean usesFrames = false;

	public Timer(int timerInterval, int numberOfCycles) {
		interval = timerInterval;
		numCycles = numberOfCycles;
	}
	
	public void useFrames() {
		usesFrames = true;
	}

	// find better name
	public boolean canRun() {
		int curr = (usesFrames)? frameCount : millis();
		if(lastInterval < 0) lastInterval = curr;
		if(curr-lastInterval >= interval) {
			lastInterval = curr;
			if(numCycles > 0 && ++cycleCounter >= numCycles) stop();
			return true;
		}
		return false;
	}

	public void stop() {
		numCycles = 0;
		lastInterval = -1;
	}

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
