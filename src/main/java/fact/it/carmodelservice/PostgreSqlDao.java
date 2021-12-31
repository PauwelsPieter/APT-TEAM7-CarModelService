package fact.it.carmodelservice;

import fact.it.carmodelservice.model.JdcbConnection;
import fact.it.carmodelservice.model.Carmodel;
import fact.it.carmodelservice.postgresql.spi.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlDao implements Dao<Carmodel, Integer> {

    private static final Logger LOGGER =
            Logger.getLogger(PostgreSqlDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlDao() {
        this.connection = JdcbConnection.getConnection();
    }

    @Override
    public Optional<Integer> save(Carmodel newmodel) {
        String message = "The model to be added should not be null";
        Carmodel nonNullcarmodel = Objects.requireNonNull(newmodel, message);
        String sql = "INSERT INTO modeldata(id, brandid, year, type, engine, name) VALUES(?,?,?,?,?,?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setInt(1, nonNullcarmodel.getId());
                statement.setString(2, nonNullcarmodel.getBrandId());
                statement.setString(3, nonNullcarmodel.getYear());
                statement.setString(4, nonNullcarmodel.getType());
                statement.setString(5, nonNullcarmodel.getEngine());
                statement.setString(6, nonNullcarmodel.getName());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(
                        Level.INFO,
                        "{0} created successfully? {1}",
                        new Object[]{nonNullcarmodel,
                                (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    public Optional<Carmodel> get(int id) {
        return connection.flatMap(conn -> {
            Optional<Carmodel> carmodel = Optional.empty();
            String sql = "SELECT * FROM modeldata WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    String year = resultSet.getString("year");
                    String type = resultSet.getString("type");
                    String engine = resultSet.getString("engine");
                    String name = resultSet.getString("name");
                    String brandId = resultSet.getString("brandid");

                    carmodel = Optional.of(
                            new Carmodel(id, brandId, year, type, engine, name));

                    LOGGER.log(Level.INFO, "Found {0} in database", carmodel.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return carmodel;
        });
    }

    public Collection<Carmodel> getByYear(String searchYear) {
        Collection<Carmodel> carmodels = new ArrayList<>();
        String sql = "SELECT * FROM modeldata WHERE year LIKE '" + searchYear + "'";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String year = resultSet.getString("year");
                    String type = resultSet.getString("type");
                    String engine = resultSet.getString("engine");
                    String name = resultSet.getString("name");
                    String brandId = resultSet.getString("brandid");
                    int id = resultSet.getInt("id");


                    Carmodel model = new Carmodel(id, brandId, year, type, engine, name);

                    carmodels.add(model);

                    LOGGER.log(Level.INFO, "Found {0} in database", model);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return carmodels;
    }

    public Collection<Carmodel> getByType(String searchType) {
        Collection<Carmodel> carmodels = new ArrayList<>();
        String sql = "SELECT * FROM modeldata WHERE type LIKE '" + searchType + "'";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String year = resultSet.getString("year");
                    String type = resultSet.getString("type");
                    String engine = resultSet.getString("engine");
                    String name = resultSet.getString("name");
                    String brandId = resultSet.getString("brandid");
                    int id = resultSet.getInt("id");


                    Carmodel model = new Carmodel(id, brandId, year, type, engine, name);

                    carmodels.add(model);

                    LOGGER.log(Level.INFO, "Found {0} in database", model);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return carmodels;
    }

    public Collection<Carmodel> getAll() {
        Collection<Carmodel> models = new ArrayList<>();
        String sql = "SELECT * FROM modeldata";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String year = resultSet.getString("year");
                    String type = resultSet.getString("type");
                    String engine = resultSet.getString("engine");
                    String name = resultSet.getString("name");
                    String brandId = resultSet.getString("brandid");
                    int id = resultSet.getInt("id");


                    Carmodel model = new Carmodel(id, brandId, year, type, engine, name);

                    models.add(model);

                    LOGGER.log(Level.INFO, "Found {0} in database", model);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return models;
    }

    public void update(Carmodel model) {
        String message = "The model to be updated should not be null";
        Carmodel nonNullcarmodel = Objects.requireNonNull(model, message);
        String sql = "UPDATE modeldata "
                + "SET "
                + "brandid = ?, "
                + "year = ?, "
                + "type = ?,"
                + "engine = ?, "
                + "name = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {


                statement.setString(1, nonNullcarmodel.getBrandId());
                statement.setString(2, nonNullcarmodel.getYear());
                statement.setString(3, nonNullcarmodel.getType());
                statement.setString(4, nonNullcarmodel.getEngine());
                statement.setString(5, nonNullcarmodel.getName());
                statement.setInt(6, nonNullcarmodel.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the model updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void deleteAll() {
        String sql = "DELETE FROM modeldata";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "All models deleted");

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void delete(Carmodel model) {
        String message = "The model to be deleted should not be null";
        Carmodel nonNullcarmodel = Objects.requireNonNull(model, message);
        String sql = "DELETE FROM modeldata WHERE id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullcarmodel.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the model deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void createDB() {
        String sql = "CREATE TABLE IF NOT EXISTS modeldata (id integer PRIMARY KEY, brandid VARCHAR ( 255 ), year VARCHAR ( 255 ), type VARCHAR ( 255 ), engine VARCHAR ( 255 ), name VARCHAR ( 255 ))";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                boolean succes = statement.execute();

                LOGGER.log(Level.INFO, "Table created");

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }


}