package client;


import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class DBClient extends  Thread {

    private String nameClient;
    private OracleDataSource ods;
    private Connection conn;

    public DBClient(String name, String url, String usr, String pwd) {
        setNameClient(name);
        InitConnection(url, usr, pwd);
        try {
            setDBAttributes(name, "RUN");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBClient(String name) {
        this(name, "jdbc:oracle:thin:@10.67.24.154:1521/ibsbeta", "test_stat", "test_stat");
    }


    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getNameClient() {
        return nameClient;
    }

    private void InitPoolConnection(String url, String usr, String pwd) throws SQLException {
        ods = new OracleDataSource();
        ods.setURL(url);
        ods.setUser(usr);
        ods.setPassword(pwd);
    }

    private void InitConnection(String url, String usr, String pwd) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC not found");
            e.printStackTrace();
            return;
        }
        try {
            conn = DriverManager.getConnection(url, usr, pwd);
        } catch (SQLException e) {
            System.out.println("Connection failed. " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setDBAttributes(String module, String action) throws SQLException {
        PreparedStatement stmt = getConnection().prepareStatement("begin sys.dbms_application_info.set_module(?,?); end;\n");
        stmt.setString(1, module);
        stmt.setString(2, action);
        stmt.execute();
        stmt.close();
    }

    private Connection getConnection() {
        return conn;
    }


    public void run() {
        System.out.println(getNameClient() + " running.");
        while (!Thread.interrupted()) {
            PreparedStatement stmt ;
            try {
                stmt = getConnection().prepareStatement("declare s varchar2(100); s2 varchar2(100); begin dbms_application_info.read_module(s,s2);  insert into test_stat.main (select test_stat.seq_id.nextval, \n" +
                        "                         s,\n" +
                        "                         mod(test_stat.seq_id.currval,100),\n" +
                        "                         mod(test_stat.seq_id.currval,435),\n" +
                        "                         sysdate-mod(test_stat.seq_id.currval,1234),\n" +
                        "                         mod(test_stat.seq_id.currval,4)+1 from dual connect by level < 100); commit;end;\n");
                stmt.execute();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            finally {
//                    stmt.close();
//            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(getNameClient() + " stopped.");
                return;
            }
        }

    }

}
