package com.example.ago.heart2;

/**
 * Created by Ago on 3.4.2016.
 */
public class ImageZ implements Comparable <ImageZ>, Cloneable {

    private Object resourceID;
    private float transZValue;
    private float yCoord;
    private float xCoord;

    public ImageZ(Object resourceID, float transZValue, float yCoord, float xCoord) {
        this.resourceID = resourceID;
        this.transZValue = transZValue;
        this.yCoord = yCoord;
        this.xCoord = xCoord;
    }

    public ImageZ() {
        resourceID = null;
        transZValue = 0;
        yCoord = 0;
        xCoord = 0;
    }

    public Object getResourceID() {
        return resourceID;
    }

    public float getxCoord() {
        return xCoord;
    }

    public float getTransZValue() {
        return transZValue;
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setResourceID(Object resourceID) {
        this.resourceID = resourceID;
    }

    public void setTransZValue(float transZValue) {
        this.transZValue = transZValue;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }

    public void setxCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    @Override
    public Object clone(){
        ImageZ imgz = new ImageZ(resourceID, transZValue, yCoord, xCoord);
        return imgz;
    }

    @Override
    public int compareTo(ImageZ _iz){
        return Float.compare(yCoord, _iz.getyCoord());
    }
}

