package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.exception.RepositoryException;
import com.epam.esm.gcs.persistence.mapper.TagRowMapper;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PostgresTagRepositoryImpl implements TagRepository {

    private final static String ID_COLUMN = "id";
    private final static String NAME_COLUMN = "name";
    private final static String TABLE_NAME = "tag";

    private final static String FIND_ALL_QUERY = "SELECT id as id, name as name FROM tag";
    private final static String FIND_BY_ID_QUERY = FIND_ALL_QUERY+" WHERE id = ?";
    private final static String UPDATE_QUERY = "UPDATE tag SET name = ? WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final TagRowMapper tagRowMapper;

    @Autowired
    private PostgresTagRepositoryImpl(DataSource dataSource, TagRowMapper tagRowMapper) {
        this.dataSource = dataSource;
        this.tagRowMapper = tagRowMapper;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_COLUMN).usingColumns(NAME_COLUMN);
    }

    @Override
    public TagModel save(TagModel tagModel) throws RepositoryException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME_COLUMN, tagModel.getName());
        return new TagModel(jdbcInsert.executeAndReturnKey(parameters).longValue(), tagModel.getName());
    }

    @Override
    public Optional<TagModel> findById(long id) throws RepositoryException {
        TagModel tagModel = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
                new Object[]{id}, new int[]{Types.BIGINT}, tagRowMapper);
        return Optional.ofNullable(tagModel);
    }

    @Override
    public Iterable<TagModel> findAll() throws RepositoryException {
        return jdbcTemplate.query(FIND_ALL_QUERY, tagRowMapper);
    }

    @Override
    public TagModel update(TagModel tagModel) throws RepositoryException {
        jdbcTemplate.update(UPDATE_QUERY, tagModel.getName(), tagModel.getId());
        return tagModel;
    }

    @Override
    public void delete(long id) throws RepositoryException {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}
