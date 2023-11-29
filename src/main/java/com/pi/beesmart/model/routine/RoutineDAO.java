package com.pi.beesmart.model.routine;

import com.pi.beesmart.ConnectionSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
@Component
public class RoutineDAO {
    @Autowired
    public ConnectionSingleton connectionSingleton;
    public List<RoutineEntity> getAllRoutines() {
        final String sql = """
                SELECT
                r.id AS id,
                r.type AS type,
                r.name AS name,
                r.time AS time,
                r.comparationType AS comparation,
                r.action AS action,
                GROUP_CONCAT(DISTINCT CASE WHEN g.type = 1 THEN d.id END) AS sensor,
                GROUP_CONCAT(DISTINCT CASE WHEN g.type = 0 THEN d.id END) AS actuator
                FROM routine AS r
                LEFT JOIN devicesofroutine AS dor ON r.id = dor.routine_id
                LEFT JOIN device AS d ON dor.device_id = d.id
                LEFT JOIN gpio AS g ON d.gpio_pinNum = g.pinNum
                GROUP BY r.id, r.name, r.time, r.comparationType, r.action;""";
        try (final PreparedStatement preparedStatement = connectionSingleton.getConnection().prepareStatement(sql); //
             final ResultSet resultado = preparedStatement.executeQuery()) {

            List<RoutineEntity> resultadoComTodasRotinas = new ArrayList<>();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                int type = resultado.getInt("type");
                String name = resultado.getString("name");
                String time = resultado.getString("time");
                int comparation = resultado.getInt("comparation");
                int action = resultado.getInt("action");
                int sensor = resultado.getInt("sensor");
                int actuator = resultado.getInt("actuator");

                RoutineEntity rotinaQueAcabeiDeObterDoBanco = new RoutineEntity(id, type, name, time, comparation, action, sensor, actuator);
                resultadoComTodasRotinas.add(rotinaQueAcabeiDeObterDoBanco);
            }

            return resultadoComTodasRotinas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RoutineEntity add(RoutineEntity entity) {
        final String sqlInsertRoutine = """
                INSERT INTO routine (name, time, type, comparationType, action)
                VALUES (?, ?, ?, ?, ?);""";
        try (final PreparedStatement preparedStatement = connectionSingleton.getConnection().prepareStatement(sqlInsertRoutine, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.name);
            preparedStatement.setString(2, entity.time);
            preparedStatement.setInt(3, entity.type);
            preparedStatement.setInt(4, entity.comparation);
            preparedStatement.setInt(5, entity.action);
            preparedStatement.executeUpdate();

            // Obtém o ID gerado (auto_increment) do produto e atualiza o objeto original
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                entity.id = rs.getInt(1);
                final String sqlInsertDevicesOfRoutine = """
                        -- Inserir um sensor associado à rotina
                        INSERT INTO devicesofroutine (routine_id, device_id)
                        VALUES (?, ?);
                        -- Inserir um atuador associado à rotina
                        INSERT INTO devicesofroutine (routine_id, device_id)
                        VALUES (?, ?);
                        """;
                try (final PreparedStatement preparedStatement2 = connectionSingleton.getConnection().prepareStatement(sqlInsertDevicesOfRoutine)) {
                    preparedStatement2.setInt(1, entity.id);
                    preparedStatement2.setInt(2, entity.sensorId);
                    preparedStatement2.setInt(3, entity.id);
                    preparedStatement2.setInt(4, entity.actuatorId);
                    preparedStatement2.executeUpdate();

                    return entity;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public RoutineEntity delete(int id) {
        final RoutineEntity rotinaASerApagada = getById(id);

        final String sql1 = "DELETE FROM routine WHERE id = ?";
        final String sql2 = "DELETE FROM devicesofroutine WHERE routine_id = ?;";
        try (final PreparedStatement preparedStatement1 = connectionSingleton.getConnection().prepareStatement(sql1)) {
            preparedStatement1.setInt(1, id);
            preparedStatement1.executeUpdate();
            try (final PreparedStatement preparedStatement2 = connectionSingleton.getConnection().prepareStatement(sql2)) {
                preparedStatement2.setInt(1, id);
                preparedStatement2.executeUpdate();
                return rotinaASerApagada;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public RoutineEntity getById(int idFilter) {
        final String sql = """
                SELECT
                r.id AS id,
                r.type AS type,
                r.name AS name,
                r.time AS time,
                r.comparationType AS comparation,
                r.action AS action,
                GROUP_CONCAT(DISTINCT CASE WHEN g.type = 1 THEN d.id END) AS sensor,
                GROUP_CONCAT(DISTINCT CASE WHEN g.type = 0 THEN d.id END) AS actuator
                FROM routine AS r
                LEFT JOIN devicesofroutine AS dor ON r.id = dor.routine_id
                LEFT JOIN device AS d ON dor.device_id = d.id
                LEFT JOIN gpio AS g ON d.gpio_pinNum = g.pinNum
                WHERE r.id = 1
                GROUP BY r.id, r.name, r.time, r.comparationType, r.action;""";
        try (final PreparedStatement preparedStatement = connectionSingleton.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idFilter);

            try (final ResultSet resultadoRotina = preparedStatement.executeQuery()) {
                if(!resultadoRotina.next()){
                    return null;
                }

                int id = resultadoRotina.getInt("id");
                int type = resultadoRotina.getInt("type");
                String name = resultadoRotina.getString("name");
                String time = resultadoRotina.getString("time");
                int comparation = resultadoRotina.getInt("comparation");
                int action = resultadoRotina.getInt("action");
                int sensor = resultadoRotina.getInt("sensor");
                int actuator = resultadoRotina.getInt("actuator");

                return new RoutineEntity(id, type, name, time, comparation, action, sensor, actuator);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
