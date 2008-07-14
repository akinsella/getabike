package org.helyx.app.j2me.lib.rms;

public interface IBasicRecordDao {

	public abstract void dispose();

	public abstract Object findFirstRecord();

	public abstract void removeAllRecords();

	public abstract void saveNewRecord(Object record);

}