package com.ioter.uhfscan.data.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ioter.uhfscan.data.greendao.EpcModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EPC_MODEL".
*/
public class EpcModelDao extends AbstractDao<EpcModel, Long> {

    public static final String TABLENAME = "EPC_MODEL";

    /**
     * Properties of entity EpcModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Epc = new Property(1, String.class, "epc", false, "EPC");
        public final static Property Casecode = new Property(2, String.class, "casecode", false, "CASECODE");
        public final static Property Message = new Property(3, String.class, "message", false, "MESSAGE");
        public final static Property State = new Property(4, String.class, "state", false, "STATE");
        public final static Property Warehouse = new Property(5, String.class, "warehouse", false, "WAREHOUSE");
    }


    public EpcModelDao(DaoConfig config) {
        super(config);
    }
    
    public EpcModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EPC_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EPC\" TEXT," + // 1: epc
                "\"CASECODE\" TEXT," + // 2: casecode
                "\"MESSAGE\" TEXT," + // 3: message
                "\"STATE\" TEXT," + // 4: state
                "\"WAREHOUSE\" TEXT);"); // 5: warehouse
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EPC_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EpcModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String epc = entity.getEpc();
        if (epc != null) {
            stmt.bindString(2, epc);
        }
 
        String casecode = entity.getCasecode();
        if (casecode != null) {
            stmt.bindString(3, casecode);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(4, message);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(5, state);
        }
 
        String warehouse = entity.getWarehouse();
        if (warehouse != null) {
            stmt.bindString(6, warehouse);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EpcModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String epc = entity.getEpc();
        if (epc != null) {
            stmt.bindString(2, epc);
        }
 
        String casecode = entity.getCasecode();
        if (casecode != null) {
            stmt.bindString(3, casecode);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(4, message);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(5, state);
        }
 
        String warehouse = entity.getWarehouse();
        if (warehouse != null) {
            stmt.bindString(6, warehouse);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public EpcModel readEntity(Cursor cursor, int offset) {
        EpcModel entity = new EpcModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // epc
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // casecode
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // message
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // state
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // warehouse
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EpcModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEpc(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCasecode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMessage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setState(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWarehouse(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(EpcModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(EpcModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EpcModel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
