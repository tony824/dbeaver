/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2016 Serge Rieder (serge@jkiss.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (version 2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jkiss.dbeaver.ext.postgresql.model.session;

import org.jkiss.dbeaver.model.admin.sessions.DBAServerSession;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.meta.Property;
import org.jkiss.utils.CommonUtils;

import java.sql.ResultSet;
import java.util.Date;

/**
 * PostgreSQL session
 */
public class PostgreSession implements DBAServerSession {
    private static final String CAT_CLIENT = "Client";
    private static final String CAT_TIMING = "Timings";

    private int pid;
    private String user;
    private String clientHost;
    private String clientPort;
    private String db;
    private String query;
    private Date backendStart;
    private Date xactStart;
    private Date queryStart;
    private Date stateChange;
    private String state;
    private String appName;

    public PostgreSession(ResultSet dbResult) {
        this.pid = JDBCUtils.safeGetInt(dbResult, "pid");
        this.user = JDBCUtils.safeGetString(dbResult, "usename");
        this.clientHost = JDBCUtils.safeGetString(dbResult, "client_hostname");
        if (CommonUtils.isEmpty(this.clientHost)) {
            this.clientHost = JDBCUtils.safeGetString(dbResult, "client_addr");
        }
        this.clientPort = JDBCUtils.safeGetString(dbResult, "client_port");
        this.db = JDBCUtils.safeGetString(dbResult, "datname");
        this.query = JDBCUtils.safeGetString(dbResult, "query");

        this.backendStart = JDBCUtils.safeGetTimestamp(dbResult, "backend_start");
        this.xactStart = JDBCUtils.safeGetTimestamp(dbResult, "xact_start");
        this.queryStart = JDBCUtils.safeGetTimestamp(dbResult, "query_start");
        this.stateChange = JDBCUtils.safeGetTimestamp(dbResult, "state_change");

        this.state = JDBCUtils.safeGetString(dbResult, "state");
        this.appName = JDBCUtils.safeGetString(dbResult, "application_name");
    }

    @Property(viewable = true, order = 1)
    public int getPid()
    {
        return pid;
    }

    @Property(viewable = true, category = CAT_CLIENT, order = 2)
    public String getUser()
    {
        return user;
    }

    @Property(viewable = false, category = CAT_CLIENT, order = 3)
    public String getClientHost()
    {
        return clientHost;
    }

    @Property(viewable = false, category = CAT_CLIENT, order = 4)
    public String getClientPort() {
        return clientPort;
    }

    @Property(viewable = true, order = 5)
    public String getDb()
    {
        return db;
    }

    @Property(viewable = true, category = CAT_CLIENT, order = 6)
    public String getAppName() {
        return appName;
    }

    @Property(viewable = false, category = CAT_TIMING, order = 30)
    public Date getBackendStart() {
        return backendStart;
    }

    @Property(viewable = false, category = CAT_TIMING, order = 31)
    public Date getXactStart() {
        return xactStart;
    }

    @Property(viewable = true, category = CAT_TIMING, order = 32)
    public Date getQueryStart() {
        return queryStart;
    }

    @Property(viewable = false, category = CAT_TIMING, order = 33)
    public Date getStateChange() {
        return stateChange;
    }

    @Property(viewable = true, order = 7)
    public String getState()
    {
        return state;
    }

    @Property(viewable = true, order = 100)
    public String getBriefQuery() {
        if (query != null && query.length() > 50) {
            return CommonUtils.truncateString(query, 50) + " ...";
        } else {
            return query;
        }
    }

    @Override
    public String getActiveQuery()
    {
        return query;
    }

    @Override
    public String toString()
    {
        return pid + "@" + db;
    }
}
