package org.example.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.example.entity.OrderCommodity;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)
public class JsonTypeHandler extends BaseTypeHandler<List<OrderCommodity>> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<OrderCommodity> parameter, JdbcType jdbcType) 
            throws SQLException {
        try {
            ps.setString(i, MAPPER.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting List<OrderCommodity> to JSON", e);
        }
    }

    @Override
    public List<OrderCommodity> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJSON(rs.getString(columnName));
    }

    @Override
    public List<OrderCommodity> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJSON(rs.getString(columnIndex));
    }

    @Override
    public List<OrderCommodity> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJSON(cs.getString(columnIndex));
    }

    private List<OrderCommodity> parseJSON(String json) throws SQLException {
        try {
            if (json == null) {
                return null;
            }
            return MAPPER.readValue(json, new TypeReference<List<OrderCommodity>>() {});
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting JSON to List<OrderCommodity>", e);
        }
    }
} 