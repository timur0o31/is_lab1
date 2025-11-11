package ru.itmo.tim.responseDto;

public class CoordinatesResponseDto {
    private int id;
    private long x;
    private int y;
    public CoordinatesResponseDto() {
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
