package com.pi.beesmart.model.routine;

import com.pi.beesmart.ConnectionSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

}
