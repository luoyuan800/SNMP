package ly.snmp.core.monitor;/*
 * Monitor.java
 * Date: 3/30/2015
 * Time: 9:05 AM
 * 
 * Copyright 2015 Dell Inc.
 * ALL RIGHTS RESERVED.
 * 
 * This software is the confidential and proprietary information of
 * Dell Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with Dell Inc.
 * 
 * DELL INC. MAKES NO REPRESENTATIONS OR WARRANTIES
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. DELL SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

import ly.snmp.core.model.Oid;

import java.util.Set;

public interface Monitor {
    public Set<Oid> getOIDs();
    public void build(Long time);
}
