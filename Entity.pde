abstract class Entity{
	PVector position, velocity;

	float width = -1;
	float height = -1;
	boolean isDead = false;

	// draw horizontall flipped?
	boolean hflip = false;

	abstract void display();
	abstract void update();

	void setSize(float w, float h){
		width = w;
		height = h;
	}

	boolean pointToRectCol(Entity e){
		// var for rect 
		PVector pos = e.position;
		float w = e.width;
		float h = e.height;

		return position.x > pos.x && position.x  < pos.x + w && position.y > pos.y && position.y  < pos.y + h;
	}

	boolean rectToPointCol(Entity e){
		// arrow position 
		PVector pos = e.position;

		float w = width;
		float h = height;
		if(hflip){
			return pos.x > (position.x - w) && pos.x  < position.x && pos.y > position.y && pos.y  < position.y + h;
		}
		return pos.x > position.x && pos.x  < position.x + w && pos.y > position.y && pos.y  < position.y + h;
	}	

	void filpHorizontal(){
		hflip = !hflip;
	}


	boolean isOnScreen(){ 
	    float buffer = width;
	    if(position.x < game_width + buffer && position.x > - buffer && position.y < game_height + buffer && position.y > - buffer) return true;
	    return false;
	}

}
