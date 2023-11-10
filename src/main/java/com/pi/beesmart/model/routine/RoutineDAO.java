package com.pi.beesmart.model.routine;

import com.pi.beesmart.ConnectionSingleton;
import com.pi.beesmart.model.device.DeviceEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoutineDAO {

    public List<DeviceEntity> getAllRoutines() {
        final String sql = """
                SELECT d.*, a.type AS actuator_type
                FROM device AS d
                JOIN gpio AS g ON d.gpio_pinNum = g.pinNum
                JOIN actuator AS a ON g.actuator_gpioId = a.id
                WHERE g.type = 0;""";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql); //
             final ResultSet resultadoOutputs = preparedStatement.executeQuery()) {

            List<DeviceEntity> resultadoComTodosOutputs = new ArrayList<>();

            while (resultadoOutputs.next()) {
                int id = resultadoOutputs.getInt("id");
                int gpio = resultadoOutputs.getInt("gpio_pinNum");
                String name = resultadoOutputs.getString("name");
                int type = resultadoOutputs.getInt("actuator_type");

                DeviceEntity outputQueAcabeiDeObterDoBanco = new DeviceEntity(id, gpio, name, type, 0);
                resultadoComTodosOutputs.add(outputQueAcabeiDeObterDoBanco);
            }

            return resultadoComTodosOutputs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
