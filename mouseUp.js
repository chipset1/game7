var mySketchInstance;

// called once the page has fully loaded
window.onload = function () {
    getSketchInstance();
};

// this is called (repeatedly) to find the sketch
function getSketchInstance() {
    var s = Processing.getInstanceById(getProcessingSketchId());
    if ( s === undefined ) {
        setTimeout(getSketchInstance, 200); // try again a bit later
        
    } else {
        mySketchInstance = s;
        //monitorSelection();
    }
}

//window.onmouseup = function(){
//	mySketchInstance.MouseReleased();
////        console.log("mouseup");
//};
//
//window.onmousemove = function(){
//	mySketchInstance.MousePressed();
//};
//
//window.onmousedown = function(){
//	mySketchInstance.MouseDragged();
//};
