package com.pi.beesmart.model.routine;

import com.pi.beesmart.ConnectionSingleton;
import com.pi.beesmart.model.device.DeviceEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoutineDAO {

    public List<RoutineEntity> getAllRoutines() {
        final String sql = """
                SELECT
                r.id AS id,
                r.name AS name,
                r.value AS value,
                r.comparationType AS comparation,
                r.action AS action,
                GROUP_CONCAT(DISTINCT CASE WHEN g.type = 1 THEN d.id END) AS sensor,
                GROUP_CONCAT(DISTINCT CASE WHEN g.type = 0 THEN d.id END) AS actuator
                FROM BeeSmart.routine AS r
                LEFT JOIN BeeSmart.devicesOfRoutine AS dor ON r.id = dor.routine_id
                LEFT JOIN BeeSmart.device AS d ON dor.device_id = d.id
                LEFT JOIN BeeSmart.gpio AS g ON d.gpio_pinNum = g.pinNum
                GROUP BY r.id, r.name, r.value, r.comparationType, r.action;""";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql); //
             final ResultSet resultado = preparedStatement.executeQuery()) {

            List<RoutineEntity> resultadoComTodasRotinas = new ArrayList<>();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String name = resultado.getString("name");
                int value = resultado.getInt("value");
                int comparation = resultado.getInt("comparation");
                int action = resultado.getInt("action");
                int sensor = resultado.getInt("sensor");
                int actuator = resultado.getInt("actuator");

                RoutineEntity rotinaQueAcabeiDeObterDoBanco = new RoutineEntity(id, name, value, comparation, action, sensor, actuator);
                resultadoComTodasRotinas.add(rotinaQueAcabeiDeObterDoBanco);
            }

            return resultadoComTodasRotinas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
