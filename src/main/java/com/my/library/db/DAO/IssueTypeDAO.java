package com.my.library.db.DAO;
import com.my.library.db.DTO.IssueTypeDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.IssueType;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;

public class IssueTypeDAO implements DAO<IssueType> {

    private final BasicDataSource dataSource;
    private static IssueTypeDAO instance = null;

    private  static final Object mutex = new Object();


    private IssueTypeDAO(BasicDataSource dataSource){
        this.dataSource =dataSource;
    }

    public static IssueTypeDAO getInstance(BasicDataSource dataSource){
        IssueTypeDAO result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new IssueTypeDAO(dataSource);
            }
        }
        return result;
    }

    public static void destroyInstance(){
        instance = null;
    }

    @Override
    public void add(IssueType type) throws SQLException
    {
        String INSERT_STRING = "insert into issue_types " +
                "(issue_type, issue_type_ua, penalty_fee) "+
                "values (?, ? , ?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement insertIssueType = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertIssueType.setString(1, type.getIssueType().get("en"));
            insertIssueType.setString(2, type.getIssueType().get("ua"));
            insertIssueType.setFloat(3, type.getPenalty());

            int affectedRows = insertIssueType.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating issue type failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertIssueType.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    type.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating genre failed, no ID obtained.");
                }
            }
            connection.commit();
        }
    }

    @Override
    public void delete(IssueType type) throws SQLException{
        String DELETE_STRING = "DELETE FROM issue_types WHERE ID=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement deleteIssueType = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteIssueType.setInt(1, type.getId());
            int affectedRows = deleteIssueType.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete issue type failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public void update(IssueType type) throws SQLException{
        String UPDATE_STRING = "UPDATE issue_types SET " +
                "issue_type = ?, " +
                "issue_type_ua = ?, " +
                "penalty_fee = ?, " +
                " WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateIssueType = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            updateIssueType.setString(1, type.getIssueType().get("en"));
            updateIssueType.setString(2, type.getIssueType().get("ua"));
            updateIssueType.setFloat(3, type.getPenalty());
            updateIssueType.setInt(4, type.getId());

            int affectedRows = updateIssueType.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public int count(SQLBuilder query) throws SQLException {
        ResultSet resultSet;
        query.build();
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
    public ArrayList<IssueType> get(SQLBuilder query) throws SQLException{
        query.build();
        ArrayList<IssueType> types = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.getSQLString());
            while (resultSet.next()) {
                types.add(IssueTypeDTO.toModel(resultSet));
            }

        }
        return types;
    }

    @Override
    public IssueType getOne(int id) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new IssueType().table).
                filter("id", id, SQLBuilder.Operators.E).
                build();
        ArrayList<IssueType> issueTypes = get(sq);
        return issueTypes.isEmpty()? null: issueTypes.get(0);
    }


}