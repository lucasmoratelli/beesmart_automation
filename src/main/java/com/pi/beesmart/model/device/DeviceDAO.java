package com.pi.beesmart.model.device;

import com.pi.beesmart.ConnectionSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    public List<DeviceEntity> getAllActuators() {
        final String sql = """
                SELECT d.*, a.type AS actuator_type
                FROM BeeSmart.device AS d
                JOIN BeeSmart.gpio AS g ON d.gpio_pinNum = g.pinNum
                JOIN BeeSmart.actuator AS a ON g.actuator_gpioId = a.id
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

    public DeviceEntity getActuatorById(int idFilter, int typeFilter) {
        final String sql = """
                SELECT d.*, a.type AS actuator_type
                FROM BeeSmart.device AS d
                JOIN BeeSmart.gpio AS g ON d.gpio_pinNum = g.pinNum
                JOIN BeeSmart.actuator AS a ON g.actuator_gpioId = a.id
                WHERE g.type = ? AND d.id = ?;""";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, typeFilter);
            preparedStatement.setInt(2, idFilter);

            try (final ResultSet resultadoOutputs = preparedStatement.executeQuery()) {

                if(!resultadoOutputs.next()){
                    return null;
                }

                int gpio = resultadoOutputs.getInt("gpio_pinNum");
                String name = resultadoOutputs.getString("name");
                int type = resultadoOutputs.getInt("actuator_type");

                return new DeviceEntity(idFilter, gpio, name, type, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addLog(int id, int type, int value) {
        final String sql = "INSERT INTO log VALUES (null, ?, ?, ?, now(), now())";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, type);
            preparedStatement.setInt(3, value);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}