package com.pi.beesmart.model;

import com.pi.beesmart.ConnectionSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OutputDAO {

    public List<OutputEntity> getAll() {
        final String sql = "SELECT * FROM output";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql); //
                final ResultSet resultadoOutputs = preparedStatement.executeQuery()) {

            List<OutputEntity> resultadoComTodosOutputs = new ArrayList<>();

            while (resultadoOutputs.next()) {
                int gpio = resultadoOutputs.getInt("gpio");
                String name = resultadoOutputs.getString("name");
                boolean status = resultadoOutputs.getBoolean("status");
                String type = resultadoOutputs.getString("type");
                int temperature = resultadoOutputs.getInt("temperature");

                OutputEntity outputQueAcabeiDeObterDoBanco = new OutputEntity(gpio, name, status, type, temperature);
                resultadoComTodosOutputs.add(outputQueAcabeiDeObterDoBanco);
            }

            return resultadoComTodosOutputs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OutputEntity getById(int gpioFilter) {
        final String sql = "SELECT * FROM output WHERE gpio = ?";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, gpioFilter);

            try (final ResultSet resultadoOutputs = preparedStatement.executeQuery()) {

                if(!resultadoOutputs.next()){
                    return null;
                }

                resultadoOutputs.next();
                int gpio = resultadoOutputs.getInt("gpio");
                String name = resultadoOutputs.getString("name");
                boolean status = resultadoOutputs.getBoolean("status");
                String type = resultadoOutputs.getString("type");
                int temperature = resultadoOutputs.getInt("temperature");

                return new OutputEntity(gpio, name, status, type, temperature);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OutputEntity update(OutputEntity entity, int gpio) {
        final String sql = "UPDATE output SET status = ? WHERE gpio = ?";
        try (final PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            preparedStatement.setBoolean(1, entity.status);
            preparedStatement.setInt(2, gpio);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entity.gpio = gpio;
        return entity;
    }
}
