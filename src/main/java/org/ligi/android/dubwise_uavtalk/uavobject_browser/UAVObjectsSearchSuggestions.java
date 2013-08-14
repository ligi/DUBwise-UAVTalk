package org.ligi.android.dubwise_uavtalk.uavobject_browser;

import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjects;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * a ContentProvider for SearchSuggestions for UAVObjects
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVObjectsSearchSuggestions extends ContentProvider {
    
	private static final String[] COLUMNS = {
        "_id",  // must include this column
        SearchManager.SUGGEST_COLUMN_TEXT_1,
        SearchManager.SUGGEST_COLUMN_QUERY
        };
    
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String query=uri.getLastPathSegment().toLowerCase();
		
		MatrixCursor cursor = new MatrixCursor(COLUMNS);
		for (UAVObject obj : UAVObjects.getUAVObjectArray())
			if (obj.getObjName().toLowerCase().startsWith(query))
				cursor.addRow(new Object[] {obj.getObjID() ,obj.getObjName(),obj.getObjName()});
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}



}
