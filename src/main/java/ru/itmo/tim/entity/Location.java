package ru.itmo.tim.entity;
import javax.persistence.*;

@Embeddable
public class Location {
    private int x;
    private int y;
    private long z;
    public Location() {

    }
    public Location(int x, int y, long z, String name) {

        this.x = x;
        this.y = y;
        this.z = z;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }
}
