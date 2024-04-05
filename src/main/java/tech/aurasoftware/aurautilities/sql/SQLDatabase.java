package tech.aurasoftware.aurautilities.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp.BasicDataSource;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.configuration.serialization.annotation.Ignored;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor
@Getter
public class SQLDatabase implements Serializable {

    private boolean enabled = false;
    private String host = "localhost";
    private int port = 3306;
    private String database = "database";
    private String username = "username";
    private String password = "password";

    @Ignored
    private DataSource dataSource;

    public SQLDatabase setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SQLDatabase setHost(String host) {
        this.host = host;
        return this;
    }

    public SQLDatabase setPort(int port) {
        this.port = port;
        return this;
    }

    public SQLDatabase setDatabase(String database) {
        this.database = database;
        return this;
    }

    public SQLDatabase setUsername(String username) {
        this.username = username;
        return this;
    }

    public SQLDatabase setPassword(String password) {
        this.password = password;
        return this;
    }

    public void createDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);


        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeDataSource(){
        if(dataSource instanceof BasicDataSource){
            try {
                ((BasicDataSource) dataSource).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CompletableFuture<ResultSet> query(String query, Object... wildcards){
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = getConnection()){
                if(connection == null){
                    return null;
                }
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                for(int i = 0; i < wildcards.length; i++){
                    preparedStatement.setObject(i + 1, wildcards[i]);
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                connection.close();
                return resultSet;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<Boolean> queryUpdate(String query, Object... wildcards){
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = getConnection()){
                if(connection == null){
                    return false;
                }
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                for(int i = 0; i < wildcards.length; i++){
                    preparedStatement.setObject(i + 1, wildcards[i]);
                }
                boolean result = preparedStatement.execute();
                preparedStatement.close();
                connection.close();
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        });
    }



}
