class Arrow extends Entity{

	Entity stickPos;
	PVector stickOff;
	Arrow(PVector pos, PVector vel){
		position = pos.get();
		velocity = vel;
		setSize(40, 0);
	}

	void display(){
		stroke(0);
		pushMatrix();
		translate(position.x, position.y);
		rotate(velocity.heading2D());
		ellipse(0,0,2,2);
		line( -width, 0, 0, 0);
		popMatrix();
	}

	void update(){
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

	void stickTo(Entity e, PVector o){
		stickOff = o;
		stickPos = e;

	}

	// void checkHit(PVector pos, float w, float h){
	// 	if(position.x > pos.x && position.x  < pos.x + w && position.y > pos.y && position.y  < pos.y + ){
	// 		println("hit");
	// 	}
	// }	

	

}
