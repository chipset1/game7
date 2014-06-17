class Ground{
	PVector start;
	PVector end; 
	Ground(PVector start, PVector end){
		this.start = start;
		this.end = end;
	}

	void display(){
		stroke(0);
		line(start.x, start.y, end.x, end.y);
	}
	//checkHit(e.position, ground.start, game_width, 150 )
	//(PVector target, PVector pos, float w, float h)
	boolean checkHit(PVector target){
		return target.x > start.x && target.x  < start.x + end.x && target.y > start.y && target.y  < start.y + end.y;
	}
}
