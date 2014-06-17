class Blood extends Entity{
	color c;

	Blood(PVector pos,color bloodColor){
		position = pos.get();
		velocity = new PVector(random(-2,2), random(-10, 0));
		float size = random(2, 4);
		setSize(size, size);
		c = bloodColor;
	}

	void display(){
		noStroke();
		fill(c);
		ellipse(position.x, position.y, width, height);
	}

	void update(){
		if(isDead) return;
		velocity.add(gravity);
		position.add(velocity);
	}
}
