/*******************************************************************************
 *
 *   Copyright 2015 Walmart, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *******************************************************************************/
package com.oneops.controller.cms;

import com.oneops.cms.cm.service.CmsCmProcessor;
import com.oneops.cms.md.service.CmsMdProcessor;
import com.oneops.cms.util.domain.CmsVar;

public class ControllerCache {

    private static final String MD_CACHE_STATUS_VAR = "MD_UPDATE_TIMESTAMP";

    private CmsCmProcessor cmsCmProcessor;
    private CmsMdProcessor mdProcessor;
    private long lastUpdatedTs;

    public void invalidateMdCacheIfRequired() {
    	if (mdProcessor.isCacheEnabled()) {
    		CmsVar var = cmsCmProcessor.getCmSimpleVar(MD_CACHE_STATUS_VAR);
        	if (var != null) {
        		long updateTs = Long.parseLong(var.getValue());
        		if (updateTs > lastUpdatedTs) {
                    lastUpdatedTs = updateTs;
                    mdProcessor.invalidateCache();
        		}
        	}	
    	}
    }

	public void setCmsCmProcessor(CmsCmProcessor cmsCmProcessor) {
		this.cmsCmProcessor = cmsCmProcessor;
	}

	public void setMdProcessor(CmsMdProcessor mdProcessor) {
		this.mdProcessor = mdProcessor;
	}

}
