package org.ligi.android.dubwise_uavtalk.uavtalk;

import android.content.SearchRecentSuggestionsProvider;

/**
 * TODO finish that
 * @author ligi
 *
 */
public class UAVObjectsSearchSuggestions extends
		SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "org.ligi.android.dubwise_uavtalk.uavtalk.UAVObjectsSearchSuggestions";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public UAVObjectsSearchSuggestions() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
