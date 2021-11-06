package com.justserver.apocalypse.utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class LocationUtil {
    public static Location addInDirection(Location inner, BlockFace direction, double addition){
        Location result = inner.clone();
        switch (direction){
            case NORTH:
                result = result.add(0, 0, -addition);
                break;
            case EAST:
                result = result.add(addition, 0, 0);
                break;
            case WEST:
                result = result.add(-addition, 0,0);
                break;
            case SOUTH:
                result = result.add(0, 0, addition);
                break;
            case UP:
                result = result.add(0, addition, 0);
                break;
            case DOWN:
                result = result.add(0, -addition, 0);
                break;
        }
        return result;
    }

    public static Location addInDirection(Location inner, BlockFace direction, Vector add){
        Location result = inner.clone();
        switch (direction){
            case NORTH:
                result = result.add(add.getX(), add.getY(), -add.getZ());
                break;
            case EAST:
                result = result.add(-add.getZ(), add.getY(), add.getX());
                break;
            case WEST:
                result = result.add(add.getZ(), add.getY(),-add.getX());
                break;
            case SOUTH:
                result = result.add(-add.getX(), add.getY(), add.getZ());
                break;

        }
        return result;
    }

    public static Location subtractInDirection(Location inner, BlockFace direction, Vector add){
        Location result = inner.clone();
        switch (direction){
            case NORTH:
                result = result.add(-add.getX(), -add.getY(), add.getZ());
                break;
            case EAST:
                result = result.add(add.getZ(), -add.getY(), -add.getX());
                break;
            case WEST:
                result = result.add(-add.getZ(), -add.getY(),add.getX());
                break;
            case SOUTH:
                result = result.add(add.getX(), -add.getY(), -add.getZ());
                break;

        }
        return result;
    }

    public static BlockFace getOpposite(BlockFace direction){
        switch (direction){
            case NORTH:
                return BlockFace.SOUTH;
            case EAST:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.EAST;
            default:return BlockFace.NORTH;
        }
    }

    public static Location addInRelativeSide(Location inner, RelativeDirection direction, BlockFace facing, int toAdd){
        Location result = inner.clone();
        switch (direction){

            case FORWARD:
                if(facing.equals(BlockFace.NORTH)){
                    result.add(0, 0, -toAdd);
                } else if(facing.equals(BlockFace.SOUTH)){
                    result.add(0, 0, toAdd);
                } else if(facing.equals(BlockFace.WEST)){
                    result.add(-toAdd, 0, 0);
                } else {
                    result.add(toAdd, 0, 0);
                }
                break;
            case BACKWARDS:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
        }
        return result;
    }

    public static Location addInHorizontalRel(Location inner, BlockFace facing, int add){
        Location result = inner.clone();
        switch (facing){
            case NORTH:
                result = result.add(add, 0, 0);
                break;
            case EAST:
                result = result.add(0, 0, add);
                break;
            case WEST:
                result = result.add(0, 0, -add);
                break;
            case SOUTH:
                result = result.add(-add, 0, 0);
                break;

        }
        return result;
    }

    public static BlockFace getRelative(RelativeDirection direction, BlockFace facing){
        switch (direction){

            case FORWARD:
                return facing;
            case BACKWARDS:
                return getOpposite(facing);
            case LEFT:
                if(facing.equals(BlockFace.NORTH)){
                    return BlockFace.WEST;
                } else if(facing.equals(BlockFace.SOUTH)){
                    return BlockFace.EAST;
                } else if(facing.equals(BlockFace.WEST)){
                    return BlockFace.SOUTH;
                } else {
                    return BlockFace.NORTH;
                }
            case RIGHT:
                if(facing.equals(BlockFace.NORTH)){
                    return BlockFace.EAST;
                } else if(facing.equals(BlockFace.SOUTH)){
                    return BlockFace.WEST;
                } else if(facing.equals(BlockFace.WEST)){
                    return BlockFace.NORTH;
                } else {
                    return BlockFace.SOUTH;
                }
        }
        return BlockFace.NORTH;
    }
}
