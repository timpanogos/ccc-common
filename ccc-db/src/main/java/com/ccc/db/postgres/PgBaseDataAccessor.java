package com.ccc.db.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccc.db.DataAccessor;
import com.ccc.tools.StrH;
import com.ccc.tools.TabToLevel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@SuppressWarnings("javadoc")
public class PgBaseDataAccessor implements DataAccessor
{
    public static final String JdbcDriverKey = "ccc.tools.da.jdbc-driver";
    public static final String NonServletKey = "ccc.tools.da.non-servlet";
    
//    public static final String JdbcDriverDefault = "com.mysql.jdbc.Driver";
    public static final String JdbcDriverDefault = "org.postgresql.Driver";
    public static String DaPortDefault = "5432"; 
    public static final String NonServletDefault = "false";
    public static String DaUrlPrefixDefault = "jdbc:postgresql://"; 
    public static final String TomcatJndiDsNamePrefix = "java:comp/env/";

    private static final AtomicInteger totalConnections = new AtomicInteger();
    private static final Hashtable<Connection, AtomicInteger> connections = new Hashtable<Connection, AtomicInteger>();

    protected final Logger log;
    protected final AtomicBoolean dsIsMine;
    protected static volatile DataSource dsource;

    public PgBaseDataAccessor()
    {
        dsIsMine = new AtomicBoolean(false);
        log = LoggerFactory.getLogger(getClass());
    }

    /* ****************************************************************************
     * DataAccessor implementation    
    ******************************************************************************/
    @Override
    public void init(Properties properties) throws Exception
    {
        TabToLevel format = new TabToLevel("JDBC initialization:");
        String jdbcDriver = StrH.getParameter(properties, JdbcDriverKey, JdbcDriverDefault, format);
        boolean nonServlet = Boolean.parseBoolean(StrH.getParameter(properties, NonServletKey, NonServletDefault, format));
        Class.forName(jdbcDriver);
        if (nonServlet)
        {
            dsIsMine.set(true);
            String user = StrH.getParameter(properties, DaUserKey, format);
            String pass = StrH.getParameter(properties, DaPassKey, format);
            String dbName = StrH.getParameter(properties, DaDbNameKey, format);
            String host = StrH.getParameter(properties, DaHostKey, format);
            String port = StrH.getParameter(properties, DaPortKey, DaPortDefault, format);
            String urlPrefix = StrH.getParameter(properties, DaUrlPrefixKey, DaUrlPrefixDefault, format);
            
            try
            {
                HikariConfig config = new HikariConfig();
                String url = urlPrefix + host + ":" + port +"/" + dbName + "?characterEncoding=UTF-8";
                config.setJdbcUrl(url);
                config.setUsername(user); 
                config.setPassword(pass);
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                config.setIdleTimeout(0);
                dsource = new HikariDataSource(config);
            } catch (Exception e)
            {
                log.error(format.toString(), e);
                throw e;
            }
            log.debug(format.toString());
            return;
        }

        InitialContext ctx = null;
        ctx = new InitialContext();
        String datasourceName = StrH.getParameter(properties, DaDataSourceTomcatKey, format);
        datasourceName = TomcatJndiDsNamePrefix + datasourceName;
        format.ttl("full context name: " + datasourceName);
        dsource = (DataSource) ctx.lookup(datasourceName);
        if (dsource == null)
        {
            String msg = "failed to obtain the DataSource: " + datasourceName;
            log.error(msg);
            throw new Exception(msg);
        }
        format.ttl("DataSource class: ", dsource.getClass().getName());
        log.info(format.toString());
        // cause db connection to be established, this takes noticeable time on aws for the first connection
        dsource.getConnection().close();
    }

    //TODO: setup a latch so if you know it's down, return quick
    // reset latch on dbup event
    @Override
    public boolean isUp()
    {
        try
        {
            dsource.getConnection().isValid(10000);
            return true;
        } catch (SQLException e)
        {
            return false;
        }
    }
    
    
    @Override
    public void close()
    {
        if (dsIsMine.get())
        {
            if(dsource != null)
                ((HikariDataSource) dsource).close();
        }
    }

    public static Connection getConnection() throws SQLException
    {
        Connection conn = dsource.getConnection();
        AtomicInteger counter = connections.get(conn);
        if(counter == null)
        {
            counter = new AtomicInteger(0);
            connections.put(conn, counter);
        }
        counter.incrementAndGet();
        totalConnections.incrementAndGet();
        return conn;
    }
    
    public static void close(Connection connection, Statement stmt, ResultSet rs, boolean closeConn) throws SQLException
    {
        try
        {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        } finally
        {
            // close the connection if either of the above two fail
            if (connection != null && closeConn)
            {
                connection.close();
                AtomicInteger counter = connections.get(connection);
                counter.decrementAndGet();
                totalConnections.decrementAndGet();
            }
        }
    }
}
