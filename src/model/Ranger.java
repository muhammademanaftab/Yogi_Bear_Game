package model;

public class Ranger {
    private Position position;

    public Ranger(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

//    public boolean isNear(Position yogiPosition) {
//        for (Direction direction : Direction.values()) {
//            Position neighbor = yogiPosition.translate(direction);
//            if (neighbor.x == position.x && neighbor.y == position.y) {
//                return true;
//            }
//        }
//        return false;
//    }
}
