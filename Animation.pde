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

    void update(float elapsedTime){
        _time += elapsedTime;

        if(_time >= FRAME_TIME){
            _currentFrame = (_currentFrame + 1) % totalFrames;
            _time -= FRAME_TIME;
        }
    }
    // resize width and height
    void display(){
//        noSmooth();
         image(images[_currentFrame], 0, 0);
    }
  
}
