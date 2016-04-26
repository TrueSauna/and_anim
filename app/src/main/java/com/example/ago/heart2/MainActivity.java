package com.example.ago.heart2;

import android.app.ActionBar;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //TESTI 123

    //testikommentti

    //testikeisari

    //movable image itself and it's animationdrawable to contain image's animations
    public ImageView _img;
    public ImageView _grid;
    public AnimationDrawable _adimgDwarf;
    public ImageZ[] ImagesWithZ;

    //FUTURE NOTIFICATION: USE ARRAYLISTS, THEY'RE DYNAMIC, BASIC ARRAYS AREN'T
    public ImageZ currentImageZ = new ImageZ();
    public ImageZ[] oldImageZArray = new ImageZ[50];


    /*

    needed for image's shadow->
    1. create layout for the image (Not a new layout. In this case, the layout exists already in content_main.xml; id=root)
    2. create parameters for the shadow-image (LayoutParams, equals parameters in xml, for example, android:layout_width="wrap_content")
    3. edit parameters
    4. create image and set its resource (the graphic itself)
    5. add LayoutParameters to image
    6. add image to layout
    ps. not needed to attach layout to parent, because it already exists in content_main.xml
    */


    //attributes of the shadowy copy of the movable image
    public ImageView _imgShadow;
    public ImageView _imgShadow2;
    public RelativeLayout _rootLayout;
    public RelativeLayout.LayoutParams _imgParamsShadow;
    public RelativeLayout.LayoutParams _imgParamsShadow2;

    PointF _pointA = new PointF(200, 400);
    PointF _pointB = new PointF(300, 400);

    public PointF[] pointArray = {_pointA, _pointB};


    public class LineInfo
    {
        int startx;
        int starty;
        int endx;
        int endy;

        public LineInfo (int sx, int sy ,int ex, int ey){
            startx = sx;
            starty = sy;
            endx = ex;
            endy = ey;
        }
    }

    public class Coordinate {
        int X;
        int Y;

        public Coordinate(int x, int y) {
            X = x;
            Y = y;
        }
    }

    public class Grid
    {
        ArrayList<LineInfo> gridLines;
        ArrayList<Coordinate> midPoints;

        public Grid() {
        }

        public Grid(ArrayList<LineInfo> gridLines, ArrayList<Coordinate> midPoints) {
            this.gridLines = gridLines;
            this.midPoints = midPoints;
        }
    }

    public Grid CreateGrid(int startx, int starty, int height, int width, int size)
    {
        ArrayList<LineInfo> alLineInfo = new ArrayList<LineInfo>();
        ArrayList<Coordinate> midPoints = new ArrayList<Coordinate>();

        int h = startx;
        //int v = height;
        int verticalLineLength = height/size * size + starty;

        int numberOfVerticalLines = width/size;

        //int a = Math.round((double)height/(float)size);


        for (int i = 0; i < numberOfVerticalLines+1; i++)
        {
            alLineInfo.add(new LineInfo(h,starty,h,verticalLineLength));
            h += size;
        }

        int horizontalLineLength = width/size * size + startx;
        int v = starty;

        int numberOfHorizontalLines = height/size;

        for (int i = 0; i < numberOfHorizontalLines+1; i++)
        {
            alLineInfo.add(new LineInfo(startx,v, horizontalLineLength,v));
            v += size;
        }


        int half = size/2;

        for (int i = 1; i < numberOfVerticalLines * 2; i += 2)
        {
            for (int j = 1; j < numberOfHorizontalLines * 2; j += 2)
            {
                int a = startx + half * i;
                int b = starty + half * j;

                midPoints.add(new Coordinate(a, b));
            }
        }


        return new Grid(alLineInfo, midPoints);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int statusHeight = getStatusBarHeight();
        int VisibleScreenHeight = height - statusHeight;



        //Draw grid
        _grid = (ImageView) this.findViewById(R.id._imgGrid);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        _grid.setImageBitmap(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);

        //Create grid
        Grid grid = CreateGrid(100, 200, 1500, 900, 150);


        //Paint grid lines
        for(LineInfo li: grid.gridLines) {
            canvas.drawLine(li.startx, li.starty, li.endx, li.endy, paint);
        }

        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);

        //Paint grid midpoints
        for(Coordinate co: grid.midPoints) {
            canvas.drawPoint(co.X, co.Y, paint);
        }


        //_img.setTranslationZ(1.1f);




        //STARTS HERE:---------------------

        _rootLayout = (RelativeLayout)findViewById(R.id._root);

        //movable image
        _img = (ImageView) findViewById(R.id._imgDwarf);
        _img.setTag(R.drawable.dwarf_1);
        //works fine:
        //_imgDwarf.bringToFront();
        //however this might be more elegant solution overall
        //z-priority (order of image if moved on top of each other, lower -> higher):
        _img.setTranslationZ(1.1f);
        _grid.setTranslationZ(0.0f);



        //_grid.setRotationX(10.0f);
        //_img.setRotationX(10.0f);

        ImageView _iwElf = MakeImage(R.drawable.elf_w_orig, new PointF(100f,100f));
        _rootLayout.addView(_iwElf);


        ImageView _iwWarr = MakeImage(R.drawable.warr, new PointF(200f,200f));
        _rootLayout.addView(_iwWarr);


        ImageView _iwMonk = MakeImage(R.drawable.head, new PointF(300f,400f));
        _rootLayout.addView(_iwMonk);

        ImageView _iwMonk2 = MakeImage(R.drawable.head2, new PointF(300f,400f));
        _rootLayout.addView(_iwMonk2);


        //ImageView IW = MakeImage(int imageviewID, int imageID);
        //tests for addin image:
        //ImageView _imgElf = (ImageView) findViewById(R.id._img2);
        //_imgElf.setImageResource(R.drawable.elf_w_orig);
        //mainLayOut.addView(_imgShadow);



        //get all images (views) from root layout and put needed attributes () to array
        //ps. java's array's size have to be declared before insertion -> maybe use arraylist instead
        int childCount = _rootLayout.getChildCount();
        ImagesWithZ = new ImageZ[childCount];

        for (int i=0; i < childCount; i++){
            View v = _rootLayout.getChildAt(i);

            ImagesWithZ[i] = new ImageZ(v.getTag(), v.getTranslationZ(), v.getY(), v.getX());
        }

        //initial sorting by the y-value:
        Arrays.sort(ImagesWithZ);

        ImageZ[] oldImageZArray = new ImageZ[4];

        _img.setOnTouchListener(new MyTouchListener());
        _iwElf.setOnTouchListener(new MyTouchListener());
        _iwWarr.setOnTouchListener(new MyTouchListener());
        _iwMonk.setOnTouchListener(new MyTouchListener());
        _iwMonk2.setOnTouchListener(new MyTouchListener());

        //still-animation for dwarf-image:

        _adimgDwarf = ( AnimationDrawable)_img.getBackground();
        _adimgDwarf.setOneShot(false);
        _adimgDwarf.start();


    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private ImageView MakeImage(int imageID, PointF point) {

        ImageView _tempImg = new ImageView(this);
        _tempImg.setImageResource(imageID);
        _tempImg.setTag(imageID);


        int childCount = _rootLayout.getChildCount();
        View v = _rootLayout.getChildAt(childCount-1);

        if(v != null){
            _tempImg.setTranslationZ(v.getTranslationZ()+0.1f);
        }
        else{
            _tempImg.setTranslationZ(1.1f);
        }

        RelativeLayout.LayoutParams _imgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        _tempImg.setLayoutParams(_imgParams);

        //border for image for testing purposes
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(3,Color.BLUE);
        _tempImg.setBackground(gd);



        _tempImg.setX(point.x);
        _tempImg.setY(point.y);

        return _tempImg;
    }

    private void saveCurrentImageToZ(ImageZ iZ){
        for(int i = 0; i < ImagesWithZ.length; i++){
            if(ImagesWithZ[i].getResourceID() == iZ.getResourceID()){
                ImagesWithZ[i] = iZ;
            }
        }
    }

    private ImageZ getCurrentImageFromZ(Object tag){
        for(int i = 0; i < ImagesWithZ.length; i++){
            if(ImagesWithZ[i].getResourceID() == tag){
                return ImagesWithZ[i];
            }
        }
        return null;
    }

    private void cloneImageZToOld(){
        for(int k = 0; k < ImagesWithZ.length; k++){
            oldImageZArray[k] = (ImageZ)ImagesWithZ[k].clone();
        }
    }

    private void updateLayoutZ(){

        /*
        adds newly made float (starts at 1.1, adds 0.1 per step) to EVERY object's z-value in layout
        that appears in ordered (ordered previously by y-coordinates) ImagesWithZ-array
        (only added images there, layout's children includes all components ex. buttons)

        ordering could be done here before everything. Ordering not used anywhere else atm

        Arrays.sort(ImagesWithZ);
        */

        int childCount = _rootLayout.getChildCount();
        float zValue = 1.1f;

        for (int k=0; k < ImagesWithZ.length; k++){
            for (int l=0; l < childCount; l++) {

                if (ImagesWithZ[k].getResourceID() == _rootLayout.getChildAt(l).getTag()) {
                    _rootLayout.getChildAt(l).setTranslationZ(zValue);
                    zValue += 0.1f;
                }
            }
        }
    }

    private PointF getClosestSnap(PointF p1, PointF[] points){

        Float currentPointX = p1.x;
        Float currentPointY = p1.y;

        Float xFromPoints = 0f;
        Float yFromPoints = 0f;
        PointF closestPoint = null;

        Double distance = null;
        Double prevDistance = null;


        for(int i = 0; i < points.length ; i++){

            xFromPoints = points[i].x;
            yFromPoints = points[i].y;

            distance = Math.sqrt((Math.pow(currentPointX.doubleValue() - xFromPoints.doubleValue(), 2)) + (Math.pow(currentPointY.doubleValue() - yFromPoints.doubleValue(), 2)));

            if(prevDistance == null){
               closestPoint = points[i];
            }
            else{
                if(distance < prevDistance){
                    closestPoint = points[i];
                }
            }

            prevDistance = distance;
        }
        return closestPoint;
    }

    private Boolean hasAlreadyTaken(PointF p){

        for(int i = 0 ; i < ImagesWithZ.length; i++){
            if(p.y == ImagesWithZ[i].getyCoord() && p.x == ImagesWithZ[i].getxCoord()){
                return true;
            }
            else if(_imgShadow.getX() == p.x && _imgShadow.getY() == p.y)
            {
                return true;
            }

        }

        return false;
    }

