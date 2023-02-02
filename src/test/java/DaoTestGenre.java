import com.my.library.db.DAO.AuthorDAO;
import com.my.library.db.DAO.GenreDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.entities.Genre;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DaoTestGenre {

    public Genre genre;

    @BeforeEach
    public void setEntity(){
        genre = new Genre();
        Map<String,String> genreMap = new HashMap<>();
        genreMap.put("en", "genreEn");
        genreMap.put("ua", "genreUa");
        genre.setGenre(genreMap);
        genre.setId(1);
        GenreDAO.destroyInstance();

    }

    @AfterEach
    public void clearDAO(){
        GenreDAO.destroyInstance();

    }

    @Test
    public void TestDeleteGenre() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        AuthorDAO authorDAO = AuthorDAO.getInstance(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(1), arg1.capture());
        GenreDAO.getInstance(dataSource).delete(this.genre);
        assertEquals(arg1.getValue(), this.genre.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
        GenreDAO.destroyInstance();
    }

    @Test
    public void TestAddGenre() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        GenreDAO.getInstance(dataSource).add(this.genre);
        assertEquals(arg1.getValue(), this.genre.getGenre().get("en"));
        assertEquals(arg2.getValue(), this.genre.getGenre().get("ua"));
        verify(preparedStatement, atLeast(1)).executeUpdate();
        verify(preparedStatement, atLeast(1)).getGeneratedKeys();
        verify(resultSet, atLeast(1)).next();
        verify(resultSet, atLeast(1)).getInt(1);
    }

    @Test
    public void TestGetAuthor() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        SQLSmartQuery sqlSmartQuery = mock(SQLSmartQuery.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.createStatement()).thenReturn(statement);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        when(statement.executeQuery(arg1.capture())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(sqlSmartQuery.build()).thenReturn("SELECT * FROM GENRES WHERE id=1 AND active = true");
        GenreDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.build());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
    }

    @Test
    public void TestUpdateAuthor() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(genre.getId());
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(3), arg3.capture());
        GenreDAO.getInstance(dataSource).update(this.genre);
        assertEquals(arg1.getValue(), this.genre.getGenre().get("en"));
        assertEquals(arg2.getValue(), this.genre.getGenre().get("ua"));
        assertEquals(arg3.getValue(), this.genre.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

}
