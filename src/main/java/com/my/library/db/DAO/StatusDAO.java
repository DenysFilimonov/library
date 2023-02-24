package com.my.library.db.DAO;
import com.my.library.db.DTO.StatusDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Status;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;

public class StatusDAO implements DAO<Status> {

    private final BasicDataSource dataSource;
    private static StatusDAO instance = null;
    private  static Object mutex = new Object();


    private StatusDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static StatusDAO getInstance(BasicDataSource dataSource){
        StatusDAO result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new StatusDAO(dataSource);
            }
        }
        return result;
    }

    public static void destroyInstance(){
        instance=null;
    }

    @Override
    public void add(Status status) throws SQLException {
        {

            String INSERT_STRING = "insert into statuses " +
                    "(status, status_ua ) " +
                    "values (?, ?)";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement insertStatus = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
                connection.setAutoCommit(false);
                insertStatus.setString(1, status.getStatus().get("en"));
                insertStatus.setString(2, status.getStatus().get("ua"));
                int affectedRows = insertStatus.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating status failed, no rows affected.");
                }
                try (ResultSet generatedKeys = insertStatus.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        status.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating status failed, no ID obtained.");
                    }
                }
                connection.commit();
            }
        }
    }

    @Override
    public void delete(Status status) throws SQLException{
        String DELETE_STRING = "DELETE FROM statuses WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement deleteStatus = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteStatus.setInt(1, status.getId());
            int affectedRows = deleteStatus.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete status failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public void update(Status status) throws SQLException{
        String UPDATE_STRING = "UPDATE statuses SET " +
                "status = ?, " +
                "status_ua = ? " +
                " WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateRole = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            updateRole.setString(1, status.getStatus().get("en"));
            updateRole.setString(2, status.getStatus().get("ua"));
            updateRole.setInt(3, status.getId());
            int affectedRows = updateRole.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating status failed, no rows affected.");
            }
            connection.commit();

        }
    }

    @Override
    public int count(SQLBuilder query) throws SQLException {
        ResultSet resultSet = null;
        int count=0;
        try  (Connection connection = dataSource.getConnection();
              Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(query.getSQLStringCount());
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    @Override
    public ArrayList<Status> get(SQLBuilder query) throws SQLException{
        ArrayList<Status> types = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.getSQLString());
            while (resultSet.next()) {
                types.add(StatusDTO.toModel(resultSet));
            }
        }
        return types;
    }

    @Override
    public Status getOne(int id) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Status().table).
                filter("id", id, SQLBuilder.Operators.E).build();
        ArrayList<Status> statuses = get(sq);
        return statuses.isEmpty()? null: statuses.get(0);
    }

}