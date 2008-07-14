package org.helyx.app.j2me.lib.rms;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;

public interface IMultiRecordDao {

	public abstract void dispose();

	public abstract Vector findAllRecords();

	public abstract void removeAllRecords();

	public abstract void saveRecordArray(Object[] recordArray);

	public abstract MultiRecordEnumeration createRecordEnumeration(
			IRecordFilter recordFilter);

	public abstract void destroyRecordEnumeration(
			MultiRecordEnumeration multiRecordEnumeration);

	public abstract int countRecords();

}