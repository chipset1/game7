/* @pjs preload="Link_ALttP.png, Mario - Swim1.gif, Mario - Swim2.gif, Mario - Swim3.gif, Mario - Swim4.gif, invader1.png, invader2.png, zeldaSwim.png"; 
 */
ArrayList<Entity> entities = new ArrayList<Entity>();
ArrayList<Arrow> arrows = new ArrayList<Arrow>();
ArrayList<Enemy> enemies = new ArrayList<Enemy>();

EnemySpawner enemySpawner;

PVector gravity = new PVector(0, 0.5);

final int game_width = 1000;
final int game_height = 650;

Ground ground;

Player player;

int killCount = 0; 

void setup() {
	size(1000, 650);

	ground = new Ground(new PVector(0, game_height -100), new PVector(game_width, game_height -100));

	enemySpawner = new EnemySpawner();
	player = new Player();

//	soundManager = new SoundManager(this);
	// soundManager.addSound("data/audio/arrow_hit.mp3");
        smooth();
}


void draw() {
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

void jsGameUpdate(){
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

void gameUpdate(){
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

void groundCol(Entity e, float scaleW, float offsetX, float offsetY){
		PVector norm = scalarProjection(e.position, ground.start, ground.end);
			if(e.isDead || !e.isOnScreen()) return;
	  		if(ground.checkHit(e.position)){
	  		// scale width to look like it splats on the ground
	  		e.width += scaleW; 
			// set pos to where it hit the ground and offset to avoid stacking
	  		e.position.set(PVector.add(norm, new PVector(offsetX, offsetY))); 
	  		e.isDead = true;
	  	}
}

void arrowToEnemyCol(Arrow a){
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
			e.velocity.add(PVector.mult(a.velocity, 0.8));
			e.isHit = true;
			
			killCount++;
			// 
			enemySpawner.enemy_speed += map(killCount, 0, 20, 0, 0.5);
			// println(killCount);
		}
	}		
}

boolean mouseToRectCol(float x ,float y, Entity e){
		PVector pos = e.position;
		float w = e.width;
		float h = e.height;
		// rect(pos.x - w, pos.y, w, h);
		return x > (pos.x - w) && x  < pos.x && y > pos.y && y  < pos.y + h;
}

PVector scalarProjection(PVector p, PVector a, PVector b) {
// for Nature of Code Simple scalar projection example
  PVector ap = PVector.sub(p, a);
  PVector ab = PVector.sub(b, a);
  ab.normalize(); // Normalize the line
  ab.mult(ap.dot(ab));
  PVector normalPoint = PVector.add(a, ab);
  return normalPoint;
}

void addEntity(Entity e){
	entities.add(e);
}

void mousePressed(){
	player.mousePressed();
}

void mouseDragged(){
	player.mouseDragged();
}

void mouseReleased() {
	player.mouseReleased();
}