//    public boolean isTransparent(ImageZ img, int x, int y ) {
//        Bitmap bitmap = ((BitmapDrawable)img. .getDrawable()).getBitmap();
//        int transparency = ((bitmap.getPixel(x,y) & 0xff000000) >> 24);
//    }

    public class MyTouchListener implements View.OnTouchListener {

        PointF _downPT = new PointF();
        PointF _startPT = new PointF();
        PointF _origStart = new PointF();

        PointF _previousPoint = null;
        PointF _currentClosestPoint = null;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            //int resId = getResources().getIdentifier(view.getTag().toString(), "drawable", getPackageName());
            //create shadow(1st image of the animation, correct frame could be calculated)

            //public:
            //ImageZ currentImageZ = new ImageZ();
            //ImageZ[] oldImageZArray = new ImageZ[0];


            try {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                int tempResId = getResources().getIdentifier(view.getTag().toString(), "drawable", getPackageName());
                ImageView tempView = new ImageView(getApplicationContext());
                tempView.setImageResource(tempResId);


                BitmapDrawable drawable = (BitmapDrawable)tempView.getDrawable();


//                    tempView.setDrawingCacheEnabled(true);
//                    Drawable drawable = ((ImageView)view).getDrawable();


                Bitmap bitmap = drawable.getBitmap();

//                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                    Bitmap bitmap = drawable.getBitmap();

                int transparency = -1;
                int bitmapheight = bitmap.getHeight();

                if(y < bitmapheight) {

                    //Bitmap tempBitmap = ((BitmapDrawable)tempView.getBackground()).getBitmap();
                    transparency = ((bitmap.getPixel((int) x, (int) y) & 0xff000000) >> 24);
                }
                else
                {
                    String s = "error";

                }

                //TODO use TODO more


                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if (transparency < 0)
                        {

                        currentImageZ = getCurrentImageFromZ(view.getTag());

    //
    //                    imageView.setDrawingCacheEnabled(true);
    //                    Drawable drawable = ((ImageView)view).getDrawable();
    //                    Bitmap bitmap = imageView.getDrawingCache();




                        //motionEvent.


    //                    // Create empty BufferedImage, sized to Image
    //                    BufferedImage buffImage =
    //                            new BufferedImage(
    //                                    image.getWidth(null),
    //                                    image.getHeight(null),
    //                                    BufferedImage.TYPE_INT_ARGB);
    //                    Graphics g = buffImage.createGraphics();
    //                    g.drawImage(image, 0, 0, null);
    //                    //Dispose the Graphics
    //                    g.dispose();
    //
    //
    //                    currentImageZ.





                            cloneImageZToOld();

                            int resId = getResources().getIdentifier(view.getTag().toString(), "drawable", getPackageName());

                            _imgShadow = new ImageView(getApplicationContext());
                            _imgShadow.setImageResource(resId);

                            //visibility:
                            _imgShadow.setAlpha((float) 0.5);

                            //attach correct parameters to image (equals parameters in xml, for example, android:layout_width="wrap_content", mandatory, null != accepted)
                            _imgParamsShadow = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            _imgShadow.setLayoutParams(_imgParamsShadow);

                            //set starting point to movable image:
                            _downPT.x = motionEvent.getX();
                            _downPT.y = motionEvent.getY();
                            _startPT = new PointF(view.getX(), view.getY() );
                            _origStart = new PointF(view.getX(), view.getY() );

                            //move shadow-image to correct position (where it was when movement started)
                            _imgShadow.setX(_startPT.x);
                            _imgShadow.setY(_startPT.y);
                            _imgShadow.setTranslationZ(0.01f);

                            //add image to main layout
                            _rootLayout.addView(_imgShadow);

                            _imgShadow2 = new ImageView(getApplicationContext());
                            _imgParamsShadow2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        }


                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (transparency < 0) {

                            //move image:
                            PointF movement = new PointF(motionEvent.getX() - _downPT.x, motionEvent.getY() - _downPT.y);
                            view.setX((int) (_startPT.x + movement.x));
                            view.setY((int) (_startPT.y + movement.y));
                            _startPT = new PointF(view.getX(), view.getY());


                            //CHANGE THE Z-INDEX OF THE IMAGES -->>
                            //comparison from previous - current to determine changes in order

                            currentImageZ.setyCoord(view.getY());
                            //saves coordinate changes to ImageZ-array:
                            saveCurrentImageToZ(currentImageZ);
                            //sort array by changed y to determine if order has changed:
                            Arrays.sort(ImagesWithZ);

                            //this changes every image's z that are in layout (in same order than ordered ImagesWithZ-array) by iterated counter
                            updateLayoutZ();

                            //get most close (2 points atm) point for snap-functions (shadow for snaptarget & location of snap)
                            _currentClosestPoint = getClosestSnap(_startPT, pointArray);

                            //check if closest point already has an image in it
                            Boolean hasTaken = hasAlreadyTaken(_currentClosestPoint);

                            int resID = 0;

                            //Show STOP:
                            //if target spot has already an image OR imageshadow (hasTaken = true):
                            if (hasTaken) {
                                resID = getResources().getIdentifier("stop", "drawable", getPackageName());
                                _imgShadow2.setTranslationZ(100f);
                                _imgShadow2.setAlpha((float) 1);
                            }
                            //if image is dragged exactly to the target spot:
                            else if (_currentClosestPoint.x == view.getX() && _currentClosestPoint.y == view.getY()) {
                                resID = getResources().getIdentifier("stop", "drawable", getPackageName());
                                _imgShadow2.setTranslationZ(100f);
                                _imgShadow2.setAlpha((float) 1);
                            }
                            //Show shadow:
                            //if theres no image OR imageshadow in snapspot
                            else {
                                resID = getResources().getIdentifier(view.getTag().toString(), "drawable", getPackageName());
                                _imgShadow2.setAlpha((float) 0.2);
                                _imgShadow2.setTranslationZ(0.01f);
                            }

                            //make snapshadow
                            _imgShadow2.setImageResource(resID);
                            _imgShadow2.setLayoutParams(_imgParamsShadow2);
                            _imgShadow2.setX(_currentClosestPoint.x);
                            _imgShadow2.setY(_currentClosestPoint.y);

                            //add shadow to layout if it's position has changed (delete previous shadow -> only one instance of shadow up)
                            if (_previousPoint != _currentClosestPoint) {
                                _rootLayout.removeView(_imgShadow2);
                                _rootLayout.addView(_imgShadow2);
                            }

                            //updating previouspoint
                            _previousPoint = _currentClosestPoint;




                        /*
                        //DISCLAIMER: when the array is sorted, would it be easier to just change the z (all of them in loop, like done before?) according to new order
                        //than trying to find those two that has changed?
                        is this better or faster than updateLayoutZ()?

                        //compares previous and current - old versus updated and sorted - array to see if any changes in z-indexes has to be made:
                        for(int i = 0; i < ImagesWithZ.length; i++){
                            if(oldImageZArray[i].getResourceID() != ImagesWithZ[i].getResourceID()){
                                //goes here when first anomaly between two arrays are found (break from loop after logic, no need to go further)
                                for(int j = 0; j < ImagesWithZ.length; j++) {

                                    if(oldImageZArray[i].getResourceID() == ImagesWithZ[j].getResourceID()){
                                        //goes here when the original row is found (that's replacement was found in previous loop)

                                        //copies z-values from old to new
                                        ImagesWithZ[i].setTransZValue(oldImageZArray[i].getTransZValue());
                                        ImagesWithZ[j].setTransZValue(oldImageZArray[j].getTransZValue());


                                        //update z-changes (2 pcs.) to rootlayout from changed (latest) array:------------------

                                        int childCount = _rootLayout.getChildCount();

                                        for (int k=0; k < childCount; k++){
                                            if(_rootLayout.getChildAt(k).getTag() == ImagesWithZ[i].getResourceID()){
                                                _rootLayout.getChildAt(k).setTranslationZ(ImagesWithZ[i].getTransZValue());
                                            }
                                            else if(_rootLayout.getChildAt(k).getTag() == ImagesWithZ[j].getResourceID()){
                                                _rootLayout.getChildAt(k).setTranslationZ(ImagesWithZ[j].getTransZValue());
                                            }
                                        }

                                        //make changes to rootlayout from changed (latest) array:------------------

                                        //copies the changed array to old array for the next movement:
                                        cloneImageZToOld();

                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        */

                        /* tests for movement, harder to find the right spot for the image than previous method
                        int x_cord = (int)motionEvent.getRawX();
                        int y_cord = (int)motionEvent.getRawY();

                        layoutParams.leftMargin = x_cord - 150;
                        layoutParams.topMargin = y_cord - 300;

                        _imgDwarf.setLayoutParams(layoutParams);
                        */

                        }

                        break;

                    case MotionEvent.ACTION_UP:

                        if (transparency < 0) {

                            _rootLayout.removeView(_imgShadow);
                            _rootLayout.removeView(_imgShadow2);

                            //delete references to shadow and snap
                            _previousPoint = null;
                            _currentClosestPoint = null;

                            TranslateAnimation moveImage = new TranslateAnimation(
                                /*  startx,
                                *   endx,
                                *   starty,
                                    endy
                                    ALL are calculated FROM current point of image
                                */
                                    _origStart.x - _startPT.x,
                                    0,
                                    _origStart.y - _startPT.y,
                                    0
                            );

                            //moveImage.setFillAfter(true);

                            moveImage.setDuration(250);

                            view.startAnimation(moveImage);

                        }

                        break;

                    default:
                        //nothing here
                        break;

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return true;
        }
    }

    //not used:
    private class MyDragListener implements View.OnDragListener {

        Drawable enterShape = getDrawable(R.drawable.dwarf_5);
        Drawable normalShape = getDrawable(R.drawable.dwarf_2);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }
    }
}
