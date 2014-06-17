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


	void display(){
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

	void update(){
		if(drag){
			stroke(0);
			drawStats();
			line(pMouse.x, pMouse.y, mouse.x, mouse.y);
		} 
	}

	void drawStats(){
		fill(0);
		float af = PVector.sub(mouse, pMouse).mag(); 
		arrowForce = map(af, 0, 25, 0, 100);
		text((int)degrees(angle * -1) + " °", pMouse.x, pMouse.y);
		text((int)arrowForce, mouse.x, mouse.y);	
	}

	void fire(PVector vel){
		arrows.add(new Arrow(position, vel));
	}

	void mousePressed(){
		pMouse.x = mouseX;
		pMouse.y = mouseY;
	}

	void mouseDragged(){
		drag = true;
		mouse.x = mouseX;
		mouse.y = mouseY;
	}

	void mouseReleased() {
		drag = false;
		PVector vel = PVector.sub( pMouse, mouse);
		vel.mult(0.05);
		fire(vel);
	}

}
