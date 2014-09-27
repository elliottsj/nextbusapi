package net.sf.nextbus.publicxmlfeed.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Junction class/table used by OrmLite to specify that a stop is associated with a direction
 */
@DatabaseTable(tableName = "directions_stops")
public class DirectionStop implements Serializable {

    private static final long serialVersionUID = -4047001999513323928L;

    public static final String FIELD_DIRECTION_ID = "direction_id";
    public static final String FIELD_STOP_ID = "stop_id";

    @DatabaseField(columnName = FIELD_DIRECTION_ID, foreign = true, uniqueCombo = true)
    private Direction direction;

    @DatabaseField(columnName = FIELD_STOP_ID, foreign = true, foreignAutoRefresh = true, uniqueCombo = true)
    private Stop stop;

    /**
     * Empty constructor for OrmLite
     */
    DirectionStop() {
    }

    public DirectionStop(Direction direction, Stop stop) {
        this.direction = direction;
        this.stop = stop;
    }

    public Direction getDirection() {
        return direction;
    }

    public Stop getStop() {
        return stop;
    }

}
